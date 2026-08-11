# Cursor MCP setup (internal admin repos)

## 1. Install dependencies (once)

```powershell
cd "path\to\mcp-internal-repos"
python -m pip install -r requirements.txt
```

## 2. Secrets

- Use a **local** `.env` file (gitignored) or set variables in **Windows** / **Cursor MCP** config.
- **Do not** commit real passwords. Rotate any password that was ever pasted into chat.

## 3. Cursor: add MCP server

**Settings → MCP → Add new global MCP** (or project MCP) with JSON like:

```json
{
  "mcpServers": {
    "internal-admin-repos": {
      "command": "C:\\Users\\YOUR_USER\\AppData\\Local\\Programs\\Python\\Python313\\python.exe",
      "args": [
        "C:\\path\\to\\mcp-internal-repos\\server.py"
      ],
      "env": {
        "INTERNAL_MCP_BASE_URL": "http://indmzossger/admin/repos",
        "INTERNAL_MCP_USERNAME": "harshraj",
        "INTERNAL_MCP_PASSWORD": "your-password-here"
      }
    }
  }
}
```

Use your real `python.exe` path and full path to `server.py`. Prefer **env** in OS user settings instead of storing the password in JSON if the file is synced.

## 4. Verify without Cursor

```powershell
python test_connection.py
```

You should see `Status: ...` and `[OK] Fetch completed.` on stderr. If the host is only on corporate VPN, connect VPN first.
