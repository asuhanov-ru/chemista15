package com.chemista15.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.chemista15.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OcrTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OcrTaskDTO.class);
        OcrTaskDTO ocrTaskDTO1 = new OcrTaskDTO();
        ocrTaskDTO1.setId(1L);
        OcrTaskDTO ocrTaskDTO2 = new OcrTaskDTO();
        assertThat(ocrTaskDTO1).isNotEqualTo(ocrTaskDTO2);
        ocrTaskDTO2.setId(ocrTaskDTO1.getId());
        assertThat(ocrTaskDTO1).isEqualTo(ocrTaskDTO2);
        ocrTaskDTO2.setId(2L);
        assertThat(ocrTaskDTO1).isNotEqualTo(ocrTaskDTO2);
        ocrTaskDTO1.setId(null);
        assertThat(ocrTaskDTO1).isNotEqualTo(ocrTaskDTO2);
    }
}
