package com.space.analysis.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuerySystemCourierParam {
    /**
     * 数据来源，默认：null,（0：全部，1：伦邦）
     */
    private Integer sourceStatus;
    /**
     * 出库时间--开始
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date outboundDateStart;
    /**
     * 出库时间--结束
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date outboundDateEnd;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 运单号
     */
    private String courierNumber;
}
