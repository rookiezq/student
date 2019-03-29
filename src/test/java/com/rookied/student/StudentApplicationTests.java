package com.rookied.student;

import com.rookied.student.bean.Course;
import com.rookied.student.bean.Score;
import com.rookied.student.bean.Student;
import com.rookied.student.mapper.CourseMapper;
import com.rookied.student.mapper.ScoreMapper;
import com.rookied.student.mapper.StudentMapper;
import com.rookied.student.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentApplicationTests {
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private ScoreMapper scoreMapper;

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

    @Test
    public void stuInsert() {
        Student student = new Student("123456","女","会计");
        studentMapper.create(student);
    }

    /**
     * 插入所有课程
     * @throws IOException
     */
    @Test
    //@Transactional
    @SuppressWarnings("all")
    public void courseInsert() throws IOException {
        Map<String,Object> map = IOUtils.getAllCoursesAndScore();
        List<Course> list = (List<Course>) map.get("coureList");
        for (Course course : list) {
            courseMapper.create(course);
        }
    }

    /**
     * 插入所有成绩
     * @throws IOException
     */
    @SuppressWarnings("all")
    @Test
    public void scoreInsert() throws IOException {
        Map<String,Object> map = IOUtils.getAllCoursesAndScore();
        List<Score> list = (List<Score>) map.get("scoreList");
       for (Score score : list) {
            scoreMapper.create(score);
        }
    }
}
