package com.space.analysis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.space.analysis.domain.Courier;
import com.space.analysis.domain.ResultBean;
import com.space.analysis.domain.param.QueryCourierParam;
import com.space.analysis.domain.param.QueryErrorCourierDataParam;
import com.space.analysis.model.ErrorCourierData;
import com.space.analysis.service.CourierService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * 快递数据-
 */
@RestController
@RequestMapping("/courier")
public class CourierController {
    @Resource
    private CourierService courierService;

    /**
     * 增加
     */
    @PostMapping("/add")
    public ResultBean<Courier> insert(Courier courier) {
        return new ResultBean<>(courierService.insert(courier));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResultBean delete(String id) {
        courierService.delete(id);
        return new ResultBean<>();
    }

    /**
     * 修改
     */
    @PutMapping
    public ResultBean<Courier> update(Courier courier) {
        return new ResultBean<>(courierService.update(courier));
    }

    /**
     * 查看单条
     */
    @GetMapping("/{id}")
    public ResultBean<Courier> queryOne(String id) {
        return new ResultBean<>(courierService.queryOne(id));
    }

    /**
     * 查看多条--分页
     */
    @GetMapping("/list")
    public ResultBean<IPage<Courier>> query(Page<Courier> page, QueryCourierParam param) {
        IPage<Courier> data = courierService.queryPage(new Page<>(page.getCurrent(), page.getSize()), param);
        return new ResultBean<>(data);
    }


    /**
     * 导入
     * tpye，默认：0（0：百世，1：韵达）
     */
    @GetMapping("/import")
    public void importFile(MultipartFile file, int type) throws Exception {
        courierService.importFile(file, type);
    }


    /**
     * 查看快递重量存在误差数据--分页
     */
    @GetMapping("/list/error")
    public ResultBean<IPage<ErrorCourierData>> queryError(Page<Courier> page, QueryErrorCourierDataParam param) {
        IPage<ErrorCourierData> data = courierService.queryError(new Page<>(page.getCurrent(), page.getSize()), param);
        return new ResultBean<>(data);
    }


    /**
     * 导出快递重量存在误差数据--
     */
    @GetMapping("/export")
    public void export(HttpServletResponse res, QueryErrorCourierDataParam param) throws Exception {
        courierService.export(res, param);
    }


}
