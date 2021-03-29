package com.space.analysis.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryErrorCourierDataParam {
    /**
     * 数据来源，默认：null,（0：全部，1：伦邦）
     */
    private Integer systemCourierSourceStatus;
    /**
     * 数据来源，默认：null,（0：百世，1：韵达）
     */
    private Integer courierSourceStatus;
    /**
     * 揽件时间--开始（入网日期）
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sendDateStart;
    /**
     * 揽件时间--结束（入网日期）
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sendDateEnd;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 运单号
     */
    private String courierNumber;
}
