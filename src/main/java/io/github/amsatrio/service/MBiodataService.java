package io.github.amsatrio.service;

import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import io.github.amsatrio.annotation.MBiodataQualifier;
import io.github.amsatrio.dao.MBiodataDao;
import io.github.amsatrio.dto.MBiodataDto;
import io.github.amsatrio.dto.PageDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@MBiodataQualifier
public class MBiodataService implements CRUDService<MBiodataDto> {
    @Inject
    private MBiodataDao mBiodataDao;

    @Override
    public MBiodataDto findById(Long id) {
        return mBiodataDao.findById(id);
    }

    @Override
    public List<MBiodataDto> findAll() throws SQLException {
        return mBiodataDao.findAll();

    }

    @Override
    public PageDto<MBiodataDto> findPage(int page, int size) throws SQLException {
        PageDto<MBiodataDto> pageDto = new PageDto<>();
        pageDto.setContent(mBiodataDao.findPage(page, size));
        pageDto.setEmpty(pageDto.getContent().isEmpty());
        pageDto.setTotalElements(mBiodataDao.countData());
        pageDto.setTotalPages(pageDto.getTotalElements() / size);
        if (pageDto.getTotalElements() % size != 0) {
            pageDto.setTotalPages(pageDto.getTotalPages() + 1);
        }
        pageDto.setFirst(page == 0);
        pageDto.setLast(page >= pageDto.getTotalPages() - 1);
        return pageDto;
    }

    @Override
    public MBiodataDto create(MBiodataDto dto) throws SQLException {
        return mBiodataDao.create(dto);
    }

    @Override
    public int update(MBiodataDto dto) throws SQLException {
        return mBiodataDao.update(dto);
    }

    @Override
    public int delete(Long id) throws SQLException {
        return mBiodataDao.deleteById(id);
    }

}
