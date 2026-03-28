#!/usr/bin/env node
/**
 * Generic MCP server: lists latest entries from ONE RSS/Atom URL (public feeds only).
 *
 * This does NOT integrate with Naukri or any site that forbids automated access.
 * Use Naukri's own job alerts inside their app or email.
 */
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";
import Parser from "rss-parser";

const FEED_URL = process.env.JOB_ALERTS_RSS_URL || "";

const parser = new Parser({
  customFields: {
    item: ["category", "categories"],
  },
});

async function fetchFeed(url) {
  if (!url || !url.startsWith("http")) {
    return {
      error:
        "Set JOB_ALERTS_RSS_URL to a public RSS/Atom URL (https://...). Example: a company careers RSS if they publish one.",
    };
  }
  const feed = await parser.parseURL(url);
  const items = (feed.items || []).slice(0, 30).map((it) => ({
    title: it.title || "",
    link: it.link || "",
    pubDate: it.pubDate || it.isoDate || "",
    tags: extractTags(it),
    contentSnippet: (it.contentSnippet || it.summary || "").slice(0, 500),
  }));
  return {
    feedTitle: feed.title || "",
    feedLink: feed.link || "",
    itemCount: items.length,
    items,
  };
}

function extractTags(item) {
  const cats = item.categories || item.category;
  if (Array.isArray(cats)) return cats.map(String);
  if (typeof cats === "string") return [cats];
  return [];
}

const server = new McpServer({
  name: "job-alerts-rss-mcp",
  version: "1.0.0",
});

server.registerTool(
  "list_latest_job_alerts",
  {
    description:
      "Returns latest entries from the configured RSS feed (JOB_ALERTS_RSS_URL). Tags come from RSS categories when present.",
    inputSchema: z.object({
      limit: z.number().optional().describe("Max items (default 15, max 30)"),
    }),
  },
  async ({ limit }) => {
    const data = await fetchFeed(FEED_URL);
    if (data.error) {
      return {
        content: [{ type: "text", text: JSON.stringify(data, null, 2) }],
        isError: true,
      };
    }
    const n = Math.min(Math.max(limit ?? 15, 1), 30);
    const sliced = { ...data, items: data.items.slice(0, n) };
    return {
      content: [{ type: "text", text: JSON.stringify(sliced, null, 2) }],
    };
  }
);

const transport = new StdioServerTransport();
await server.connect(transport);
