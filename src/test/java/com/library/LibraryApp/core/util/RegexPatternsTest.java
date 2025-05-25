package com.library.LibraryApp.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.UUID;
import java.util.regex.Pattern;


class RegexPatternsTest {

    @Test
    public void validate_UUID_by_pattern_then_return_result(){

        Pattern UUID_REGEX = Pattern.compile(RegexPatterns.UUID);
        Assertions.assertTrue(UUID_REGEX.matcher(UUID.randomUUID().toString()).matches());
        Assertions.assertFalse(UUID_REGEX.matcher("invalid-uuid").matches());

    }
}