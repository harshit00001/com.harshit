package com.harshit.docqa.rag;

/**
 * One indexed passage. {@code source} and {@code heading} exist so an answer can cite where it
 * came from — an answer without a citation is an answer nobody can verify.
 */
public record Chunk(String id, String source, String heading, int ordinal, String text) {

    public String citationLabel() {
        return heading == null || heading.isBlank() ? source : source + " > " + heading;
    }
}
