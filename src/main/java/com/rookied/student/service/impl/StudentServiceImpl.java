package com.rookied.student.service.impl;

import com.rookied.student.bean.Student;
import com.rookied.student.mapper.StudentMapper;
import com.rookied.student.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-30 10:45
 **/
@Service
public class StudentServiceImpl implements StudentService {
    private static final String MAJOR_CS = "计算机科学与技术";
    private static final String MAJOR_AC = "会计";

    @Resource
    private StudentMapper studentMapper;

    /**
     * 通过学号查找所有成绩
     *
     * @param stuId  学号
     * @param cterm  学期
     * @param stuMaj 专业
     * @return 成绩列表
     */
    @Override
    public List<Student> findAllScore(String stuId, int cterm, String stuMaj) {
        //如果学号为空
        boolean flag = "".equalsIgnoreCase(stuId) || stuId == null;
        //按专业查询 分别赋给一个默认学号
        if (flag && MAJOR_CS.equalsIgnoreCase(stuMaj)){
            stuId = "20141308001";
        }
        if (flag && MAJOR_AC.equalsIgnoreCase(stuMaj)){
            stuId = "20141312001";
        }
        return studentMapper.findAllScore(stuId,cterm,stuMaj);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Student> findAll() {
        return null;
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public List<Student> findById(Long id) {
        return null;
    }

    /**
     * 添加
     *
     * @param student
     */
    @Override
    public void create(Student student) {

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
     * @param student
     */
    @Override
    public void update(Student student) {

    }
}
