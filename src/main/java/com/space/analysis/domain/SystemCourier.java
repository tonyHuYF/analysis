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
 * 快递实体类(系统导出的)
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_courier")
public class SystemCourier implements Serializable {

    private static final long serialVersionUID = -2417304522008102287L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 出库单号
     */
    private String outboundNumber;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 网店单号
     */
    private String storeNumber;
    /**
     * 渠道商名称
     */
    private String supplierNumber;
    /**
     * 库位编号
     */
    private String storageNumber;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 货号
     */
    private String goodsNumber;
    /**
     * 商品数量
     */
    private BigDecimal goodsNum;
    /**
     * 快递单号（运单号）
     */
    private String courierNumber;
    /**
     * 快递公司
     */
    private String courierName;
    /**
     * 商品总重量(kg)
     */
    private BigDecimal goodsWeight;
    /**
     * 出库时间
     */
    private Date outboundDate;
    /**
     * 数据来源，默认：0（0：全部，1：伦邦）
     */
    private int sourceStatus = 0;

}
