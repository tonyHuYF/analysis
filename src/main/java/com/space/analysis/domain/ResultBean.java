package com.space.analysis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultBean<T> implements Serializable {
    public static final int SUCCESS = 0;
    public static final int FAIL = -1;

    private String msg;
    private int code;
    private T data;

    public ResultBean(T data) {
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = FAIL;
    }

}
