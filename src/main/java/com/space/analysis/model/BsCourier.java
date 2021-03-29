package com.space.analysis.model;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 百世快递pojo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BsCourier implements Serializable {

    private static final long serialVersionUID = -2417304522008102287L;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String customerName;
    /**
     * 揽件时间（入网日期）
     */
    @Excel(name = "入网日期", importFormat = "yyyy-MM-dd")
    private Date sendDate;
    /**
     * 运单号
     */
    @Excel(name = "运单号")
    private String courierNumber;
    /**
     * 目的省份
     */
    @Excel(name = "电子面单打印")
    private String sendProvince;
    /**
     * 重量
     */
    @Excel(name = "一级站点称重")
    private BigDecimal weight;
    /**
     * 快递费（总）
     */
    @Excel(name = "快递费")
    private BigDecimal amount;
    /**
     * 数据来源，默认：0（0：百世，1：韵达）
     */
    private int sourceStatus = 0;
}
