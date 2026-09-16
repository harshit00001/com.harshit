package com.harshit.docqa.rag.store;

import com.harshit.docqa.rag.Chunk;

public record ScoredChunk(Chunk chunk, double score) {
}
