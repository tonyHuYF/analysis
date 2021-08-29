package com.space.analysis.model;

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
 * 快递实体类(系统导出的)pojo
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemCourierPojoOld implements Serializable {

    private static final long serialVersionUID = -2417304522008102287L;
    /**
     * 出库单号
     */
    @Excel(name = "出库单号")
    private String outboundNumber;
    /**
     * 订单号
     */
    @Excel(name = "订单号")
    private String orderNumber;
    /**
     * 网店单号
     */
    @Excel(name = "网店单号")
    private String storeNumber;
    /**
     * 渠道商名称
     */
    @Excel(name = "渠道商名称")
    private String supplierNumber;
    /**
     * 库位编号
     */
    @Excel(name = "库位编号")
    private String storageNumber;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String goodsName;
    /**
     * 货号
     */
    @Excel(name = "货号")
    private String goodsNumber;
    /**
     * 商品数量
     */
    @Excel(name = "商品数量")
    private BigDecimal goodsNum;
    /**
     * 快递单号（运单号）
     */
    @Excel(name = "快递单号")
    private String courierNumber;
    /**
     * 快递公司
     */
    @Excel(name = "快递公司")
    private String courierName;
    /**
     * 商品总重量(kg)
     */
    @Excel(name = "商品总重量(克)")
    private BigDecimal goodsWeight;
    /**
     * 出库时间
     */
    @Excel(name = "出库时间", importFormat = "yyyy-MM-dd")
    private Date outboundDate;
    /**
     * 数据来源，默认：0（0：全部，1：伦邦）
     */
    private int sourceStatus = 0;

}
