#!/usr/bin/env python3
"""
ClearWalls catalog seeder.

Generates original AI wallpapers via Pollinations.ai (safe mode), lets you curate
them locally, then uploads the keepers to YOUR Firebase project — Storage for the
images, Firestore for the catalog documents the app pages through.

This replaces Pexels/Unsplash, whose API terms prohibit wallpaper apps. Everything
seeded here is owned content with no third-party licensing exposure.

Usage:
  1. Generate (no credentials needed; takes a while — Pollinations is free):
       python seed.py generate --per-category 15
       python seed.py generate --categories nature,space --per-category 20

  2. Curate: open output/<category>/ and DELETE any image you wouldn't ship.

  3. Upload what survived (needs a Firebase service-account key + bucket):
       python seed.py upload --service-account serviceAccount.json \
                             --bucket <project-id>.appspot.com --picks 20

  Schema written to Firestore matches WallpaperMapper.toCuratedWallpaper() —
  keep the two in sync if you change fields.
"""

import argparse
import io
import json
import random
import sys
import time
import uuid
from pathlib import Path
from urllib.parse import quote

OUTPUT_DIR = Path(__file__).parent / "output"
FULL_W, FULL_H = 1080, 1920
THUMB_W, THUMB_H = 405, 720

# Category key -> (display name, pinned, prompt templates).
# Category keys must match WallpaperRepositoryImpl.DEFAULT_CATEGORIES.
CATEGORIES = {
    "amoled": ("AMOLED Dark", True, [
        "single glowing {subject} on pure black background, minimal, OLED dark wallpaper",
        "thin neon {color} geometric lines on true black, minimalist dark wallpaper",
        "faint constellation pattern on pitch black sky, subtle, dark minimal wallpaper",
    ]),
    "nature": ("Nature", False, [
        "misty {place} at sunrise, soft golden light, breathtaking landscape photography",
        "dense pine forest in fog, moody atmosphere, vertical landscape wallpaper",
        "dramatic {place} under stormy sky, cinematic nature photography",
        "turquoise alpine lake surrounded by peaks, crystal clear reflection",
    ]),
    "abstract": ("Abstract", False, [
        "flowing liquid {color} and {color2} ink in water, abstract macro art",
        "layered translucent glass shapes, {color} palette, abstract 3d render",
        "swirling silk fabric in {color} tones, elegant abstract composition",
    ]),
    "minimal": ("Minimal", False, [
        "single {subject} centered on soft {color} background, minimalist flat design",
        "clean geometric composition, two tone {color} and white, minimal wallpaper",
        "quiet {color} color field with subtle grain, calm minimal art",
    ]),
    "space": ("Space", False, [
        "vibrant nebula with newborn stars, deep space astrophotography style",
        "ringed gas giant seen from icy moon surface, epic sci-fi vista",
        "spiral galaxy in deep darkness, cosmic scale, vertical wallpaper",
    ]),
    "city": ("City", False, [
        "rain-soaked neon street at night, reflections, cinematic cyberpunk mood",
        "skyline at blue hour from rooftop, city lights bokeh, vertical wallpaper",
        "narrow old-town alley with warm lanterns at dusk, atmospheric",
    ]),
    "gradient": ("Gradient", False, [
        "smooth {color} to {color2} gradient, soft grain, premium wallpaper",
        "aurora-like flowing gradient, {color} and {color2}, dreamy blur",
    ]),
    "texture": ("Texture", False, [
        "macro of brushed metal surface, {color} tint, premium texture",
        "handmade paper texture with subtle fibers, warm neutral tone",
        "dark slate stone texture with fine veins, close-up",
    ]),
    "art": ("Art", False, [
        "impressionist painting of {place} at golden hour, thick brushstrokes",
        "ukiyo-e style great wave with {color} sky, traditional japanese art",
        "watercolor {subject}, loose expressive style, artistic wallpaper",
    ]),
    "animals": ("Animals", False, [
        "majestic {animal} portrait, dramatic lighting, wildlife photography",
        "{animal} in natural habitat at dawn, telephoto wildlife shot",
    ]),
    "flowers": ("Flowers", False, [
        "macro of dew drops on {color} rose petals, soft morning light",
        "field of wildflowers at sunset, dreamy bokeh, vertical wallpaper",
    ]),
    "technology": ("Technology", False, [
        "circuit board macro with glowing {color} traces, tech aesthetic",
        "futuristic holographic interface floating in dark room, sci-fi",
        "server room corridor with {color} accent lighting, symmetry",
    ]),
}

FILL = {
    "subject": ["mountain", "wave", "leaf", "moon", "lotus", "koi fish", "paper crane", "bonsai tree"],
    "color":   ["deep blue", "emerald", "amber", "violet", "coral", "teal", "crimson", "slate"],
    "color2":  ["magenta", "gold", "cyan", "lavender", "peach", "midnight blue"],
    "place":   ["mountain valley", "coastal cliffs", "desert dunes", "rice terraces", "waterfall gorge"],
    "animal":  ["snow leopard", "red fox", "humpback whale", "peacock", "siberian tiger", "owl"],
}

STOPWORDS = {"a", "an", "the", "on", "in", "of", "at", "with", "and", "to", "from", "style", "wallpaper", "vertical"}


def fill_prompt(template: str) -> str:
    prompt = template
    for key, options in FILL.items():
        while "{" + key + "}" in prompt:
            prompt = prompt.replace("{" + key + "}", random.choice(options), 1)
    return prompt


def prompt_tags(prompt: str, category: str) -> list:
    words = [w.strip(",.").lower() for w in prompt.split()]
    tags = [w for w in words if len(w) >= 3 and w not in STOPWORDS and w.isalpha()]
    tags.append(category)
    return sorted(set(tags))[:20]


def generate(categories, per_category, sleep_s):
    import requests
    from PIL import Image

    manifest_path = OUTPUT_DIR / "catalog.json"
    manifest = json.loads(manifest_path.read_text()) if manifest_path.exists() else {}

    for cat in categories:
        display, pinned, templates = CATEGORIES[cat]
        cat_dir = OUTPUT_DIR / cat
        cat_dir.mkdir(parents=True, exist_ok=True)
        existing = len(list(cat_dir.glob("*.jpg")))
        needed = max(0, per_category - existing)
        print(f"[{cat}] have {existing}, generating {needed} more")

        for i in range(needed):
            prompt = fill_prompt(random.choice(templates))
            wid = f"{cat}_{uuid.uuid4().hex[:10]}"
            url = (
                "https://image.pollinations.ai/prompt/"
                + quote(prompt)
                + f"?width={FULL_W}&height={FULL_H}&nologo=true&safe=true"
                + f"&seed={random.randint(0, 10**9)}"
            )
            try:
                resp = requests.get(url, timeout=180)
                resp.raise_for_status()
                img = Image.open(io.BytesIO(resp.content)).convert("RGB")
            except Exception as e:
                print(f"  [{cat}] {i+1}/{needed} FAILED ({e}); continuing")
                time.sleep(sleep_s)
                continue

            img = img.resize((FULL_W, FULL_H)) if img.size != (FULL_W, FULL_H) else img
            img.save(cat_dir / f"{wid}.jpg", quality=90)
            img.resize((THUMB_W, THUMB_H)).save(cat_dir / f"{wid}_thumb.jpg", quality=80)

            # Dominant color = mean pixel of a tiny resize.
            r, g, b = img.resize((1, 1)).getpixel((0, 0))
            title = " ".join(prompt.split(",")[0].split()[:6]).title()
            manifest[wid] = {
                "title": title,
                "prompt": prompt,
                "category": cat,
                "tags": prompt_tags(prompt, cat),
                "width": FULL_W,
                "height": FULL_H,
                "dominantColor": f"#{r:02x}{g:02x}{b:02x}",
                "isAmoled": cat == "amoled" or (r + g + b) < 90,
            }
            manifest_path.write_text(json.dumps(manifest, indent=2))
            print(f"  [{cat}] {i+1}/{needed} ok: {title}")
            time.sleep(sleep_s)

    print(f"\nDone. Now CURATE: delete rejects from {OUTPUT_DIR}/<category>/ then run upload.")


def upload(service_account, bucket_name, picks):
    import firebase_admin
    from firebase_admin import credentials, firestore, storage

    cred = credentials.Certificate(service_account)
    firebase_admin.initialize_app(cred, {"storageBucket": bucket_name})
    db = firestore.client()
    bucket = storage.bucket()

    manifest = json.loads((OUTPUT_DIR / "catalog.json").read_text())
    uploaded = []

    def upload_blob(local: Path, remote: str) -> str:
        token = uuid.uuid4().hex
        blob = bucket.blob(remote)
        blob.metadata = {"firebaseStorageDownloadTokens": token}
        blob.upload_from_filename(str(local), content_type="image/jpeg")
        return (
            f"https://firebasestorage.googleapis.com/v0/b/{bucket_name}/o/"
            f"{quote(remote, safe='')}?alt=media&token={token}"
        )

    for wid, meta in manifest.items():
        cat = meta["category"]
        full_path = OUTPUT_DIR / cat / f"{wid}.jpg"
        thumb_path = OUTPUT_DIR / cat / f"{wid}_thumb.jpg"
        if not full_path.exists():
            continue  # curated out
        full_url = upload_blob(full_path, f"wallpapers/{cat}/{wid}.jpg")
        thumb_url = upload_blob(thumb_path, f"wallpapers/{cat}/{wid}_thumb.jpg") \
            if thumb_path.exists() else full_url

        db.collection("curated_wallpapers").document(wid).set({
            "title": meta["title"],
            "category": cat,
            "tags": meta["tags"],
            "thumbnailUrl": thumb_url,
            "previewUrl": full_url,
            "fullUrl": full_url,
            "width": meta["width"],
            "height": meta["height"],
            "dominantColor": meta["dominantColor"],
            "isAmoled": meta["isAmoled"],
            "createdAt": firestore.SERVER_TIMESTAMP,
        })
        uploaded.append((wid, cat, meta, thumb_url, full_url))
        print(f"uploaded {wid} ({cat})")

    # Categories collection (order = declaration order above).
    counts = {}
    for _, cat, *_ in uploaded:
        counts[cat] = counts.get(cat, 0) + 1
    for order, (cat, (display, pinned, _t)) in enumerate(CATEGORIES.items()):
        if counts.get(cat):
            db.collection("categories").document(cat).set({
                "name": cat,
                "displayName": display,
                "order": order,
                "isPinned": pinned,
                "count": counts[cat],
                "thumbnailUrl": next(u[3] for u in uploaded if u[1] == cat),
            })

    # Editor picks: random sample across categories.
    for order, (wid, cat, meta, thumb_url, full_url) in enumerate(
        random.sample(uploaded, min(picks, len(uploaded)))
    ):
        db.collection("editor_picks").document(wid).set({
            "title": meta["title"],
            "category": cat,
            "tags": meta["tags"],
            "thumbnailUrl": thumb_url,
            "previewUrl": full_url,
            "fullUrl": full_url,
            "width": meta["width"],
            "height": meta["height"],
            "dominantColor": meta["dominantColor"],
            "isAmoled": meta["isAmoled"],
            "order": order,
            "createdAt": firestore.SERVER_TIMESTAMP,
        })

    print(f"\nDone: {len(uploaded)} wallpapers, {len(counts)} categories, "
          f"{min(picks, len(uploaded))} editor picks.")
    print("Now deploy firestore.rules + storage.rules from this folder (see README).")


def main():
    p = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    sub = p.add_subparsers(dest="cmd", required=True)

    g = sub.add_parser("generate", help="generate AI wallpapers locally for curation")
    g.add_argument("--categories", default=",".join(CATEGORIES), help="comma-separated keys")
    g.add_argument("--per-category", type=int, default=15)
    g.add_argument("--sleep", type=float, default=6.0, help="seconds between requests (be polite)")

    u = sub.add_parser("upload", help="upload curated output/ to Firebase")
    u.add_argument("--service-account", required=True, help="path to Firebase service-account JSON")
    u.add_argument("--bucket", required=True, help="e.g. my-project.appspot.com")
    u.add_argument("--picks", type=int, default=20, help="number of editor picks")

    args = p.parse_args()
    if args.cmd == "generate":
        cats = [c.strip() for c in args.categories.split(",") if c.strip()]
        unknown = [c for c in cats if c not in CATEGORIES]
        if unknown:
            sys.exit(f"Unknown categories: {unknown}. Valid: {list(CATEGORIES)}")
        generate(cats, args.per_category, args.sleep)
    else:
        upload(args.service_account, args.bucket, args.picks)


if __name__ == "__main__":
    main()
