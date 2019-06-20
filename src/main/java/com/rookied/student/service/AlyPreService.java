package com.rookied.student.service;

import org.springframework.stereotype.Service;

/**
 * @desciption: 分析和预测
 * @author: Demon
 * @version: 1.0 2019-04-21 17:00
 **/
public interface AlyPreService {
    /**
     * 分析
     * @param maj 专业
     * @return
     */
    String analyse(String maj);

    /**
     * 预测
     * @param maj 专业
     * @param data 成绩
     * @return
     */
    String predict(String maj,String data);
}
