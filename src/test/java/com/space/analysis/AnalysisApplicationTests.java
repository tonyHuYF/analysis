package com.space.analysis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.space.analysis.domain.Courier;
import com.space.analysis.domain.param.QueryCourierParam;
import com.space.analysis.mapper.CourierMapper;
import com.space.analysis.service.CourierService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class AnalysisApplicationTests {

//    @Resource
//    private CourierMapper courierMapper;

    @Test
    void contextLoads() {
//        List<Courier> couriers = courierMapper.queryList("444");
//        System.out.println(couriers);
    }

}
