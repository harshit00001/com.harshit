package com.harshit.docqa.rag;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Splits a document into overlapping passages.
 * <p>
 * Two deliberate choices, both worth defending in an interview:
 * <ul>
 *   <li><b>Split on markdown headings first.</b> A heading marks a topic boundary, so chunks stay
 *       about one thing. Blindly slicing every N characters mixes topics and pollutes retrieval.</li>
 *   <li><b>Overlap between chunks.</b> Without it, the one sentence that answers the question can
 *       land exactly on a boundary and be split across two chunks, so neither scores well.</li>
 * </ul>
 */
@Component
public class Chunker {

    public List<Chunk> chunk(String source, String content, int chunkSize, int overlap) {
        List<Chunk> chunks = new ArrayList<>();
        int ordinal = 0;
        for (Section section : splitByHeading(content)) {
            for (String window : slide(section.body(), chunkSize, overlap)) {
                if (window.isBlank()) {
                    continue;
                }
                String id = source + "#" + ordinal;
                chunks.add(new Chunk(id, source, section.heading(), ordinal, window.strip()));
                ordinal++;
            }
        }
        return chunks;
    }

    private List<Section> splitByHeading(String content) {
        List<Section> sections = new ArrayList<>();
        StringBuilder body = new StringBuilder();
        String heading = "";
        for (String line : content.split("\\R")) {
            if (line.startsWith("#")) {
                if (!body.isEmpty()) {
                    sections.add(new Section(heading, body.toString()));
                    body.setLength(0);
                }
                heading = line.replaceAll("^#+\\s*", "").strip();
            } else {
                body.append(line).append('\n');
            }
        }
        if (!body.isEmpty()) {
            sections.add(new Section(heading, body.toString()));
        }
        return sections;
    }

    /**
     * Sliding window with overlap, cutting on a sentence or newline boundary where one is nearby so
     * chunks read as prose rather than stopping mid-word.
     */
    private List<String> slide(String text, int chunkSize, int overlap) {
        List<String> windows = new ArrayList<>();
        String body = text.strip();
        if (body.isEmpty()) {
            return windows;
        }
        if (body.length() <= chunkSize) {
            windows.add(body);
            return windows;
        }
        int step = Math.max(1, chunkSize - overlap);
        int start = 0;
        while (start < body.length()) {
            int end = Math.min(body.length(), start + chunkSize);
            if (end < body.length()) {
                end = niceBoundary(body, start, end);
            }
            windows.add(body.substring(start, end));
            if (end >= body.length()) {
                break;
            }
            start += step;
        }
        return windows;
    }

    private int niceBoundary(String body, int start, int end) {
        int floor = start + (end - start) / 2;
        for (int i = end; i > floor; i--) {
            char c = body.charAt(i - 1);
            if (c == '\n' || c == '.' || c == '!' || c == '?') {
                return i;
            }
        }
        return end;
    }

    private record Section(String heading, String body) {
    }
}
