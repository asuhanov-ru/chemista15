package com.chemista15.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AuthorCriteriaTest {

    @Test
    void newAuthorCriteriaHasAllFiltersNullTest() {
        var authorCriteria = new AuthorCriteria();
        assertThat(authorCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void authorCriteriaFluentMethodsCreatesFiltersTest() {
        var authorCriteria = new AuthorCriteria();

        setAllFilters(authorCriteria);

        assertThat(authorCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void authorCriteriaCopyCreatesNullFilterTest() {
        var authorCriteria = new AuthorCriteria();
        var copy = authorCriteria.copy();

        assertThat(authorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(authorCriteria)
        );
    }

    @Test
    void authorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var authorCriteria = new AuthorCriteria();
        setAllFilters(authorCriteria);

        var copy = authorCriteria.copy();

        assertThat(authorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(authorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var authorCriteria = new AuthorCriteria();

        assertThat(authorCriteria).hasToString("AuthorCriteria{}");
    }

    private static void setAllFilters(AuthorCriteria authorCriteria) {
        authorCriteria.id();
        authorCriteria.uuid();
        authorCriteria.name();
        authorCriteria.callsign();
        authorCriteria.bookId();
        authorCriteria.distinct();
    }

    private static Condition<AuthorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCallsign()) &&
                condition.apply(criteria.getBookId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AuthorCriteria> copyFiltersAre(AuthorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCallsign(), copy.getCallsign()) &&
                condition.apply(criteria.getBookId(), copy.getBookId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
