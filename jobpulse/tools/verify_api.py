"""Smoke-check the running JobPulse API."""
import json
import sys
import urllib.request

sys.stdout.reconfigure(encoding="utf-8", errors="replace")

BASE = "http://localhost:8090"


def get(path):
    with urllib.request.urlopen(BASE + path, timeout=30) as response:
        return json.loads(response.read().decode("utf-8"))


stats = get("/api/crawl/stats")
print("=== STATS ===")
print(json.dumps(stats, indent=2))

print("\n=== TOP MATCHED JAVA ROLES (minScore=60, matchedOnly) ===")
result = get("/api/jobs/search?matchedOnly=true&minScore=60&size=12")
print("total:", result["totalElements"])
for job in result["jobs"]:
    print(f"  [{job['matchScore']:>3}] {job['company']:<10} | {job['city'] or '-':<12} "
          f"| {job['title'][:52]}")
    print(f"        skills: {', '.join(job['matchedSkills'][:8])}")

print("\n=== SKILL FILTER: Java + Spring Boot + Kubernetes ===")
result = get("/api/jobs/search?skills=Java,Spring%20Boot,Kubernetes&size=10")
print("returned:", len(result["jobs"]))
for job in result["jobs"]:
    print(f"  [{job['matchScore']:>3}] {job['company']} | {job['city']} | {job['title'][:50]}")

print("\n=== CITY FILTER: Pune ===")
result = get("/api/jobs/search?city=pune&size=10")
print("returned:", len(result["jobs"]))
for job in result["jobs"]:
    print(f"  [{job['matchScore']:>3}] {job['company']} | {job['city']} | {job['title'][:50]}")

print("\n=== INDIA-ONLY CHECK: distinct countries and cities in the index ===")
countries = {}
cities = set()
page = get("/api/jobs/search?size=100")
for job in page["jobs"]:
    countries[job["country"] or "(none)"] = countries.get(job["country"] or "(none)", 0) + 1
    if job["city"]:
        cities.add(job["city"])
print("countries:", countries)
print("cities:", sorted(cities))
