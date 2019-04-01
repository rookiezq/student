package com.rookied.student.service;

import com.rookied.student.bean.Score;

import java.util.List;
import java.util.Map;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-29 20:05
 **/
public interface ScoreService extends BaseService<Score> {
    /**
     * 根据专业和学期查找所有科目成绩
     * @param maj 专业
     * @param term 学期
     * @return 返回对应科目的map
     */
    Map<String, List<String>> findScoreByTermMajor(String maj,int term);
}
