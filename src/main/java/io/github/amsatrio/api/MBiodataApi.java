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

import io.github.amsatrio.dao.MBiodataDao;
import io.github.amsatrio.dto.MBiodataDto;
import io.github.amsatrio.dto.PageDto;
import io.github.amsatrio.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@Provider
@Path("api/m-biodata")
public class MBiodataApi {
    @Inject
    private MBiodataDao mBiodataDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@QueryParam("id") Long id) {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        MBiodataDto mBiodataDto = mBiodataDao.findById(id);
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
        return responseDto.generateResponse(mBiodataDao.findAll());
    }

    @GET
    @Path("/page")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPage(@QueryParam("page") int page, @QueryParam("size") int size) throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        PageDto<MBiodataDto> pageDto = new PageDto<>();
        pageDto.init(mBiodataDao.findPage(page, size), Long.valueOf(page), Long.valueOf(size), mBiodataDao.countData());
        return responseDto.generateResponse(pageDto);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid MBiodataDto mBiodataDto) throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(Response.Status.CREATED, "success", mBiodataDao.create(mBiodataDto));
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Valid MBiodataDto mBiodataDto) throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        return responseDto.generateResponse(mBiodataDao.update(mBiodataDto));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("id") Long id) throws SQLException {
        ResponseDto<Object> responseDto = new ResponseDto<>();
        int status = mBiodataDao.deleteById(id);
        return responseDto.generateResponse(status == 1);
    }
}
