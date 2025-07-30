package io.github.amsatrio.resources;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import io.github.amsatrio.dto.HelloWorldDto;
import io.github.amsatrio.dto.ResponseDto;
import io.github.amsatrio.service.HelloWorldService;

@Path("api")
@RequestScoped
@Provider
public class HelloWorldResource {

    @Inject
    private HelloWorldService helloWorldService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHello() {
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
        return "{\"receivedMessage\": \"" + message + "\"}";
    }

    @GET
    @Path("/hello") // Path parameter
    @Produces(MediaType.APPLICATION_JSON)
    public String find() {
        ResponseDto<Object> responseDto = new ResponseDto<>(HttpServletResponse.SC_OK, "success",
                helloWorldService.find());

        return responseDto.toJsonString();
    }

    @POST
    @Path("/hello") // Path parameter
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(HelloWorldDto helloWorldDto) {
        helloWorldDto = helloWorldService.update(helloWorldDto.getMessage());
        ResponseDto<Object> responseDto = new ResponseDto<>(HttpServletResponse.SC_OK, "success",
                helloWorldDto);

        return responseDto.toJsonString();
    }
}