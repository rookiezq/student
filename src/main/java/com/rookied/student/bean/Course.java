package com.rookied.student.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

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
    private int cId;
    /**
     * 课程名
     */
    private String cName;
    /**
     * 课程学期
     */
    private int cTerm;
}
