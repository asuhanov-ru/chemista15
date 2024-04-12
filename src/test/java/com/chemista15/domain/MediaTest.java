package com.chemista15.domain;

import static com.chemista15.domain.BookTestSamples.*;
import static com.chemista15.domain.CollectionTestSamples.*;
import static com.chemista15.domain.MediaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chemista15.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Media.class);
        Media media1 = getMediaSample1();
        Media media2 = new Media();
        assertThat(media1).isNotEqualTo(media2);

        media2.setId(media1.getId());
        assertThat(media1).isEqualTo(media2);

        media2 = getMediaSample2();
        assertThat(media1).isNotEqualTo(media2);
    }

    @Test
    void collectionTest() throws Exception {
        Media media = getMediaRandomSampleGenerator();
        Collection collectionBack = getCollectionRandomSampleGenerator();

        media.setCollection(collectionBack);
        assertThat(media.getCollection()).isEqualTo(collectionBack);

        media.collection(null);
        assertThat(media.getCollection()).isNull();
    }

    @Test
    void bookTest() throws Exception {
        Media media = getMediaRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        media.addBook(bookBack);
        assertThat(media.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getMedia()).isEqualTo(media);

        media.removeBook(bookBack);
        assertThat(media.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getMedia()).isNull();

        media.books(new HashSet<>(Set.of(bookBack)));
        assertThat(media.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getMedia()).isEqualTo(media);

        media.setBooks(new HashSet<>());
        assertThat(media.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getMedia()).isNull();
    }
}
