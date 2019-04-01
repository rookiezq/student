package com.rookied.student.service;

import java.util.List;

/**
 * @desciption: 通用服务接口
 * @author: Demon
 * @version: 1.0 2019-03-29 19:55
 **/
public interface BaseService<T> {

    /**
     * 查询所有
     *
     * @return
     */
    List<T> findAll();

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    List<T> findById(Long id);

    /**
     * 添加
     *
     * @param t
     */
    void create(T t);

    /**
     * 删除（批量）
     *
     * @param ids
     */
    void delete(Long... ids);

    /**
     * 修改
     *
     * @param t
     */
    void update(T t);

    /**
     * 将list放进redis
     * @param list 获取的list
     */
    void pushInRedis(String key,List<T> list);

    /**
     * 从redis中获取list
     * @param key 存放的key值
     * @return 获取的list
     */
    List<T> rangeFromRedis(String key);
}
