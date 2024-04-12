package com.chemista15.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BookCriteriaTest {

    @Test
    void newBookCriteriaHasAllFiltersNullTest() {
        var bookCriteria = new BookCriteria();
        assertThat(bookCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void bookCriteriaFluentMethodsCreatesFiltersTest() {
        var bookCriteria = new BookCriteria();

        setAllFilters(bookCriteria);

        assertThat(bookCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void bookCriteriaCopyCreatesNullFilterTest() {
        var bookCriteria = new BookCriteria();
        var copy = bookCriteria.copy();

        assertThat(bookCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(bookCriteria)
        );
    }

    @Test
    void bookCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bookCriteria = new BookCriteria();
        setAllFilters(bookCriteria);

        var copy = bookCriteria.copy();

        assertThat(bookCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(bookCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bookCriteria = new BookCriteria();

        assertThat(bookCriteria).hasToString("BookCriteria{}");
    }

    private static void setAllFilters(BookCriteria bookCriteria) {
        bookCriteria.id();
        bookCriteria.uuid();
        bookCriteria.name();
        bookCriteria.mediaStartPage();
        bookCriteria.mediaEndPage();
        bookCriteria.authorId();
        bookCriteria.mediaId();
        bookCriteria.distinct();
    }

    private static Condition<BookCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getMediaStartPage()) &&
                condition.apply(criteria.getMediaEndPage()) &&
                condition.apply(criteria.getAuthorId()) &&
                condition.apply(criteria.getMediaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BookCriteria> copyFiltersAre(BookCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getMediaStartPage(), copy.getMediaStartPage()) &&
                condition.apply(criteria.getMediaEndPage(), copy.getMediaEndPage()) &&
                condition.apply(criteria.getAuthorId(), copy.getAuthorId()) &&
                condition.apply(criteria.getMediaId(), copy.getMediaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
