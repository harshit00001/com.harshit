"""Verify Workday India country facet + job detail endpoint for Accenture."""
import json
import sys
import urllib.request

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

BASE = "https://accenture.wd103.myworkdayjobs.com/wday/cxs/accenture/AccentureCareers"
INDIA_COUNTRY_ID = "c4f78be1a8f14da0ab49ce1162348a5e"

HEADERS = {
    "Content-Type": "application/json",
    "Accept": "application/json",
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
}


def post(url, payload):
    request = urllib.request.Request(
        url, data=json.dumps(payload).encode("utf-8"), headers=HEADERS, method="POST"
    )
    with urllib.request.urlopen(request, timeout=40) as response:
        return json.loads(response.read().decode("utf-8"))


def get(url):
    request = urllib.request.Request(url, headers=HEADERS, method="GET")
    with urllib.request.urlopen(request, timeout=40) as response:
        return json.loads(response.read().decode("utf-8"))


search = post(
    BASE + "/jobs",
    {
        "appliedFacets": {"locationCountry": [INDIA_COUNTRY_ID]},
        "limit": 20,
        "offset": 0,
        "searchText": "java",
    },
)

print("INDIA total:", search.get("total"))
postings = search.get("jobPostings", [])
for posting in postings[:12]:
    print(" -", posting.get("title"), "|", posting.get("bulletFields"), "|", posting.get("externalPath"))

if postings:
    path = postings[0]["externalPath"]
    detail = get(BASE + path)
    info = detail.get("jobPostingInfo", {})
    print("\n=== DETAIL KEYS ===")
    print(list(info.keys()))
    for key in ["title", "jobRequisitionId", "location", "country", "postedOn",
                "startDate", "timeType", "jobPostingId", "externalUrl"]:
        print(key, "=>", str(info.get(key))[:200])
    description = info.get("jobDescription") or ""
    print("\ndescription length:", len(description))
    print("description head:", description[:400].replace("\n", " "))
