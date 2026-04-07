package com.eimc.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

///  Builder Pattern for HttpResponse
@Setter
@Getter
@SuperBuilder

/// Doesn't include empty fields in response
@JsonInclude(NON_DEFAULT)

/// Order of the HTTP response
@JsonPropertyOrder({

        "timeStamp",
        "statusCode",
        "status",
        "errorCode",
        "message",
        "path",
        "requestMethod",
        "data",
        "developerMessage"

})

public class HttpResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
    protected Instant timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String errorCode;
    protected String message;
    protected String path;
    protected String requestMethod;
    protected Map<?,?> data;
    protected String developerMessage;

    public HttpResponse() {}

    public HttpResponse(Instant timeStamp,
                        int statusCode,
                        HttpStatus status,
                        String errorCode,
                        String message,
                        String path,
                        String requestMethod,
                        Map<?, ?> data,
                        String developerMessage)
    {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
        this.requestMethod = requestMethod;
        this.data = data;
        this.developerMessage = developerMessage;
    }

}
