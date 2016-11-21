package com.koval.storage.converter;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Created by Volodymyr Kovalenko
 */
@Component
public class InstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(String source) {
        if (NumberUtils.isCreatable(source)) {
            return Instant.ofEpochMilli(NumberUtils.toLong(source));
        }
        return null;
    }
}
