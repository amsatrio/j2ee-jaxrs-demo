package io.github.amsatrio.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.github.amsatrio.dto.HelloWorldDto;
import io.github.amsatrio.dto.ResponseDto;
import io.github.amsatrio.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("hello-world-api")
@RequestScoped
@Provider
public class HelloWorldApi {

    @Inject
    private HelloWorldService helloWorldService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHello() {
        log.info("getHello()");
        return "Hello, JAX-RS!";
    }

    @GET
    @Path("/greeting")
    @Produces(MediaType.TEXT_HTML)
    public String getGreeting(@QueryParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "Guest";
        }
        return "<html><body><h1>Hello, " + name + "!</h1></body></html>";
    }

    @GET
    @Path("/{message}") // Path parameter
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomMessage(@PathParam("message") String message) {
        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.generateResponse(message);
        return responseDto.toJsonString();
    }

    @GET
    @Path("/hello") // Path parameter
    @Produces(MediaType.APPLICATION_JSON)
    public Response find() {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(helloWorldService.find());
    }

    @POST
    @Path("/hello") // Path parameter
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Valid HelloWorldDto helloWorldDto) {
        helloWorldDto = helloWorldService.update(helloWorldDto.getMessage());
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(null);
    }
}