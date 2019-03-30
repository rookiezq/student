package com.rookied.student.service.impl;

import com.rookied.student.bean.Score;
import com.rookied.student.service.ScoreService;

import java.util.List;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-29 20:06
 **/
public class ScoreServiceImpl implements ScoreService {
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
}
