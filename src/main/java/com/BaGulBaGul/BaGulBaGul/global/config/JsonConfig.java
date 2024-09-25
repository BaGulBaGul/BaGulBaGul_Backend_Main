package com.BaGulBaGul.BaGulBaGul.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
            .serializationInclusion(JsonInclude.Include.ALWAYS)
            //PATCH 요청에서 명시하지 않은 필드와 null값을 구분하기 위한 JsonNullable 모듈
            .modulesToInstall(new JsonNullableModule())
            //LocalDateTime, LocalDate JsonFormat 설정(응답 dto -> json)
            .serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)))
            .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)));
    private static final ObjectMapper objectMapper = builder.build();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Bean
    Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return builder;
    }
}
