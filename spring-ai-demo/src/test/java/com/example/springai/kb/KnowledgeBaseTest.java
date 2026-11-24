package com.example.springai.kb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class KnowledgeBaseTest {

    @Test
    void classpathDocumentsAreLoaded() {
        assertThat(new KnowledgeBase().all()).extracting(KnowledgeBase.Doc::id)
                .contains("returns-policy", "shipping", "warranty");
    }

    @Test
    void searchRanksByOverlapAndIgnoresStopWords() {
        KnowledgeBase kb = new KnowledgeBase(List.of(
                new KnowledgeBase.Doc("a", "refund within 30 days of delivery"),
                new KnowledgeBase.Doc("b", "shipping takes 3 days")));

        assertThat(kb.search("what is the refund window after delivery", 2))
                .extracting(KnowledgeBase.Doc::id).containsExactly("a");
        assertThat(kb.search("the of and", 2)).isEmpty();
    }
}
