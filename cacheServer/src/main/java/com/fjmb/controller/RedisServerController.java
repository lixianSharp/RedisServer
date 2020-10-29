package com.fjmb.controller;

import com.fjmb.bean.ResponseDTO;
import com.fjmb.bean.ResultCode;
import com.fjmb.bean.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author lxy
 * @Date 2020/7/7
 * @Descript
 **/
@RestController
public class RedisServerController {
    private static final Logger logger = Logger.getLogger("RedisServiceController");

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 保存数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/pushDataToRedis")
    public ResponseDTO pushDataToRedis(@RequestBody Map<String, Object> map) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(map.size()<=0){
            responseDTO.setResultCode(ResultCode.PARAM_ERROR);
            responseDTO.setResultMsg(ResultMsg.PARAM_ERROR);
            return responseDTO;
        }

        String key = String.valueOf(map.get("key"));
        Map<String,Object> entries = redisTemplate.opsForHash().entries(key);
        if(entries.size()>0){
            //如果原先存在该key对应的数据，则先将原先的数据删除，然后再添加
            Boolean delete = redisTemplate.delete(key);
           logger.info("key="+key+"在Redis中已存在，删除原先的key，再重新添加该key-value");
        }
        try {
            map.remove("key");//去除map中多余的名字为 key的 key-value
            redisTemplate.opsForHash().putAll(key,map);
            responseDTO.setResultCode(ResultCode.SUCCESS);
            responseDTO.setResultMsg(ResultMsg.SUCCESS);
        } catch (Exception e) {
            responseDTO.setResultCode(ResultCode.FAIL);
            responseDTO.setResultMsg(ResultMsg.FAIL);
            e.printStackTrace();
        }

        return responseDTO;
    }


    /**
     * 根据指定key获取数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/pullDataToRedis")
    public ResponseDTO pullDataToRedis(@RequestBody Map<String, Object> map) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(map.size()<=0){
            responseDTO.setResultCode(ResultCode.PARAM_ERROR);
            responseDTO.setResultMsg(ResultMsg.PARAM_ERROR);
            return responseDTO;
        }

        String key = String.valueOf(map.get("key"));
        Map<String,Object> entries = redisTemplate.opsForHash().entries(key);
        if(entries.size()>0){
            //表示有数据
            responseDTO.setData(entries);
            responseDTO.setResultCode(ResultCode.SUCCESS);
            responseDTO.setResultMsg(ResultMsg.SUCCESS);
            logger.info("key="+key+",value="+entries);
        }else{
            logger.info("key="+key+",在Redis中无该key");
            responseDTO.setResultCode(ResultCode.KEY_NOT_EXIST);
            responseDTO.setResultMsg(ResultMsg.KEY_NOT_EXIST);
        }

        return responseDTO;
    }

    /**
     * 根据key进行模糊查询
     * @param map   key：代表要模糊查询的key
     * @return
     */
    @RequestMapping(value = "/pullDataByKeyVague")
    public ResponseDTO pullDataByKeyVague(@RequestBody Map<String, Object> map) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(map.size()<=0){
            responseDTO.setResultCode(ResultCode.PARAM_ERROR);
            responseDTO.setResultMsg(ResultMsg.PARAM_ERROR);
            return responseDTO;
        }

        String key = String.valueOf(map.get("key"));
        //模糊匹配查找出所有符合的key
        Set<String> keys = redisTemplate.keys(key);
        logger.info("获取到的所有的key="+keys);
        if(keys.size()<=0){
            //如果没有找到对应的key
            responseDTO.setResultMsg(ResultMsg.KEY_NOT_EXIST);
            responseDTO.setResultCode(ResultCode.KEY_NOT_EXIST);
            return responseDTO;
        }

        //找到对应的key，则将其所有的key对应的value都放到一个resultMap集合中
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        for (String targetKey : keys) {
            Map<String, Object> entries = redisTemplate.opsForHash().entries(targetKey);

            resultMap.put(targetKey,entries);
        }
        responseDTO.setData(resultMap);
        responseDTO.setResultCode(ResultCode.SUCCESS);
        responseDTO.setResultMsg(ResultMsg.SUCCESS);
        return responseDTO;
    }


    /**
     * 删除数据,根据具体的key删除
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteKey")
    public ResponseDTO deleteKey(@RequestBody Map<String, Object> map) {
        ResponseDTO responseDTO = new ResponseDTO();

        if(map.size()<=0){
            responseDTO.setResultCode(ResultCode.PARAM_ERROR);
            responseDTO.setResultMsg(ResultMsg.PARAM_ERROR);
            return responseDTO;
        }

        String key = String.valueOf(map.get("key"));
        logger.info("要删除的key="+key);
        Boolean delete = redisTemplate.delete(key);
        if(delete){
            responseDTO.setResultCode(ResultCode.SUCCESS);
            responseDTO.setResultMsg(ResultMsg.SUCCESS);
        }else{
            responseDTO.setResultMsg(ResultMsg.DELETE_KEY_FAIL);
            responseDTO.setResultCode(ResultCode.DELETE_KEY_FAIL);
        }

        return responseDTO;
    }

    /**
     * 删除数据,根据模糊匹配key进行删除，例如 age_balance_report_*
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteByPatternKey")
    public ResponseDTO deleteByPatternKey(@RequestBody Map<String, Object> map) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(map.size()<=0){
            responseDTO.setResultCode(ResultCode.PARAM_ERROR);
            responseDTO.setResultMsg(ResultMsg.PARAM_ERROR);
            return responseDTO;
        }

        String key = String.valueOf(map.get("key"));
        //模糊匹配查找出所有符合的key
        Set<String> keys = redisTemplate.keys(key);
        logger.info("要删除的所有keys="+keys);
        //一次性删除一个key集合
        Long delRes = redisTemplate.delete(keys);

        if(delRes>0){
            responseDTO.setResultCode(ResultCode.SUCCESS);
            responseDTO.setResultMsg(ResultMsg.SUCCESS);
        }else{
            responseDTO.setResultMsg(ResultMsg.DELETE_KEY_FAIL);
            responseDTO.setResultCode(ResultCode.DELETE_KEY_FAIL);
        }
        return responseDTO;
    }


}
