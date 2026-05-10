package com.azevedo.barberflow.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * SpringDoc maps {@link LocalDateTime} to {@code string} + {@code format: date-time}; Swagger UI then fabricates
 * samples with {@code Z}. We describe these fields as plain strings matching {@link com.fasterxml.jackson.annotation.JsonFormat}.
 */
@Component
public class LocalWallClockDateTimeModelConverter implements ModelConverter {

    private static final String EXAMPLE = "2026-05-11T12:00:00";

    private static final String PATTERN = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$";

    @PostConstruct
    void register() {
        ModelConverters.getInstance().addConverter(this);
    }

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (type == null || type.getType() == null) {
            return delegate(chain, type, context);
        }
        JavaType javaType = TypeFactory.defaultInstance().constructType(type.getType());
        if (javaType != null && LocalDateTime.class.equals(javaType.getRawClass())) {
            StringSchema schema = new StringSchema();
            schema.example(EXAMPLE);
            schema.pattern(PATTERN);
            schema.description("Local wall time (yyyy-MM-dd'T'HH:mm:ss); no timezone suffix.");
            return schema;
        }
        return delegate(chain, type, context);
    }

    private static Schema<?> delegate(Iterator<ModelConverter> chain, AnnotatedType type, ModelConverterContext context) {
        return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
    }
}
