package com.rookied.student.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @desciption: 成绩多表查询
 * @author: Demon
 * @version: 1.0 2019-03-29 22:21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean implements Serializable {
    private String stuId;
    private String stuSex;
    private String cname;
    private String score;
    private int cterm;
    private String stuMaj;
    private int sid;
}
