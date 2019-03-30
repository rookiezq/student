package com.rookied.student.mapper;

import com.rookied.student.bean.PageBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-29 22:37
 **/
@Mapper
public interface PageBeanMapper {
    /**
     * 查找所有成绩
     *
     * @return 成绩列表
     */
    List<PageBean> findAll();
}
