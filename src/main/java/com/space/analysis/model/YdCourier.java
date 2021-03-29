package com.space.analysis.model;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 韵达快递pojo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YdCourier implements Serializable {

    private static final long serialVersionUID = -2417304522008102287L;

    /**
     * 客户名称
     */
    @Excel(name = "条码分配客户")
    private String customerName;
    /**
     * 揽件时间（入网日期）
     */
    @Excel(name = "揽件时间", importFormat = "yyyy-MM-dd")
    private Date sendDate;
    /**
     * 运单号
     */
    @Excel(name = "运单号")
    private String courierNumber;
    /**
     * 目的省份
     */
    @Excel(name = "目的省份")
    private String sendProvince;
    /**
     * 目的城市
     */
    @Excel(name = "目的城市")
    private String sendCity;
    /**
     * 重量
     */
    @Excel(name = "揽件重量")
    private BigDecimal weight;
    /**
     * 面单费用
     */
    @Excel(name = "面单")
    private BigDecimal firstAmount;
    /**
     * 续重费用
     */
    @Excel(name = "运费")
    private BigDecimal secondAmount;

    /**
     * 数据来源，默认：0（0：百世，1：韵达）
     */
    private int sourceStatus = 1;
}
