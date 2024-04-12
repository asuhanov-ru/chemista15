package com.chemista15.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MediaCriteriaTest {

    @Test
    void newMediaCriteriaHasAllFiltersNullTest() {
        var mediaCriteria = new MediaCriteria();
        assertThat(mediaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void mediaCriteriaFluentMethodsCreatesFiltersTest() {
        var mediaCriteria = new MediaCriteria();

        setAllFilters(mediaCriteria);

        assertThat(mediaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void mediaCriteriaCopyCreatesNullFilterTest() {
        var mediaCriteria = new MediaCriteria();
        var copy = mediaCriteria.copy();

        assertThat(mediaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(mediaCriteria)
        );
    }

    @Test
    void mediaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var mediaCriteria = new MediaCriteria();
        setAllFilters(mediaCriteria);

        var copy = mediaCriteria.copy();

        assertThat(mediaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(mediaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var mediaCriteria = new MediaCriteria();

        assertThat(mediaCriteria).hasToString("MediaCriteria{}");
    }

    private static void setAllFilters(MediaCriteria mediaCriteria) {
        mediaCriteria.id();
        mediaCriteria.uuid();
        mediaCriteria.fileName();
        mediaCriteria.fileType();
        mediaCriteria.fileDesc();
        mediaCriteria.collectionId();
        mediaCriteria.bookId();
        mediaCriteria.distinct();
    }

    private static Condition<MediaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getFileName()) &&
                condition.apply(criteria.getFileType()) &&
                condition.apply(criteria.getFileDesc()) &&
                condition.apply(criteria.getCollectionId()) &&
                condition.apply(criteria.getBookId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MediaCriteria> copyFiltersAre(MediaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getFileName(), copy.getFileName()) &&
                condition.apply(criteria.getFileType(), copy.getFileType()) &&
                condition.apply(criteria.getFileDesc(), copy.getFileDesc()) &&
                condition.apply(criteria.getCollectionId(), copy.getCollectionId()) &&
                condition.apply(criteria.getBookId(), copy.getBookId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
