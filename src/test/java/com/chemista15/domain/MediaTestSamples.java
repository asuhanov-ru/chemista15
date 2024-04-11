package com.chemista15.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Media getMediaSample1() {
        return new Media()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .fileName("fileName1")
            .fileType("fileType1")
            .fileDesc("fileDesc1");
    }

    public static Media getMediaSample2() {
        return new Media()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .fileName("fileName2")
            .fileType("fileType2")
            .fileDesc("fileDesc2");
    }

    public static Media getMediaRandomSampleGenerator() {
        return new Media()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .fileName(UUID.randomUUID().toString())
            .fileType(UUID.randomUUID().toString())
            .fileDesc(UUID.randomUUID().toString());
    }
}
