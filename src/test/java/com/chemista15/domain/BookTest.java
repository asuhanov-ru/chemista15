package com.chemista15.domain;

import static com.chemista15.domain.AuthorTestSamples.*;
import static com.chemista15.domain.BookTestSamples.*;
import static com.chemista15.domain.MediaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chemista15.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void authorTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        book.setAuthor(authorBack);
        assertThat(book.getAuthor()).isEqualTo(authorBack);

        book.author(null);
        assertThat(book.getAuthor()).isNull();
    }

    @Test
    void mediaTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        Media mediaBack = getMediaRandomSampleGenerator();

        book.setMedia(mediaBack);
        assertThat(book.getMedia()).isEqualTo(mediaBack);

        book.media(null);
        assertThat(book.getMedia()).isNull();
    }
}
