package com.chemista15.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OcrTaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OcrTask getOcrTaskSample1() {
        return new OcrTask()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .mediaId(1L)
            .pageNumber(1)
            .jobStatus("jobStatus1");
    }

    public static OcrTask getOcrTaskSample2() {
        return new OcrTask()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .mediaId(2L)
            .pageNumber(2)
            .jobStatus("jobStatus2");
    }

    public static OcrTask getOcrTaskRandomSampleGenerator() {
        return new OcrTask()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .mediaId(longCount.incrementAndGet())
            .pageNumber(intCount.incrementAndGet())
            .jobStatus(UUID.randomUUID().toString());
    }
}
