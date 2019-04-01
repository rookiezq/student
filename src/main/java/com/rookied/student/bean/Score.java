package com.rookied.student.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @desciption: 成绩类
 * @author: Demon
 * @version: 1.0 2019-03-28 18:33
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score implements Serializable {
    /**
     * 成绩id
     */
    private int sid;
    /**
     * 课程id
     */
    private int cid;
    /**
     * 学生学号
     */
    private String stuId;
    /**
     * 成绩
     */
    private String score;

    public Score(int cid, String stuId, String score) {
        this.cid = cid;
        this.stuId = stuId;
        this.score = score;
    }
}
