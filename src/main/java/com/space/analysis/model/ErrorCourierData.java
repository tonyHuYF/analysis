package com.space.analysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCourierData implements Serializable {

    private static final long serialVersionUID = -2417304522008102287L;
    /**
     * 公司
     */
    private String companyName;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 快递公司
     */
    private String courierName;
    /**
     * 运单号
     */
    private String courierNumber;
    /**
     * 揽件时间（入网日期）
     */
    private Date sendDate;
    /**
     * 目的省份
     */
    private String sendProvince;
    /**
     * 目的城市
     */
    private String sendCity;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 快递费（总）
     */
    private BigDecimal amount;
    /**
     * 快递重量（系统）
     */
    private BigDecimal goodsWeight;

}
