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
public class LbCourierData implements Serializable {

    private static final long serialVersionUID = -2417304522008102287L;
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
     * 商品名称
     */
    private String goodsName;
    /**
     * 快递重量（系统）
     */
    private BigDecimal goodsWeight;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 快递费（总）
     */
    private BigDecimal amount;

}
