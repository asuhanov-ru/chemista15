package com.chemista15.service.mapper;

import static com.chemista15.domain.OcrTaskAsserts.*;
import static com.chemista15.domain.OcrTaskTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OcrTaskMapperTest {

    private OcrTaskMapper ocrTaskMapper;

    @BeforeEach
    void setUp() {
        ocrTaskMapper = new OcrTaskMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOcrTaskSample1();
        var actual = ocrTaskMapper.toEntity(ocrTaskMapper.toDto(expected));
        assertOcrTaskAllPropertiesEquals(expected, actual);
    }
}
