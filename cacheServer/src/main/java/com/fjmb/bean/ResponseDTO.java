package com.fjmb.bean;

import lombok.Data;

import java.util.Map;

/**
 * @author lxy
 * @Date 2020/6/2
 * @Descript
 **/
@Data
public class ResponseDTO {
    private String resultMsg = "FAIL";
    private int resultCode = 0;
    private Map<String,Object> data;
    private Object object;
}

