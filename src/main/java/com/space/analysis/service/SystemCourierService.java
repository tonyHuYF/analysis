package com.space.analysis.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.space.analysis.domain.Courier;
import com.space.analysis.domain.SystemCourier;
import com.space.analysis.domain.param.QueryCourierParam;
import com.space.analysis.domain.param.QueryErrorCourierDataParam;
import com.space.analysis.domain.param.QueryLbCourierDataParam;
import com.space.analysis.domain.param.QuerySystemCourierParam;
import com.space.analysis.mapper.CourierMapper;
import com.space.analysis.mapper.SystemCourierMapper;
import com.space.analysis.model.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemCourierService {
    @Resource
    private SystemCourierMapper systemCourierMapper;
    @Resource
    private CourierMapper courierMapper;

    /**
     * 增加
     */
    public SystemCourier insert(SystemCourier systemCourier) {
        systemCourierMapper.insert(systemCourier);
        return systemCourier;
    }

    /**
     * 删除
     */
    public void delete(String id) {
        systemCourierMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public SystemCourier update(SystemCourier systemCourier) {
        SystemCourier systemCourierData = systemCourierMapper.selectById(systemCourier.getId());
        BeanUtil.copyProperties(systemCourier, systemCourierData);
        systemCourierMapper.updateById(systemCourierData);
        return systemCourierData;
    }

    /**
     * 查看单条
     */
    public SystemCourier queryOne(String id) {
        return systemCourierMapper.selectById(id);
    }

    /**
     * 查看多条
     */
    public List<SystemCourier> queryAll(QuerySystemCourierParam param) {
        QueryWrapper<SystemCourier> wrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getCourierNumber())) {
            wrapper.like("courier_number", param.getCourierNumber());
        }

        if (ObjectUtil.isNotEmpty(param.getOrderNumber())) {
            wrapper.like("order_number", param.getOrderNumber());
        }

        if (ObjectUtil.isNotEmpty(param.getOutboundDateStart())) {
            wrapper.ge("outbound_date", DateUtil.beginOfDay(param.getOutboundDateStart()));
        }

        if (ObjectUtil.isNotEmpty(param.getOutboundDateEnd())) {
            wrapper.le("outbound_date", DateUtil.endOfDay(param.getOutboundDateEnd()));
        }

        List<SystemCourier> systemCouriers = systemCourierMapper.selectList(wrapper);

        return systemCouriers;
    }

    /**
     * 查看多条--分页
     */
    public IPage<SystemCourier> queryPage(Page<SystemCourier> page, QuerySystemCourierParam param) {
        QueryWrapper<SystemCourier> wrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getCourierNumber())) {
            wrapper.like("courier_number", param.getCourierNumber());
        }

        if (ObjectUtil.isNotEmpty(param.getOrderNumber())) {
            wrapper.like("order_number", param.getOrderNumber());
        }

        if (ObjectUtil.isNotEmpty(param.getOutboundDateStart())) {
            wrapper.ge("outbound_date", DateUtil.beginOfDay(param.getOutboundDateStart()));
        }

        if (ObjectUtil.isNotEmpty(param.getOutboundDateEnd())) {
            wrapper.le("outbound_date", DateUtil.endOfDay(param.getOutboundDateEnd()));
        }

        IPage<SystemCourier> result = systemCourierMapper.selectPage(page, wrapper);

        return result;
    }

    /**
     * 导入
     */
    public void importFile(MultipartFile file, int type) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setTitleRows(0);

        List<T> data = importData(file, SystemCourierPojo.class, params);

        List<SystemCourier> systemCouriers = new ArrayList<>();

        for (Object item : data) {
            SystemCourier temp = new SystemCourier();
            BeanUtil.copyProperties(item, temp);
            //处理快递单号空格
            temp.setCourierNumber(StrUtil.trim(temp.getCourierNumber()));
            systemCouriers.add(temp);
        }

        //过滤运单号空值数据，过滤重复数据（即没快递重量的）
        List<SystemCourier> filterData = systemCouriers.stream()
                .filter(p -> ObjectUtil.isNotEmpty(p.getCourierNumber()))
                .filter(p -> ObjectUtil.isNotEmpty(p.getGoodsWeight()))
                .collect(Collectors.toList());

        //重量转换千克
        for (SystemCourier item : filterData) {
            if (ObjectUtil.isNotEmpty(item.getGoodsWeight())) {
                item.setGoodsWeight(item.getGoodsWeight().divide(new BigDecimal(1000)));
            }
        }

        //伦邦数据
        if (type != 0) {
            filterData.forEach(p -> p.setSourceStatus(1));
        }

        //插入数据
        batchInsert(filterData);

    }


    /**
     * 返回excel数据
     */
    public List<T> importData(MultipartFile file, Class<?> classzz, ImportParams params) throws Exception {
        if (ObjectUtil.isEmpty(file)) {
            return null;
        }

        List<T> result = ExcelImportUtil.importExcel(file.getInputStream(), classzz, params);

        return result;
    }

    /**
     * 批量插入
     */
    @Transactional
    public void batchInsert(List<SystemCourier> systemCouriers) {
        if (ObjectUtil.isNotEmpty(systemCouriers)) {
            //数据插入前，先删除已有数据
            List<SystemCourier> existSysCouriers = systemCourierMapper.selectList(new QueryWrapper<>());
            List<String> existNumbers = existSysCouriers.stream().map(SystemCourier::getCourierNumber).collect(Collectors.toList());

            //带删除的快递数据
            List<String> toDeleteNumbers = systemCouriers.stream()
                    .filter(p -> existNumbers.contains(p.getCourierNumber())).map(SystemCourier::getCourierNumber).collect(Collectors.toList());


            if (ObjectUtil.isNotEmpty(toDeleteNumbers)) {
                QueryWrapper<SystemCourier> wrapper = new QueryWrapper<>();
                wrapper.in("courier_number", toDeleteNumbers);
                systemCourierMapper.delete(wrapper);
            }


            int fromIndex = 0;
            while (true) {
                int toIndex = fromIndex + 100;
                if (toIndex > systemCouriers.size()) {
                    toIndex = systemCouriers.size();
                    systemCourierMapper.batchInsert(systemCouriers.subList(fromIndex, toIndex));
                    break;
                }

                systemCourierMapper.batchInsert(systemCouriers.subList(fromIndex, toIndex));
                fromIndex = toIndex;
            }
        }
    }


    /**
     * 查看伦邦快递费用
     */
    public List<LbCourierData> queryLbAll(QueryLbCourierDataParam param) {

        QueryWrapper<Courier> courierWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotEmpty(param.getCourierNumber())) {
            courierWrapper.like("courier_number", param.getCourierNumber());
        }


        if (ObjectUtil.isNotEmpty(param.getSendDateStart())) {
            courierWrapper.ge("send_date", DateUtil.beginOfDay(param.getSendDateStart()));
        }

        if (ObjectUtil.isNotEmpty(param.getSendDateEnd())) {
            courierWrapper.le("send_date", DateUtil.endOfDay(param.getSendDateEnd()));
        }

        List<Courier> couriers = courierMapper.selectList(courierWrapper);

        Map<String, Courier> courierMap = couriers.stream().collect(
                Collectors.toMap(k -> k.getCourierNumber(), v -> v));

        QueryWrapper<SystemCourier> systemCourierWrapper = new QueryWrapper<>();

        //过滤无需物流、广东顺丰数据
        String[] extraData = new String[]{"无需物流", "广东顺丰"};

        systemCourierWrapper.notIn("courier_name", Arrays.asList(extraData));

        systemCourierWrapper.eq("source_status", "1");

        List<SystemCourier> systemCouriers = systemCourierMapper.selectList(systemCourierWrapper);

        List<LbCourierData> result = new ArrayList<>();

        for (SystemCourier item : systemCouriers) {
            Courier courier = courierMap.get(item.getCourierNumber());
            if (ObjectUtil.isEmpty(courier)) {
                continue;
            }
            LbCourierData temp = new LbCourierData();
            temp.setCourierName(item.getCourierName());
            temp.setCourierNumber(item.getCourierNumber());
            temp.setSendDate(courier.getSendDate());
            temp.setGoodsName(item.getGoodsName());
            temp.setGoodsWeight(item.getGoodsWeight());
            temp.setWeight(courier.getWeight());
            temp.setAmount(courier.getAmount());

            result.add(temp);
        }

        result.sort(Comparator.comparing(LbCourierData::getSendDate));

        return result;
    }


    /**
     * 导出伦邦快递费用
     */
    public void export(HttpServletResponse res, QueryLbCourierDataParam param) throws Exception {
        List<LbCourierData> errorCourierData = queryLbAll(param);
        Map<String, Object> map = new HashMap<>();
        List<Map<String, String>> maplist = new ArrayList<>();

        //统计费用、重量总和
        BigDecimal allWeight = new BigDecimal(0);
        BigDecimal allAmount = new BigDecimal(0);
        BigDecimal allGoodsWeight = new BigDecimal(0);

        for (LbCourierData data : errorCourierData) {
            Map<String, String> temp = new HashMap<>();
            temp.put("courierName", data.getCourierName());
            temp.put("courierNumber", data.getCourierNumber());
            if (ObjectUtil.isNotEmpty(data.getSendDate())) {
                temp.put("sendDate", DateUtil.formatDate(data.getSendDate()));
            }
            temp.put("goodsName", data.getGoodsName());
            temp.put("goodsWeight", data.getGoodsWeight().toString());
            temp.put("weight", data.getWeight().toString());
            temp.put("amount", data.getAmount().toString());

            maplist.add(temp);

            allWeight = allWeight.add(data.getWeight());
            allAmount = allAmount.add(data.getAmount());
            allGoodsWeight = allGoodsWeight.add(data.getGoodsWeight());
        }

        map.put("maplist", maplist);
        map.put("allWeight", allWeight);
        map.put("allAmount", allAmount);
        map.put("allGoodsWeight", allGoodsWeight);

        TemplateExportParams params = new TemplateExportParams("template/template_lb.xls");

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        ServletOutputStream outputStream = res.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();

    }


}
