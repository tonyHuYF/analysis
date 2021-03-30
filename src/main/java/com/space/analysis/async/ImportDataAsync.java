package com.space.analysis.async;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.space.analysis.mapper.CourierMapper;
import com.space.analysis.mapper.SystemCourierMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 异步处理导入excel
 */
@Component
@Log4j2
public class ImportDataAsync {
    /**
     * 批量插入(仅限SystemCourierMapper、CourierMapper)
     */
    @Async
    public void batchInsert(List datas, BaseMapper mapper) {
        if (ObjectUtil.isNotEmpty(datas)) {
            log.info("========================异步插入开始===================================");
            int fromIndex = 0;
            while (true) {
                int toIndex = fromIndex + 100;
                if (toIndex > datas.size()) {
                    toIndex = datas.size();

                    if (mapper instanceof SystemCourierMapper) {
                        ((SystemCourierMapper) mapper).batchInsert(datas.subList(fromIndex, toIndex));
                    }

                    if (mapper instanceof CourierMapper) {
                        ((CourierMapper) mapper).batchInsert(datas.subList(fromIndex, toIndex));
                    }

                    break;
                }

                if (mapper instanceof SystemCourierMapper) {
                    ((SystemCourierMapper) mapper).batchInsert(datas.subList(fromIndex, toIndex));
                }

                if (mapper instanceof CourierMapper) {
                    ((CourierMapper) mapper).batchInsert(datas.subList(fromIndex, toIndex));
                }

                fromIndex = toIndex;
            }

            log.info("========================异步插入结束===================================");
        }
    }


}
