package com.chemista15.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CollectionCriteriaTest {

    @Test
    void newCollectionCriteriaHasAllFiltersNullTest() {
        var collectionCriteria = new CollectionCriteria();
        assertThat(collectionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void collectionCriteriaFluentMethodsCreatesFiltersTest() {
        var collectionCriteria = new CollectionCriteria();

        setAllFilters(collectionCriteria);

        assertThat(collectionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void collectionCriteriaCopyCreatesNullFilterTest() {
        var collectionCriteria = new CollectionCriteria();
        var copy = collectionCriteria.copy();

        assertThat(collectionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(collectionCriteria)
        );
    }

    @Test
    void collectionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var collectionCriteria = new CollectionCriteria();
        setAllFilters(collectionCriteria);

        var copy = collectionCriteria.copy();

        assertThat(collectionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(collectionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var collectionCriteria = new CollectionCriteria();

        assertThat(collectionCriteria).hasToString("CollectionCriteria{}");
    }

    private static void setAllFilters(CollectionCriteria collectionCriteria) {
        collectionCriteria.id();
        collectionCriteria.uuid();
        collectionCriteria.name();
        collectionCriteria.description();
        collectionCriteria.distinct();
    }

    private static Condition<CollectionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CollectionCriteria> copyFiltersAre(CollectionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
