package com.space.analysis.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryLbCourierDataParam {
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
}
