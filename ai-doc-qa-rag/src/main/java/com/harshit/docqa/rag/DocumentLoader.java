package com.harshit.docqa.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Reads plain-text documents from a folder. Markdown, text and source files are supported because
 * they need no parsing library; PDF support is a documented upgrade (add Apache PDFBox and convert
 * to text before chunking).
 */
@Component
public class DocumentLoader {

    private static final Logger log = LoggerFactory.getLogger(DocumentLoader.class);
    private static final List<String> SUPPORTED = List.of(".md", ".txt", ".java", ".yml", ".yaml", ".json", ".sql");
    private static final long MAX_BYTES = 2 * 1024 * 1024;

    public List<LoadedDocument> load(Path root) {
        if (!Files.isDirectory(root)) {
            log.warn("docs path {} does not exist, nothing indexed", root.toAbsolutePath());
            return List.of();
        }
        List<LoadedDocument> documents = new ArrayList<>();
        try (Stream<Path> files = Files.walk(root)) {
            files.filter(Files::isRegularFile)
                    .filter(this::supported)
                    .forEach(path -> read(root, path).ifPresent(documents::add));
        } catch (IOException e) {
            throw new UncheckedIOException("failed to scan " + root, e);
        }
        return documents;
    }

    private boolean supported(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return SUPPORTED.stream().anyMatch(name::endsWith);
    }

    private java.util.Optional<LoadedDocument> read(Path root, Path path) {
        try {
            if (Files.size(path) > MAX_BYTES) {
                log.warn("skipping {} ({} bytes) - larger than the {} byte limit",
                        path.getFileName(), Files.size(path), MAX_BYTES);
                return java.util.Optional.empty();
            }
            String content = Files.readString(path);
            String relative = root.relativize(path).toString().replace('\\', '/');
            return java.util.Optional.of(new LoadedDocument(relative, content));
        } catch (IOException e) {
            log.warn("could not read {}: {}", path, e.getMessage());
            return java.util.Optional.empty();
        }
    }

    public record LoadedDocument(String source, String content) {
    }
}
