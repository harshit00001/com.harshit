package com.harshit.docqa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Every knob that changes retrieval behaviour lives here, because tuning a RAG pipeline is
 * mostly about these six numbers rather than about the model.
 */
@ConfigurationProperties(prefix = "rag")
public class RagProperties {

    /** Folder scanned for documents to index. Point it at any notes folder. */
    private String docsPath = "./docs";

    /** Target characters per chunk. Too small loses context, too large dilutes the match. */
    private int chunkSize = 900;

    /** Characters repeated between neighbouring chunks so a sentence is never cut in half. */
    private int chunkOverlap = 150;

    /** How many chunks are retrieved and put in front of the answerer. */
    private int topK = 4;

    /**
     * Similarity floor. Below this the service refuses to answer instead of guessing, which is
     * the difference between a demo and something you would put in front of users.
     */
    private double minScore = 0.14;

    /** local | ollama | openai */
    private String embeddingProvider = "local";

    /** extractive | ollama | openai */
    private String answerProvider = "extractive";

    private String evalFile = "./eval/questions.json";

    private final Ollama ollama = new Ollama();
    private final OpenAi openai = new OpenAi();

    public static class Ollama {
        private String baseUrl = "http://localhost:11434";
        private String embeddingModel = "nomic-embed-text";
        private String chatModel = "llama3.2";

        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getEmbeddingModel() { return embeddingModel; }
        public void setEmbeddingModel(String embeddingModel) { this.embeddingModel = embeddingModel; }
        public String getChatModel() { return chatModel; }
        public void setChatModel(String chatModel) { this.chatModel = chatModel; }
    }

    public static class OpenAi {
        private String baseUrl = "https://api.openai.com";
        private String apiKey = "";
        private String embeddingModel = "text-embedding-3-small";
        private String chatModel = "gpt-4o-mini";

        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getEmbeddingModel() { return embeddingModel; }
        public void setEmbeddingModel(String embeddingModel) { this.embeddingModel = embeddingModel; }
        public String getChatModel() { return chatModel; }
        public void setChatModel(String chatModel) { this.chatModel = chatModel; }
    }

    public String getDocsPath() { return docsPath; }
    public void setDocsPath(String docsPath) { this.docsPath = docsPath; }
    public int getChunkSize() { return chunkSize; }
    public void setChunkSize(int chunkSize) { this.chunkSize = chunkSize; }
    public int getChunkOverlap() { return chunkOverlap; }
    public void setChunkOverlap(int chunkOverlap) { this.chunkOverlap = chunkOverlap; }
    public int getTopK() { return topK; }
    public void setTopK(int topK) { this.topK = topK; }
    public double getMinScore() { return minScore; }
    public void setMinScore(double minScore) { this.minScore = minScore; }
    public String getEmbeddingProvider() { return embeddingProvider; }
    public void setEmbeddingProvider(String embeddingProvider) { this.embeddingProvider = embeddingProvider; }
    public String getAnswerProvider() { return answerProvider; }
    public void setAnswerProvider(String answerProvider) { this.answerProvider = answerProvider; }
    public String getEvalFile() { return evalFile; }
    public void setEvalFile(String evalFile) { this.evalFile = evalFile; }
    public Ollama getOllama() { return ollama; }
    public OpenAi getOpenai() { return openai; }
}
