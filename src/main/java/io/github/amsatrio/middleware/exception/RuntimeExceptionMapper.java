package io.github.amsatrio.middleware.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.github.amsatrio.dto.ResponseDto;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(Response.Status.INTERNAL_SERVER_ERROR, "something went wrong", exception);
    }
    
}
