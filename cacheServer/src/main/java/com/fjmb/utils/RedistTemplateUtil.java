package com.fjmb.utils;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lxy
 * @Date 2020/7/7
 * @Descript
 **/
public class RedistTemplateUtil {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 从redis根据key值取map集合
     * @param key
     * @return
     */
    public Map<String,Object> getMapDataByKey(String key){
        Map<String,Object> redisMap = redisTemplate.opsForHash().entries(key);
        return  redisMap;
    }

    /**
     * 从redis根据key取List集合
     * @param key
     * @return
     */
    public List getListDataByKey(String key){
        List range = redisTemplate.opsForList().range(key, 0, -1);
        return range;
    }

    /**
     * 往redis中存map集合
     * @param key
     * @param map
     */
    public void pushMapDate(String key,Map<String,Object> map){
        redisTemplate.opsForHash().putAll(key,map);
    }


}
