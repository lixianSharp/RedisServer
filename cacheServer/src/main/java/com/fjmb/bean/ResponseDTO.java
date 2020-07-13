package com.fjmb.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lxy
 * @Date 2020/6/2
 * @Descript
 **/
@Data
public class ResponseDTO {
    private String resultMsg ;
    private Integer resultCode;
    private Map<String, Object> data;

    /**
     * 给 resultMsg、resultCode 默认初始化
     */
    public ResponseDTO() {
        this.resultMsg = ResultMsg.FAIL;
        this.resultCode = ResultCode.FAIL;
    }
}

