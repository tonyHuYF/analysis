package com.space.analysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.space.analysis.domain.Courier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface CourierMapper extends BaseMapper<Courier> {
    /**
     * 批量导入
     */
    int batchInsert(@Param("list") List<T> list);

}
