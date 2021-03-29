package com.space.analysis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 快递实体类（来源快递公司）
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("courier")
public class Courier implements Serializable {

    private static final long serialVersionUID = -2417304522008102287L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 揽件时间（入网日期）
     */
    private Date sendDate;
    /**
     * 运单号
     */
    private String courierNumber;
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
     * 面单费用
     */
    private BigDecimal firstAmount;
    /**
     * 续重费用
     */
    private BigDecimal secondAmount;
    /**
     * 快递费（总）
     */
    private BigDecimal amount;
    /**
     * 数据来源，默认：0（0：百世，1：韵达）
     */
    private int sourceStatus;
}
