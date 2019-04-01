package com.rookied.student.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @desciption: 课程类
 * @author: Demon
 * @version: 1.0 2019-03-28 18:33
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    /**
     * 课程id
     */
    private int cid;
    /**
     * 课程名
     */
    private String cname;
    /**
     * 课程学期
     */
    private int cterm;
    /**
     * 课程所属专业
     */
    private String cmaj;
    /**
     * 某门课所有的成绩
     */
    private List<Score> cscores;

    public Course(int cid, String cname, int cterm, String cmaj) {
        this.cid = cid;
        this.cname = cname;
        this.cterm = cterm;
        this.cmaj = cmaj;
    }
}
