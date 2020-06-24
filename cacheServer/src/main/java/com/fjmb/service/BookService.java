package com.fjmb.service;

import com.fjmb.bean.Book;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author lxy
 * @Date 2020/6/2
 * @Descript
 **/
public interface BookService {

    abstract  int deleteByPrimaryKey(Integer bid);

    //表示使用my-redis-cache1缓存空间，key的生成策略为book+bid，当bid<10的时候才会使用缓存
    @Cacheable(value = "my-redis-cache1",key = "'id_#_'+#bid",condition = "#bid<30")
    Book selectByPrimaryKey(Integer bid);

}
