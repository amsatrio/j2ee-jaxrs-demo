package io.github.amsatrio.api;

import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.github.amsatrio.annotation.MBiodataQualifier;
import io.github.amsatrio.dto.MBiodataDto;
import io.github.amsatrio.dto.ResponseDto;
import io.github.amsatrio.service.CRUDService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@Provider
@Path("api/m-biodata")
public class MBiodataApi {
    @Inject @MBiodataQualifier
    private CRUDService<MBiodataDto> crudService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@QueryParam("id") Long id) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        MBiodataDto mBiodataDto = crudService.findById(id);
        if(mBiodataDto == null){
            return responseDto.generateResponse(Response.Status.NOT_FOUND, "data not found", mBiodataDto);
        }
        return responseDto.generateResponse(mBiodataDto);
    }
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(crudService.findAll());
    }

    @GET
    @Path("/page")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPage(@QueryParam("page") int page, @QueryParam("size") int size) throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(crudService.findPage(page, size));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid MBiodataDto mBiodataDto) throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(Response.Status.CREATED, "success", crudService.create(mBiodataDto));
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Valid MBiodataDto mBiodataDto) throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(crudService.update(mBiodataDto) == 1);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("id") Long id) throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        int status = crudService.delete(id);
        return responseDto.generateResponse(status == 1);
    }
}
