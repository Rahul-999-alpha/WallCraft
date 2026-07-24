#!/usr/bin/env bash
set -euo pipefail

# One-shot Firebase bootstrap for ClearWalls. Run AFTER `firebase login`.
#
#   ./setup_firebase.sh [project-id]
#
# Creates the Firebase project + Android app, writes the REAL
# app/google-services.json (replacing the compile-only placeholder), creates
# the Firestore database, and deploys firestore.rules.
#
# Storage note: since Oct 2024 a Storage bucket requires the Blaze plan
# (no-cost allowances still apply). Upgrade in the console, then run:
#   firebase deploy --only storage --project <project-id>
# and use the bucket name (<project-id>.firebasestorage.app) for seed.py upload.

cd "$(dirname "$0")"
PROJECT_ID="${1:-clearwalls-app-$RANDOM}"
PACKAGE="com.clearwalls"
REPO_ROOT="$(cd ../.. && pwd)"

echo "==> Creating Firebase project: $PROJECT_ID"
firebase projects:create "$PROJECT_ID" --display-name "ClearWalls"

echo "==> Registering Android app ($PACKAGE)"
APP_ID=$(firebase apps:create android "$PACKAGE" --project "$PROJECT_ID" --json \
  | python3 -c "import json,sys; print(json.load(sys.stdin)['result']['appId'])")
echo "    appId: $APP_ID"

echo "==> Writing real app/google-services.json"
firebase apps:sdkconfig android "$APP_ID" --project "$PROJECT_ID" --json \
  | python3 -c "import json,sys; print(json.load(sys.stdin)['result']['fileContents'])" \
  > "$REPO_ROOT/app/google-services.json"
python3 -c "import json; json.load(open('$REPO_ROOT/app/google-services.json'))" \
  && echo "    valid JSON written"

echo "==> Creating Firestore database (asia-south1 / Mumbai)"
firebase firestore:databases:create "(default)" --location=asia-south1 --project "$PROJECT_ID"

echo "==> Deploying Firestore rules"
firebase deploy --only firestore --project "$PROJECT_ID"

cat <<EOF

Done. Remaining manual bits:
  1. (Storage) Upgrade project to Blaze in the Firebase console, then:
       firebase deploy --only storage --project $PROJECT_ID
  2. Service account key for the seeder:
       console -> Project settings -> Service accounts -> Generate new private key
       save as tools/seed_wallpapers/serviceAccount.json
  3. Seed the catalog:
       python seed.py upload --service-account serviceAccount.json \\
                             --bucket $PROJECT_ID.firebasestorage.app --picks 20
  4. Rebuild the app so the real google-services.json is baked in.
EOF
