# ClearWalls — Play Store publishing runbook (v1.0.8)

Everything between here and a live listing, in order. Items marked **[BLOCKER]**
must be done before uploading anything.

**Hard deadline context:** from **31 Aug 2026** new apps/updates must target
**API 36**. This release targets 35 (fine if submitted before then). Plan the
API-36 + AGP bump as the first post-launch update (see §9).

---

## 1. Build environment — DONE on this Mac (2026-07-22)

No Android Studio needed; everything runs through `./gradlew` from the terminal:
- JDK 17 (Homebrew `openjdk@17`) + Android SDK 35 (`android-commandlinetools`)
  installed; `sdk.dir` set in `local.properties`
- `keystore/clearwalls.jks` — freshly generated upload key (Play App Signing
  holds the real key). **Back up the .jks and the passwords in
  `local.properties` — both are gitignored and exist only on this machine.**
- 6 production AdMob IDs recovered from repo history into `local.properties`
- `app/google-services.json` — currently a compile-only PLACEHOLDER (the app has
  never had a real Firebase project; all historical builds used a placeholder
  too). Replace via §2 before building the final AAB.

Verified from CLI: `testDebugUnitTest` (3/3), `assembleDebug`, `bundleRelease`
(R8 + signing).

## 2. Create the real Firebase project + seed the catalog  [BLOCKER]

The app serves ONLY your own Firebase content (Pexels/Unsplash are disconnected —
their API terms prohibit wallpaper apps). Without this section, the home feed is
empty and Crashlytics/Analytics are dead.

```bash
firebase login                       # one-time browser auth (CLI already installed)
cd tools/seed_wallpapers
./setup_firebase.sh clearwalls-app   # creates project + android app, writes REAL
                                     # app/google-services.json, Firestore + rules
```

Storage requires the Blaze plan for new projects (no-cost allowances still apply
— effectively ₹0 at this app's scale): upgrade in the console, deploy
`storage.rules`, then seed per `tools/seed_wallpapers/README.md`. Target ~150
curated wallpapers across the 12 categories.

## 3. Host the privacy policy  [BLOCKER]

`docs/privacy-policy.html` is ready (contact email set). The repo is public, so
GitHub Pages can serve it directly — enable it (Settings → Pages → Deploy from
branch → `master` + `/docs`, or):

```bash
gh api repos/Rahul-999-alpha/WallCraft/pages -X POST \
  -F "source[branch]=master" -F "source[path]=/docs"
```

URL: `https://rahul-999-alpha.github.io/WallCraft/privacy-policy.html` — put it
in `local.properties` → `PRIVACY_POLICY_URL` (currently `PENDING`) and later in
Play Console → App content → Privacy policy.

## 4. AdMob console  [BLOCKER]

1. Verify the app + 5 ad units exist and IDs are in `local.properties`.
2. **Privacy & messaging → create and PUBLISH a GDPR (EU) consent message** for
   this app. The in-app UMP flow shows nothing until a message is published, and
   EEA ad serving stays limited without it. Enable the "privacy options" re-entry
   choice when creating it.
3. After the app is live on Play: AdMob → Apps → link to the Play listing, and
   complete app-ads.txt if you attach a developer website.

## 5. Verify on a device / emulator  [BLOCKER]

Compile + unit tests + R8 release pipeline are already verified from the CLI
(2026-07-22). What remains is the on-device pass — install
`app/build/outputs/apk/debug/app-debug.apk` on any phone/emulator
(`adb install`), AFTER §2 so the catalog isn't empty.

Manual pass (debug build):
- [ ] First launch: UMP consent form appears (debug geography forces EEA); after
      consent, banner/native ads load (test ads)
- [ ] Home feed + Editor's Picks load from YOUR Firestore catalog; categories work
- [ ] Search finds seeded content (single keyword, e.g. "aurora")
- [ ] Detail: download (all qualities incl. rewarded-gated 4K), set wallpaper,
      favorite, report dialog submits (check `reports` collection in console)
- [ ] AI tab: normal prompt generates; a blocked prompt (e.g. containing "nsfw")
      shows the moderation message and never hits the network; Report works
- [ ] Settings: privacy policy opens; "Ad privacy options" row appears (EEA debug)
      and reopens the form
- [ ] No READ_MEDIA_IMAGES in the merged manifest
      (`apkanalyzer manifest print` or Studio's Merged Manifest tab)
- [ ] Release build: `./gradlew bundleRelease` succeeds and requireKey() passes

## 6. Play Console — account and app setup

- New **personal** developer accounts (created after Nov 2023) must run a
  **closed test with ≥12 opted-in testers for 14 consecutive days**, then apply
  for production access. Budget 3+ weeks. Organisation accounts skip this.
- Create app → **upload the AAB** (`app/build/outputs/bundle/release/app-release.aab`;
  Play does not accept APKs for new apps). Enrol in Play App Signing (default).
- App content declarations, answered for what v1.0.8 actually does:
  - **Privacy policy**: your hosted URL
  - **Ads**: yes, contains ads
  - **Data safety**: collects Device/other IDs (advertising ID — ads), App
    interactions (analytics), Crash logs + Diagnostics (Crashlytics); all
    "collected", none "shared" beyond service providers; none optional; no
    account data. AI prompts are processed ephemerally by Pollinations — declare
    under "Other app performance data" if you want to be conservative.
  - **Content rating questionnaire**: utility/productivity app; note user-generated
    content = AI-generated images with moderation + reporting
  - **Target audience**: 13+ (do not select children)
  - **AI-generated content**: yes — in-app reporting is implemented (flag icon on
    wallpapers, Report button on AI results)
- Store listing assets you still need: 512×512 icon export, 1024×500 feature
  graphic, 4–8 phone screenshots (shoot after seeding — the catalog IS the pitch).

## 7. Closed test → production

1. Internal testing track first: install from Play, re-run the §5 checklist once.
2. Closed track: 12+ testers (friends/colleagues; each must opt in via the link
   AND install). Keep it running 14 straight days; ship at least one content
   refresh during the window so testers have a reason to reopen the app.
3. Apply for production access, answer the questionnaire honestly, then promote
   the same build to production.

## 8. What was deliberately changed for this release (context for future you)

- **Content**: catalog = owned Firestore collections + AI generation. Pexels/
  Unsplash code remains but is disconnected; do NOT re-enable for a store build.
- **Ad load**: 10-min first-session grace, native every 10 tiles, interstitial
  every 4th action with 3-min cooldown, notifications daily. Tighten only with
  retention data (Firebase Analytics day-1/day-7) — not before.
- **Consent**: UMP gathers consent before any ad request; banners/native render
  nothing until `AdManager.adsEnabled` flips.
- **AI safety**: client blocklist (`PromptModeration`) + Pollinations `safe=true`
  + in-app reporting to Firestore `reports`.

## 9. First update after launch (target: before 31 Aug 2026)

- Bump AGP 8.7.3 → 8.9.1+ (Gradle wrapper accordingly), compileSdk/targetSdk 36,
  re-test, ship as 1.0.9. This is mandatory to keep updating past 31 Aug 2026.
- Rotate `ADMIN_PASSWORD_HASH` (plaintext of the current one is in git history;
  panel is DEBUG-only so it's not shipping, but rotate anyway).
- Candidate revenue lever once retention is known: one-time "Remove ads" IAP via
  Play Billing (AdManager already has the `isPremium` seam).

## 10. Honest revenue expectations

Wallpaper is a saturated, low-eCPM category. With organic installs only, expect
pocket money, not income: AdMob pays out at $100 and that can take months at
sub-1k DAU. The two levers that actually move the needle: weekly content refreshes
(the seeder makes this a 30-minute job) and keeping the ad load light enough that
ratings stay above ~4.2. Treat the first two months as a retention experiment;
only invest further if day-7 retention clears ~10%.
