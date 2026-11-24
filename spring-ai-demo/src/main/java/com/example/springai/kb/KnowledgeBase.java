package com.example.springai.kb;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * Tiny retrieval layer for grounding (RAG-lite): keyword overlap over classpath:kb/*.txt.
 * In production swap for a vector store (Spring AI VectorStore / pgvector); the workflow only sees {@link #search}.
 */
@Component
public class KnowledgeBase {

    public record Doc(String id, String text) {}

    private static final Set<String> STOP = Set.of("the", "a", "an", "is", "are", "of", "to", "for", "and", "in",
            "on", "what", "how", "do", "does", "can", "i", "my", "me", "it", "be", "with", "at", "or", "you");

    private final List<Doc> docs;

    public KnowledgeBase() {
        this.docs = load();
    }

    public KnowledgeBase(List<Doc> docs) {
        this.docs = docs;
    }

    public List<Doc> all() {
        return docs;
    }

    /** Top-k documents sharing at least one meaningful token with the query, best first. */
    public List<Doc> search(String query, int k) {
        Set<String> q = tokens(query);
        return docs.stream()
                .map(d -> new Scored(d, overlap(q, tokens(d.text()))))
                .filter(s -> s.score > 0)
                .sorted(Comparator.comparingInt((Scored s) -> s.score).reversed())
                .limit(k)
                .map(s -> s.doc)
                .toList();
    }

    private record Scored(Doc doc, int score) {}

    private static int overlap(Set<String> a, Set<String> b) {
        Set<String> common = new HashSet<>(a);
        common.retainAll(b);
        return common.size();
    }

    static Set<String> tokens(String text) {
        Set<String> out = new HashSet<>();
        Arrays.stream(text.toLowerCase().split("[^a-z0-9]+"))
                .filter(t -> t.length() > 1 && !STOP.contains(t))
                .forEach(out::add);
        return out;
    }

    private static List<Doc> load() {
        try {
            Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:kb/*.txt");
            return Arrays.stream(res).map(r -> {
                try {
                    String name = r.getFilename().replace(".txt", "");
                    return new Doc(name, new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }).toList();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load knowledge base", e);
        }
    }
}
