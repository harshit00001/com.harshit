package com.harshit.docqa.rag.embed;

/**
 * The two lines of maths that a vector database performs for you.
 * <p>
 * Vectors are L2-normalised on creation, which makes cosine similarity equal to the dot product —
 * that is exactly why pgvector, Pinecone and friends recommend normalised embeddings.
 */
public final class Vectors {

    private Vectors() {
    }

    public static void normalise(float[] vector) {
        double sumOfSquares = 0;
        for (float value : vector) {
            sumOfSquares += value * value;
        }
        double magnitude = Math.sqrt(sumOfSquares);
        if (magnitude == 0) {
            return;
        }
        for (int i = 0; i < vector.length; i++) {
            vector[i] = (float) (vector[i] / magnitude);
        }
    }

    /** Cosine similarity in [-1, 1]; equals the dot product when both inputs are normalised. */
    public static double cosine(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException(
                    "dimension mismatch: " + a.length + " vs " + b.length
                            + " - the index was built with a different embedding model, so it must be rebuilt");
        }
        double dot = 0;
        double normA = 0;
        double normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) {
            return 0;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
