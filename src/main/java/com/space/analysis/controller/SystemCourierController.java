package com.space.analysis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.space.analysis.domain.Courier;
import com.space.analysis.domain.ResultBean;
import com.space.analysis.domain.SystemCourier;
import com.space.analysis.domain.param.QueryCourierParam;
import com.space.analysis.domain.param.QueryErrorCourierDataParam;
import com.space.analysis.domain.param.QueryLbCourierDataParam;
import com.space.analysis.domain.param.QuerySystemCourierParam;
import com.space.analysis.service.CourierService;
import com.space.analysis.service.SystemCourierService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 快递数据
 */
@RestController
@RequestMapping("/systemCourier")
public class SystemCourierController {
    @Resource
    private SystemCourierService systemCourierService;

    /**
     * 增加
     */
    @PostMapping("/add")
    public ResultBean<SystemCourier> insert(SystemCourier systemCourier) {
        return new ResultBean<>(systemCourierService.insert(systemCourier));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResultBean delete(String id) {
        systemCourierService.delete(id);
        return new ResultBean<>();
    }

    /**
     * 修改
     */
    @PutMapping
    public ResultBean<SystemCourier> update(SystemCourier systemCourier) {
        return new ResultBean<>(systemCourierService.update(systemCourier));
    }

    /**
     * 查看单条
     */
    @GetMapping("/{id}")
    public ResultBean<SystemCourier> queryOne(String id) {
        return new ResultBean<>(systemCourierService.queryOne(id));
    }

    /**
     * 查看多条--分页
     */
    @GetMapping("/list")
    public ResultBean<IPage<SystemCourier>> query(Page<SystemCourier> page, QuerySystemCourierParam param) {
        IPage<SystemCourier> data = systemCourierService.queryPage(new Page<>(page.getCurrent(), page.getSize()), param);
        return new ResultBean<>(data);
    }

    /**
     * 查看多条
     */
    @GetMapping("/list/all")
    public ResultBean<List<SystemCourier>> queryAll(QuerySystemCourierParam param) {
        return new ResultBean<>(systemCourierService.queryAll(param));
    }

    /**
     * 导入
     * tpye，默认：0（0：全部，1：伦邦）
     */
    @GetMapping("/import")
    public void importFile(MultipartFile file) throws Exception {
        systemCourierService.importFile(file);
    }

    /**
     * 导出伦邦快递费用
     */
    @GetMapping("/export")
    public void export(HttpServletResponse res, QueryLbCourierDataParam param) throws Exception {
        systemCourierService.export(res, param);
    }

}
