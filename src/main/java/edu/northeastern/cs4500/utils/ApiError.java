package edu.northeastern.cs4500.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM,
        property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String debugMessage;

    private ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, Throwable ex) {
        this(status);
        this.message = "Unexpected error";
        if (ex != null) {
            this.debugMessage = ex.getLocalizedMessage();
        }
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this(status);
        this.message = message;
        if (ex != null) {
            this.debugMessage = ex.getLocalizedMessage();
        }
    }
}

class LowerCaseClassNameResolver extends TypeIdResolverBase {

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
