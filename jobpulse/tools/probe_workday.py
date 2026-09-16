"""Probe the Workday CXS job API used by Accenture careers."""
import json
import sys
import urllib.request

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

URL = "https://accenture.wd103.myworkdayjobs.com/wday/cxs/accenture/AccentureCareers/jobs"

payload = {
    "appliedFacets": {},
    "limit": 20,
    "offset": 0,
    "searchText": "java",
}

request = urllib.request.Request(
    URL,
    data=json.dumps(payload).encode("utf-8"),
    headers={
        "Content-Type": "application/json",
        "Accept": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
    },
    method="POST",
)

with urllib.request.urlopen(request, timeout=40) as response:
    print("status:", response.status)
    data = json.loads(response.read().decode("utf-8"))

print("top keys:", list(data.keys()))
print("total:", data.get("total"))

postings = data.get("jobPostings", [])
print("returned:", len(postings))
if postings:
    print("\nfields of first posting:")
    for key, value in postings[0].items():
        print("  ", key, "=>", str(value)[:160])

print("\nsample titles + locations:")
for posting in postings[:15]:
    print(" -", posting.get("title"), "|", posting.get("locationsText"))

facets = data.get("facets", [])
print("\nfacet groups:")
for facet in facets:
    print(" *", facet.get("facetParameter"), "-", facet.get("descriptor"))
    for value in (facet.get("values") or [])[:6]:
        print("     ", value.get("id"), value.get("descriptor"), value.get("count"))
