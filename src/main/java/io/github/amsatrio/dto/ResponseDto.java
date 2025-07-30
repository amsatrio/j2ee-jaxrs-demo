package io.github.amsatrio.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> implements Serializable {
    private int status;
    private String message;
    private T data;
    private String timeStamp;
    
    public Response generateResponse(Response.Status status, String message, T data) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.data = data;
        this.message = message;
        this.timeStamp = simpleDateFormat.format(date);
        this.status = status.getStatusCode();
        return Response.status(status).entity(this).type(MediaType.APPLICATION_JSON).build();
    }

    public Response generateResponse(T data) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Response.Status status = Response.Status.OK;
        this.data = data;
        this.message = "success";
        this.timeStamp = simpleDateFormat.format(date);
        this.status = status.getStatusCode();
        return Response.status(status).entity(this).type(MediaType.APPLICATION_JSON).build();
    }

    public String toJsonString(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }
}
