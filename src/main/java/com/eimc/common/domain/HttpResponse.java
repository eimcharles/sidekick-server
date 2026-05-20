package com.eimc.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@SuperBuilder
@JsonInclude(NON_NULL)
@JsonPropertyOrder({"status", "errorCode", "message", "path", "payload"})
public class HttpResponse {

    protected HttpStatus status;
    protected String errorCode;
    protected String message;
    protected String path;
    protected Map<String, Object> payload;

    public HttpResponse() {}
}
