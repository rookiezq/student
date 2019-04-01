package com.rookied.student.service.impl;

import com.rookied.student.bean.Score;
import com.rookied.student.service.ScoreService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-29 20:06
 **/
@Service
public class ScoreServiceImpl implements ScoreService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Score> findAll() {
        return null;
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public List<Score> findById(Long id) {
        return null;
    }

    /**
     * 添加
     *
     * @param score
     */
    @Override
    public void create(Score score) {

    }

    /**
     * 删除（批量）
     *
     * @param ids
     */
    @Override
    public void delete(Long... ids) {

    }

    /**
     * 修改
     *
     * @param score
     */
    @Override
    public void update(Score score) {

    }

    /**
     * 将list放进redis
     *
     * @param list 获取的list
     */
    @Override
    public void pushInRedis(String key,List<Score> list) {

    }

    /**
     * 从redis中获取list
     *
     * @param key 存放的key值
     * @return 获取的list
     */
    @Override
    public List<Score> rangeFromRedis(String key) {
        return null;
    }

    /**
     * 根据专业和学期查找所有科目成绩
     *
     * @param maj  专业
     * @param term 学期
     * @return 返回对应科目的map
     */
    @Override
    public Map<String, List<String>> findScoreByTermMajor(String maj, int term) {
        String key = maj+term;
        Cursor<Map.Entry<Object, Object>> curosr = redisTemplate.opsForHash().scan("scoreMap", ScanOptions.NONE);
        Map<String,Map<String,List<String>>> map = new HashMap<>();
        while(curosr.hasNext()){
            Map.Entry<Object, Object> entry = curosr.next();
            map.put((String)entry.getKey(),(Map<String,List<String>>)entry.getValue());
        }
        return map.get(key);
    }
}
