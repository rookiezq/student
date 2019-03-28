package com.rookied.student;

import com.rookied.student.bean.Course;
import com.rookied.student.bean.Student;
import com.rookied.student.mapper.CourseMapper;
import com.rookied.student.mapper.StudentMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentApplicationTests {
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private CourseMapper courseMapper;

    @Test
    public void stuList() {
        List<Student> list = studentMapper.findAll();
        for (Student student : list) {
            System.out.println(student);
        }
    }
    @Test
    public void courseList() {
        List<Course> list = courseMapper.findAll();
        for (Course course : list) {
            System.out.println(course);
        }
    }

}
