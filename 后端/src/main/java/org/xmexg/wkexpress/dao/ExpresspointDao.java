package org.xmexg.wkexpress.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.xmexg.wkexpress.entity.Expresspoint;

import java.util.List;

/**
 * 获取快递点
 */
@Mapper
public interface ExpresspointDao {
    @Select("select point from expresspoint")
    public List<Expresspoint> getAll();
}
