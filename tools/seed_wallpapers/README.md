# Catalog seeder

Generates original AI wallpapers (Pollinations.ai, safe mode) and uploads the
curated set to your Firebase project. This is the app's content supply — it
replaces Pexels/Unsplash, whose API terms prohibit wallpaper apps.

## One-time setup

```bash
cd tools/seed_wallpapers
python3 -m venv venv && source venv/bin/activate
pip install -r requirements.txt
```

For upload you also need:
- Firebase console → Project settings → Service accounts → **Generate new private key**
  → save as `serviceAccount.json` in this folder (gitignored).
- Your Storage bucket name (Firebase console → Storage), e.g. `my-project.appspot.com`.

## Workflow

```bash
# 1. Generate (~30s per image; ~12 categories x 15 = a few hours; resumable —
#    re-running only generates what's missing per category)
python seed.py generate --per-category 15

# 2. CURATE — this step is the product. Open output/<category>/ and delete
#    every image you wouldn't ship. Aim to keep the best ~60-70%.

# 3. Upload keepers + build categories + pick 20 editor picks
python seed.py upload --service-account serviceAccount.json \
                      --bucket <project-id>.appspot.com --picks 20

# 4. Deploy the security rules (console paste or Firebase CLI):
#    - firestore.rules  → Firestore → Rules
#    - storage.rules    → Storage → Rules
```

Verified 2026-07-22: generate path produces correct 1080x1920 JPEGs + thumbs +
manifest (see git history for the sample).

## Notes

- **Firestore schema** must stay in sync with `WallpaperMapper.toCuratedWallpaper()`.
- **Editor picks** are a random sample; to hand-pick, edit the `editor_picks`
  collection in the console (documents are full copies with an `order` field).
- **Refreshing content**: run generate/curate/upload again any time — new docs
  append to the catalog; the app needs no update. This is your "new wallpapers
  every week" lever for retention.
- **Reports**: check the `reports` collection weekly; delete reported docs from
  `curated_wallpapers`/`editor_picks` and extend the app's PromptModeration
  blocklist if a prompt pattern keeps slipping through.
- Storage bandwidth is the cost centre at scale (thumbnails are ~20 KB, fulls
  ~130 KB). The Coil/OkHttp caches keep repeat traffic low; revisit if the app
  passes a few thousand DAU.
