"""
MCP server: fetch an internal HTTP(S) URL using HTTP Basic authentication.

Credentials and base URL come ONLY from environment variables — never hardcode secrets.
"""

from __future__ import annotations

import base64
import os
from urllib.parse import urljoin

import httpx
from mcp.server.fastmcp import FastMCP

MCP_NAME = "internal-admin-repos"

mcp = FastMCP(
    name=MCP_NAME,
    instructions=(
        "Fetches pages from an internal admin/repos (or similar) URL using HTTP Basic auth. "
        "Configure INTERNAL_MCP_BASE_URL, INTERNAL_MCP_USERNAME, INTERNAL_MCP_PASSWORD in the environment."
    ),
)


def _env(name: str, default: str | None = None) -> str | None:
    v = os.environ.get(name, default)
    if v is not None and isinstance(v, str) and v.strip() == "":
        return default
    return v


def _basic_auth_header(user: str, password: str) -> str:
    raw = f"{user}:{password}".encode("utf-8")
    return "Basic " + base64.b64encode(raw).decode("ascii")


async def run_fetch(relative_path: str = "") -> str:
    """Core HTTP GET used by the MCP tool and by test_connection.py."""
    base = _env("INTERNAL_MCP_BASE_URL")
    user = _env("INTERNAL_MCP_USERNAME")
    password = _env("INTERNAL_MCP_PASSWORD")
    if not base or not user or password is None:
        return (
            "Missing env: set INTERNAL_MCP_BASE_URL, INTERNAL_MCP_USERNAME, and INTERNAL_MCP_PASSWORD "
            "in the MCP server environment (see .env.example)."
        )

    base = base.rstrip("/") + "/"
    path = (relative_path or "").lstrip("/")
    url = urljoin(base, path) if path else base.rstrip("/")

    verify = _env("INTERNAL_MCP_VERIFY_SSL", "true")
    verify_ssl = str(verify).lower() in ("1", "true", "yes")

    accept = _env("INTERNAL_MCP_ACCEPT", "text/html,application/json;q=0.9,*/*;q=0.1")

    headers = {
        "User-Agent": "MCP-internal-admin-repos/1.0",
        "Accept": accept,
        "Authorization": _basic_auth_header(user, password),
    }

    try:
        async with httpx.AsyncClient(verify=verify_ssl, follow_redirects=True, timeout=60.0) as client:
            r = await client.get(url, headers=headers)
    except httpx.RequestError as e:
        return f"Request failed: {e!r}"

    snippet = r.text
    if len(snippet) > 120_000:
        snippet = snippet[:120_000] + "\n\n... [truncated] ..."

    return (
        f"URL: {url}\n"
        f"Status: {r.status_code} {r.reason_phrase}\n"
        f"Content-Type: {r.headers.get('content-type', '')}\n\n"
        f"{snippet}"
    )


@mcp.tool()
async def fetch_admin_repos(relative_path: str = "") -> str:
    """GET the configured internal base URL (INTERNAL_MCP_BASE_URL), optionally with a path suffix.

    Use relative_path like '' for the exact base URL, or 'api/v1/list' to append to the base
    (trailing slashes are normalized). Response body is returned as text (HTML, JSON, etc.).

    Args:
        relative_path: Path segment to append to INTERNAL_MCP_BASE_URL, or empty string.
    """
    return await run_fetch(relative_path)


def main() -> None:
    mcp.run(transport="stdio")


if __name__ == "__main__":
    main()
