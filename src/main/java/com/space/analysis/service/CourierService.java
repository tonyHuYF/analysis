package com.space.analysis.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.space.analysis.domain.Courier;
import com.space.analysis.domain.ResultBean;
import com.space.analysis.domain.SystemCourier;
import com.space.analysis.domain.param.QueryErrorCourierDataParam;
import com.space.analysis.domain.param.QuerySystemCourierParam;
import com.space.analysis.mapper.CourierMapper;
import com.space.analysis.domain.param.QueryCourierParam;
import com.space.analysis.mapper.SystemCourierMapper;
import com.space.analysis.model.BsCourier;
import com.space.analysis.model.ErrorCourierData;
import com.space.analysis.model.YdCourier;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourierService {
    @Resource
    private CourierMapper courierMapper;
    @Resource
    private SystemCourierMapper systemCourierMapper;

    /**
     * ??????
     */
    public Courier insert(Courier courier) {
        courierMapper.insert(courier);
        return courier;
    }

    /**
     * ??????
     */
    public void delete(String id) {
        courierMapper.deleteById(id);
    }

    /**
     * ??????
     */
    public Courier update(Courier courier) {
        Courier courierData = courierMapper.selectById(courier.getId());
        BeanUtil.copyProperties(courier, courierData);
        courierMapper.updateById(courierData);
        return courierData;
    }

    /**
     * ????????????
     */
    public Courier queryOne(String id) {
        return courierMapper.selectById(id);
    }

    /**
     * ????????????
     */
    public List<Courier> queryAll(QueryCourierParam param) {
        QueryWrapper<Courier> wrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getCourierNumber())) {
            wrapper.like("courier_number", param.getCourierNumber());
        }

        if (ObjectUtil.isNotEmpty(param.getSendProvince())) {
            wrapper.like("send_province", param.getSendProvince());
        }

        if (ObjectUtil.isNotEmpty(param.getSendCity())) {
            wrapper.like("send_city", param.getSendCity());
        }

        if (ObjectUtil.isNotEmpty(param.getSourceStatus())) {
            wrapper.eq("source_status", param.getSourceStatus());
        }

        if (ObjectUtil.isNotEmpty(param.getSendDateStart())) {
            wrapper.ge("send_date", DateUtil.beginOfDay(param.getSendDateStart()));
        }

        if (ObjectUtil.isNotEmpty(param.getSendDateEnd())) {
            wrapper.le("send_date", DateUtil.endOfDay(param.getSendDateEnd()));
        }

        List<Courier> couriers = courierMapper.selectList(wrapper);

        return couriers;
    }

    /**
     * ????????????--??????
     */
    public IPage<Courier> queryPage(Page<Courier> page, QueryCourierParam param) {
        QueryWrapper<Courier> wrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(param.getCourierNumber())) {
            wrapper.like("courier_number", param.getCourierNumber());
        }

        if (ObjectUtil.isNotEmpty(param.getSendProvince())) {
            wrapper.like("send_province", param.getSendProvince());
        }

        if (ObjectUtil.isNotEmpty(param.getSendCity())) {
            wrapper.like("send_city", param.getSendCity());
        }

        if (ObjectUtil.isNotEmpty(param.getSourceStatus())) {
            wrapper.eq("source_status", param.getSourceStatus());
        }

        if (ObjectUtil.isNotEmpty(param.getSendDateStart())) {
            wrapper.ge("send_date", DateUtil.beginOfDay(param.getSendDateStart()));
        }

        if (ObjectUtil.isNotEmpty(param.getSendDateEnd())) {
            wrapper.le("send_date", DateUtil.endOfDay(param.getSendDateEnd()));
        }

        IPage<Courier> result = courierMapper.selectPage(page, wrapper);

        return result;
    }

    /**
     * ??????
     */
    public void importFile(MultipartFile file, int type) throws Exception {
        List<T> data;
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setTitleRows(0);
        params.setKeyIndex(1);

        if (type == 0) {
            //??????
            data = importData(file, BsCourier.class, params);
        } else {
            //??????
            data = importData(file, YdCourier.class, params);
        }

        List<Courier> couriers = new ArrayList<>();

        for (Object item : data) {
            Courier temp = new Courier();
            BeanUtil.copyProperties(item, temp);

            //??????????????????????????????????????????
            if (temp.getSourceStatus() == 0 && ObjectUtil.isNotEmpty(temp.getSendProvince())) {
                String[] splits = temp.getSendProvince().split("\\|");
                if (splits.length > 1) {
                    temp.setSendProvince(splits[0]);
                    temp.setSendCity(splits[1]);
                }
            }

            //????????????????????????
            temp.setCourierNumber(StrUtil.trim(temp.getCourierNumber()));

            //???????????????????????????????????????????????????
            if (type == 1) {
                temp.setAmount(temp.getFirstAmount().add(temp.getSecondAmount()));
            }

            couriers.add(temp);
        }

        //???????????????????????????
        List<Courier> filterData = couriers.stream().filter(p -> ObjectUtil.isNotEmpty(p.getCourierNumber())).collect(Collectors.toList());


        //????????????
        batchInsert(filterData);

    }


    /**
     * ??????excel??????
     */
    public List<T> importData(MultipartFile file, Class<?> classzz, ImportParams params) throws Exception {
        if (ObjectUtil.isEmpty(file)) {
            return null;
        }

        List<T> result = ExcelImportUtil.importExcel(file.getInputStream(), classzz, params);

        return result;
    }

    /**
     * ????????????
     */
    @Transactional
    public void batchInsert(List<Courier> couriers) {
        if (ObjectUtil.isNotEmpty(couriers)) {
            //???????????????????????????????????????
            List<Courier> existCouriers = courierMapper.selectList(new QueryWrapper<>());
            List<String> existNumbers = existCouriers.stream().map(Courier::getCourierNumber).collect(Collectors.toList());

            //????????????????????????
            List<String> toDeleteNumbers = couriers.stream()
                    .filter(p -> existNumbers.contains(p.getCourierNumber())).map(Courier::getCourierNumber).collect(Collectors.toList());

            if (ObjectUtil.isNotEmpty(toDeleteNumbers)) {
                QueryWrapper<Courier> wrapper = new QueryWrapper<>();
                wrapper.in("courier_number", toDeleteNumbers);
                courierMapper.delete(wrapper);
            }


            int fromIndex = 0;
            while (true) {
                int toIndex = fromIndex + 100;
                if (toIndex > couriers.size()) {
                    toIndex = couriers.size();
                    courierMapper.batchInsert(couriers.subList(fromIndex, toIndex));
                    break;
                }

                courierMapper.batchInsert(couriers.subList(fromIndex, toIndex));
                fromIndex = toIndex;
            }
        }
    }

    /**
     * ????????????????????????????????????--??????
     */
    public IPage<ErrorCourierData> queryError(Page<ErrorCourierData> page, QueryErrorCourierDataParam param) {
        List<ErrorCourierData> list = queryErrorAll(param);
        int current = (int) page.getCurrent();
        int size = (int) page.getSize();
        int total = list.size();

        int fromIndex = (current - 1) * size;
        int toIndex = total < size * current ? total : size * current;
        List<ErrorCourierData> errorCourierData = list.subList(fromIndex, toIndex);

        page.setCurrent(current);
        page.setSize(size);
        page.setTotal(total);
        page.setRecords(errorCourierData);

        return page;
    }


    /**
     * ????????????????????????????????????
     */
    public List<ErrorCourierData> queryErrorAll(QueryErrorCourierDataParam param) {

        QueryWrapper<Courier> courierWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotEmpty(param.getCourierNumber())) {
            courierWrapper.like("courier_number", param.getCourierNumber());
        }

        if (ObjectUtil.isNotEmpty(param.getCourierSourceStatus())) {
            courierWrapper.eq("source_status", param.getCourierSourceStatus());
        }

        if (ObjectUtil.isNotEmpty(param.getSendDateStart())) {
            courierWrapper.ge("send_date", DateUtil.beginOfDay(param.getSendDateStart()));
        }

        if (ObjectUtil.isNotEmpty(param.getSendDateEnd())) {
            courierWrapper.le("send_date", DateUtil.endOfDay(param.getSendDateEnd()));
        }

        List<Courier> couriers = courierMapper.selectList(courierWrapper);

        QueryWrapper<SystemCourier> systemCourierWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotEmpty(param.getOrderNumber())) {
            systemCourierWrapper.like("order_number", param.getOrderNumber());
        }

        if (ObjectUtil.isNotEmpty(param.getSystemCourierSourceStatus())) {
            systemCourierWrapper.eq("source_status", param.getSystemCourierSourceStatus());
        }

        //???????????????????????????????????????
        String[] extraData = new String[]{"????????????", "????????????"};

        systemCourierWrapper.notIn("courier_name", Arrays.asList(extraData));


        List<SystemCourier> systemCouriers = systemCourierMapper.selectList(systemCourierWrapper);

        //??????????????????????????????
        List<SystemCourier> allSystemCourier = systemCouriers.stream()
                .filter(p -> p.getGoodsWeight().compareTo(new BigDecimal(0)) > 0)
                .collect(Collectors.toList());

        Map<String, SystemCourier> systemCourierMap = allSystemCourier.stream().collect(
                Collectors.toMap(k -> k.getCourierNumber(), v -> v));


        List<ErrorCourierData> result = new ArrayList<>();

        for (Courier item : couriers) {
            SystemCourier systemCourier = systemCourierMap.get(item.getCourierNumber());
            if (ObjectUtil.isNotEmpty(systemCourier)) {
                //?????????????????????????????????????????????
                if (item.getWeight().compareTo(systemCourier.getGoodsWeight()) == 1) {
                    //??????????????????????????????????????????
                    BigDecimal courierWeight = item.getWeight().setScale(0, BigDecimal.ROUND_UP);
                    BigDecimal systemWeight = systemCourier.getGoodsWeight().setScale(0, BigDecimal.ROUND_UP);
                    if (courierWeight.compareTo(systemWeight) == 1) {
                        ErrorCourierData temp = new ErrorCourierData();
                        String companyName = "";
                        if (systemCourier.getSourceStatus() == 1) {
                            companyName = "???????????????";
                        } else {
                            companyName = "??????????????????";
                        }
                        temp.setCompanyName(companyName);
                        temp.setOrderNumber(systemCourier.getOrderNumber());
                        temp.setCourierName(systemCourier.getCourierName());
                        temp.setCourierNumber(item.getCourierNumber());
                        temp.setSendDate(item.getSendDate());
                        temp.setSendProvince(item.getSendProvince());
                        temp.setSendCity(item.getSendCity());
                        temp.setWeight(item.getWeight());
                        temp.setAmount(item.getAmount());
                        temp.setGoodsWeight(systemCourier.getGoodsWeight());

                        result.add(temp);
                    }
                }
            }
        }

        result.sort(Comparator.comparing(ErrorCourierData::getSendDate));

        return result;
    }


    /**
     * ????????????????????????????????????
     */
    public void export(HttpServletResponse res, QueryErrorCourierDataParam param) throws Exception {
        List<ErrorCourierData> errorCourierData = queryErrorAll(param);
        Map<String, Object> map = new HashMap<>();
        List<Map<String, String>> maplist = new ArrayList<>();

        //???????????????????????????
        BigDecimal allWeight = new BigDecimal(0);
        BigDecimal allAmount = new BigDecimal(0);
        BigDecimal allGoodsWeight = new BigDecimal(0);

        for (ErrorCourierData data : errorCourierData) {
            Map<String, String> temp = new HashMap<>();
            temp.put("companyName", data.getCompanyName());
            temp.put("orderNumber", data.getOrderNumber());
            temp.put("courierName", data.getCourierName());
            temp.put("courierNumber", data.getCourierNumber());

            if (ObjectUtil.isNotEmpty(data.getSendDate())) {
                temp.put("sendDate", DateUtil.formatDate(data.getSendDate()));
            }

            temp.put("sendProvince", data.getSendProvince());
            temp.put("sendCity", data.getSendCity());
            temp.put("weight", data.getWeight().toString());
            temp.put("amount", data.getAmount().toString());
            temp.put("goodsWeight", data.getGoodsWeight().toString());
            maplist.add(temp);

            allWeight = allWeight.add(data.getWeight());
            allAmount = allAmount.add(data.getAmount());
            allGoodsWeight = allGoodsWeight.add(data.getGoodsWeight());
        }


        map.put("maplist", maplist);
        map.put("allWeight", allWeight);
        map.put("allAmount", allAmount);
        map.put("allGoodsWeight", allGoodsWeight);

        TemplateExportParams params = new TemplateExportParams("template/template_analysis.xls");


        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        ServletOutputStream outputStream = res.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();

    }


}
