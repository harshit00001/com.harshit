package com.harshit.docqa.rag;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Tokenisation shared by the embedder and the answerer.
 * <p>
 * Both must agree on what a word is, otherwise a passage can rank first for a question and then
 * score badly when its sentences are compared to that same question. Question words such as "what"
 * and "how" are dropped because they appear in nearly every question and carry no signal about which
 * passage answers it.
 * <p>
 * <b>This list is deliberately short, and measured rather than guessed.</b> Extending it with
 * plausible-looking filler words ("get", "give", "all", "one") dropped evaluation accuracy from 15/16
 * to 13/16, because in a corpus about Java "get" is a domain term — {@code join} versus {@code get}
 * is one of the questions the corpus answers. A stop-word list is corpus-specific, and the only way
 * to know whether a word carries signal is to run the evaluation both ways.
 */
public final class Text {

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "a", "an", "and", "or", "but", "if", "then", "than", "that", "this", "these",
            "those", "is", "are", "was", "were", "be", "been", "being", "to", "of", "in", "on",
            "for", "with", "as", "by", "at", "from", "it", "its", "we", "you", "your", "our",
            "can", "will", "would", "should", "could", "do", "does", "did", "how", "what", "why",
            "when", "which", "who", "not", "no", "so", "into", "about", "there", "their", "they");

    private Text() {
    }

    /** Ordered content words, stemmed. Keeps {@code +} and {@code #} so c++ and c# survive. */
    public static List<String> tokens(String text) {
        String[] raw = text.toLowerCase(Locale.ROOT).split("[^a-z0-9+#.]+");
        List<String> tokens = new ArrayList<>(raw.length);
        for (String token : raw) {
            String cleaned = token.replaceAll("^\\.+|\\.+$", "");
            if (cleaned.length() < 2 || STOP_WORDS.contains(cleaned)) {
                continue;
            }
            tokens.add(stem(cleaned));
        }
        return tokens;
    }

    /**
     * Light suffix stripping, the standard first step of a lexical retrieval pipeline. Without it
     * "defaults" and "default" are unrelated tokens, so a question asking what something defaults to
     * does not match the sentence that says it. Only inflections are removed, and only on words long
     * enough that the stem stays recognisable.
     */
    private static String stem(String token) {
        if (token.length() < 5) {
            return token;
        }
        if (token.endsWith("ies")) {
            return token.substring(0, token.length() - 3) + "y";
        }
        if (token.endsWith("sses")) {
            return token.substring(0, token.length() - 2);
        }
        if (token.endsWith("s") && !token.endsWith("ss") && !token.endsWith("us")) {
            return token.substring(0, token.length() - 1);
        }
        if (token.endsWith("ing") && token.length() > 6) {
            return token.substring(0, token.length() - 3);
        }
        if (token.endsWith("ed") && token.length() > 5) {
            return token.substring(0, token.length() - 2);
        }
        return token;
    }

    public static Set<String> uniqueTokens(String text) {
        return new LinkedHashSet<>(tokens(text));
    }
}
