package com.fjmb.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lxy
 * @Date 2020/6/2
 * @Descript
 **/
@Data
public class Book implements Serializable {
    private Integer bid;
    private String name;
    private Float price;
}
