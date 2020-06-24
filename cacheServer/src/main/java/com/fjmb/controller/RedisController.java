package com.fjmb.controller;

import com.fjmb.bean.Book;
import com.fjmb.bean.RedisConstants;
import com.fjmb.bean.ResponseDTO;
import com.fjmb.utils.RedisUtil;
import com.fjmb.utils.RedisUtil2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lxy
 * @Date 2020/6/2
 * @Descript
 **/
@RestController
public class RedisController {


    @Autowired
    RedisUtil2 redisUtil2;
    @RequestMapping(value = "/getRedis",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO getRedis(){
        ResponseDTO responseDTO = new ResponseDTO();
        Set<Book> setBook = new HashSet<Book>();
        for(int i=1;i<=500;i++){
            Book book = new Book();
            book.setBid(i);
            book.setName("百年孤独"+i);
            book.setPrice(123.8F);
            setBook.add(book);
        }

//        redisUtil2.set("500",setBook);
        Object o = redisUtil2.get("500");
        responseDTO.setObject(o);
        responseDTO.setResultCode(1);
        responseDTO.setResultMsg("SUCCESS");
        return responseDTO;
    }

}
