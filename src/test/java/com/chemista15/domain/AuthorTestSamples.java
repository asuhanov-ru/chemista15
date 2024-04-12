package com.chemista15.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Author getAuthorSample1() {
        return new Author().id(1L).uuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1").callsign("callsign1");
    }

    public static Author getAuthorSample2() {
        return new Author().id(2L).uuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2").callsign("callsign2");
    }

    public static Author getAuthorRandomSampleGenerator() {
        return new Author()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .callsign(UUID.randomUUID().toString());
    }
}
