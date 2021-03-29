package com.space.analysis.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCourierParam {
    /**
     * 数据来源，默认：null,（0：百世，1：韵达）
     */
    private Integer sourceStatus;
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
}
