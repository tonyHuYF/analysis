package com.space.analysis.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
     * 线上订单号
     */
    private String orderNumber;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 商品名称
     */
    private String goodName;
    /**
     * 发货日期
     */
    private Date sendDate;
    /**
     * 快递公司
     */
    private String courierName;
    /**
     * 快递单号（运单号）
     */
    private String courierNumber;
    /**
     * 商品总重量(kg)
     */
    private BigDecimal goodsWeight;
    /**
     * 数据来源，默认：0（0：空间汇，1：伦邦）
     */
    private int sourceStatus = 0;

}
