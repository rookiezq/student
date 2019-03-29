package com.rookied.student.mapper;

import com.rookied.student.bean.Score;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-28 19:01
 **/
@Mapper
public interface ScoreMapper {
    /**
     * 查找所有成绩
     *
     * @return 成绩列表
     */
    List<Score> findAll();

    /**
     * 通过学号查找
     *
     * @param id 学号
     * @return 成绩列表
     */
    List<Score> findById(int id);

    /**
     * 创建一个成绩信息
     *
     * @param score 成绩
     */
    void create(Score score);

    /**
     * 删除一个成绩信息
     *
     * @param id 学号
     */
    void delete(int id);

    /**
     * 更新一个成绩信息
     *
     * @param user 成绩类
     */
    void update(Score user);

}
