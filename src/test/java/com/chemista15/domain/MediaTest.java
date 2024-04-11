package com.chemista15.domain;

import static com.chemista15.domain.CollectionTestSamples.*;
import static com.chemista15.domain.MediaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chemista15.web.rest.TestUtil;
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
}
