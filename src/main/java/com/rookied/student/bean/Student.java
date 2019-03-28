package com.rookied.student.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @desciption: 学生类
 * @author: Demon
 * @version: 1.0 2019-03-28 18:32
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {
    /**
     * 学生学号
     */
    private String stuId;
    /**
     * 学生性别
     */
    private String stuSex;
    /**
     * 学生专业
     */
    private String stuMaj;

}
