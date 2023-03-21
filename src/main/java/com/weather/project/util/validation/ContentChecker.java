package com.weather.project.util.validation;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class ContentChecker {

    public boolean isEmpty(String... content) {
        if (!isNull(content)) {
            return Arrays.stream(content).anyMatch(String::isEmpty);
        } else {
            return true;
        }
    }

    public boolean isNull (String... content) {
        return Arrays.stream(content).anyMatch(Objects::isNull);
    }
}
