package com.space.analysis.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 快递实体类(系统导出的)pojo
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemCourierPojo implements Serializable {

    private static final long serialVersionUID = -2417304522008102287L;
    /**
     * 线上订单号
     */
    @Excel(name = "线上订单号")
    private String orderNumber;
    /**
     * 店铺名称
     */
    @Excel(name = "店铺名称")
    private String shopName;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String goodName;
    /**
     * 发货日期
     */
    @Excel(name = "发货日期", importFormat = "yyyy/MM/dd")
    private Date sendDate;
    /**
     * 快递公司
     */
    @Excel(name = "快递公司")
    private String courierName;
    /**
     * 快递单号（运单号）
     */
    @Excel(name = "快递单号")
    private String courierNumber;
    /**
     * 商品总重量(kg)
     */
    @Excel(name = "订单商品重量")
    private BigDecimal goodsWeight;
    /**
     * 数据来源，默认：0（0：空间汇，1：伦邦）
     */
    private int sourceStatus = 0;


    private void setSourceStatus() {
        if ("伦邦旗舰店".equals(this.shopName)) {
            this.sourceStatus = 1;
        } else {
            sourceStatus = 0;
        }
    }

}
