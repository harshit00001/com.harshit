"""Probe helper: discover the JSON API behind a career page.

Usage:  python tools/probe_accenture.py <html-file>
"""
import os
import re
import sys

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

path = sys.argv[1] if len(sys.argv) > 1 else os.path.join(os.environ["TEMP"], "acc_page.html")
html = open(path, encoding="utf-8", errors="replace").read()
print("html size:", len(html))

candidates = set()
pattern = re.compile(r"""["'](https?://[^"']*|/[^"']*)["']""")
for match in pattern.finditer(html):
    url = match.group(1)
    if len(url) < 8:
        continue
    if url.endswith((".css", ".png", ".jpg", ".jpeg", ".svg", ".woff", ".woff2", ".ico", ".gif")):
        continue
    if re.search(r"api|jobsearch|joblist|jobdetail|search|graphql|\.json", url, re.I):
        candidates.add(url)

print("\n==== candidate endpoints ====")
for url in sorted(candidates)[:120]:
    print(url)

print("\n==== vendor hints ====")
for keyword in ["coveo", "algolia", "phenom", "workday", "searchHub", "organizationId",
                "accessToken", "apiKey", "sitecore", "elastic", "__NEXT_DATA__"]:
    for match in list(re.finditer(keyword, html, re.I))[:2]:
        start = max(0, match.start() - 140)
        snippet = html[start:match.start() + 200].replace("\n", " ")
        print(f"[{keyword}] {snippet}\n")
