package com.chemista15.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OcrTaskCriteriaTest {

    @Test
    void newOcrTaskCriteriaHasAllFiltersNullTest() {
        var ocrTaskCriteria = new OcrTaskCriteria();
        assertThat(ocrTaskCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void ocrTaskCriteriaFluentMethodsCreatesFiltersTest() {
        var ocrTaskCriteria = new OcrTaskCriteria();

        setAllFilters(ocrTaskCriteria);

        assertThat(ocrTaskCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void ocrTaskCriteriaCopyCreatesNullFilterTest() {
        var ocrTaskCriteria = new OcrTaskCriteria();
        var copy = ocrTaskCriteria.copy();

        assertThat(ocrTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(ocrTaskCriteria)
        );
    }

    @Test
    void ocrTaskCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ocrTaskCriteria = new OcrTaskCriteria();
        setAllFilters(ocrTaskCriteria);

        var copy = ocrTaskCriteria.copy();

        assertThat(ocrTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(ocrTaskCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ocrTaskCriteria = new OcrTaskCriteria();

        assertThat(ocrTaskCriteria).hasToString("OcrTaskCriteria{}");
    }

    private static void setAllFilters(OcrTaskCriteria ocrTaskCriteria) {
        ocrTaskCriteria.id();
        ocrTaskCriteria.uuid();
        ocrTaskCriteria.mediaId();
        ocrTaskCriteria.pageNumber();
        ocrTaskCriteria.jobStatus();
        ocrTaskCriteria.createTime();
        ocrTaskCriteria.startTime();
        ocrTaskCriteria.stopTime();
        ocrTaskCriteria.distinct();
    }

    private static Condition<OcrTaskCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUuid()) &&
                condition.apply(criteria.getMediaId()) &&
                condition.apply(criteria.getPageNumber()) &&
                condition.apply(criteria.getJobStatus()) &&
                condition.apply(criteria.getCreateTime()) &&
                condition.apply(criteria.getStartTime()) &&
                condition.apply(criteria.getStopTime()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OcrTaskCriteria> copyFiltersAre(OcrTaskCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUuid(), copy.getUuid()) &&
                condition.apply(criteria.getMediaId(), copy.getMediaId()) &&
                condition.apply(criteria.getPageNumber(), copy.getPageNumber()) &&
                condition.apply(criteria.getJobStatus(), copy.getJobStatus()) &&
                condition.apply(criteria.getCreateTime(), copy.getCreateTime()) &&
                condition.apply(criteria.getStartTime(), copy.getStartTime()) &&
                condition.apply(criteria.getStopTime(), copy.getStopTime()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
