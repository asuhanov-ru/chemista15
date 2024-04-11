package com.chemista15.domain;

import static com.chemista15.domain.CollectionTestSamples.*;
import static com.chemista15.domain.MediaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chemista15.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CollectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collection.class);
        Collection collection1 = getCollectionSample1();
        Collection collection2 = new Collection();
        assertThat(collection1).isNotEqualTo(collection2);

        collection2.setId(collection1.getId());
        assertThat(collection1).isEqualTo(collection2);

        collection2 = getCollectionSample2();
        assertThat(collection1).isNotEqualTo(collection2);
    }

    @Test
    void mediaTest() throws Exception {
        Collection collection = getCollectionRandomSampleGenerator();
        Media mediaBack = getMediaRandomSampleGenerator();

        collection.addMedia(mediaBack);
        assertThat(collection.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getCollection()).isEqualTo(collection);

        collection.removeMedia(mediaBack);
        assertThat(collection.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getCollection()).isNull();

        collection.media(new HashSet<>(Set.of(mediaBack)));
        assertThat(collection.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getCollection()).isEqualTo(collection);

        collection.setMedia(new HashSet<>());
        assertThat(collection.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getCollection()).isNull();
    }
}
