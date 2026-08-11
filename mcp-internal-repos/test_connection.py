"""
Quick check: same HTTP logic as the MCP tool, without starting stdio MCP.
Run from this directory with env vars set (or a local .env file you create — do not commit).

  python test_connection.py
"""

from __future__ import annotations

import asyncio
import sys

# Optional: load .env if python-dotenv is installed (pip install python-dotenv)
try:
    from dotenv import load_dotenv

    load_dotenv()
except ImportError:
    pass

from server import run_fetch


async def main() -> int:
    out = await run_fetch("")
    print(out)
    if "Missing env:" in out:
        print("\n[FAIL] Environment not configured.", file=sys.stderr)
        return 1
    if "Request failed:" in out:
        print("\n[FAIL] Network / TLS / DNS error (see above).", file=sys.stderr)
        return 2
    status_line = next((ln for ln in out.split("\n") if ln.startswith("Status:")), "")
    print("\n[OK] Fetch completed.", status_line, file=sys.stderr)
    return 0


if __name__ == "__main__":
    raise SystemExit(asyncio.run(main()))
