package com.chemista15.domain;

import static com.chemista15.domain.OcrTaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chemista15.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrTask.class);
        OcrTask ocrTask1 = getOcrTaskSample1();
        OcrTask ocrTask2 = new OcrTask();
        assertThat(ocrTask1).isNotEqualTo(ocrTask2);

        ocrTask2.setId(ocrTask1.getId());
        assertThat(ocrTask1).isEqualTo(ocrTask2);

        ocrTask2 = getOcrTaskSample2();
        assertThat(ocrTask1).isNotEqualTo(ocrTask2);
    }
}
