package com.BaGulBaGul.BaGulBaGul.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JsonConfig {
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String datetimeFormat = "yyyy-MM-dd'T'HH:mm:ss";
    @Bean
    Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
                //JsonNullable에서 undefined동작을 위해 명시되지 않은 필드도 받아들임.
        builder.serializationInclusion(JsonInclude.Include.ALWAYS)
                .modulesToInstall(new JsonNullableModule())
                //LocalDateTime, LocalDate JsonFormat 설정(응답 dto -> json)
                .serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)))
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)));
        return builder;
    }
}
