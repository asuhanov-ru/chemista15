package com.chemista15.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BookTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Book getBookSample1() {
        return new Book()
            .id(1L)
            .uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .mediaStartPage(1)
            .mediaEndPage(1);
    }

    public static Book getBookSample2() {
        return new Book()
            .id(2L)
            .uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .mediaStartPage(2)
            .mediaEndPage(2);
    }

    public static Book getBookRandomSampleGenerator() {
        return new Book()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .mediaStartPage(intCount.incrementAndGet())
            .mediaEndPage(intCount.incrementAndGet());
    }
}
