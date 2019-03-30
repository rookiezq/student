package com.rookied.student;

import com.rookied.student.bean.Course;
import com.rookied.student.bean.PageBean;
import com.rookied.student.bean.Score;
import com.rookied.student.bean.Student;
import com.rookied.student.mapper.CourseMapper;
import com.rookied.student.mapper.PageBeanMapper;
import com.rookied.student.mapper.ScoreMapper;
import com.rookied.student.mapper.StudentMapper;
import com.rookied.student.service.StudentService;
import com.rookied.student.util.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentApplicationTests {
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private PageBeanMapper pageBeanMapper;
    @Resource
    private StudentService studentService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void stuList() {
        int term = 0;
        //查询某个学生所有学期的课程 0代表所有学期
        List<Student> list = studentService.findAllScore("", term, "会计");
        //两个专业12个学期的课
        Map<String, List<String>> map = new HashMap<>(12);
        List<String> courseList = new ArrayList<>();
        for (Student student : list) {
            List<PageBean> pageBeans = student.getPageBeans();
            for (PageBean pageBean : pageBeans) {
                term++;
                //按照每学期获取所有的课程名
                if (term != pageBean.getCterm()) {
                    continue;
                }
                courseList.add(pageBean.getCname());
            }
        }
    }

    /**
     * 查询所有课程
     */
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
        List<Course> list = (List<Course>) map.get("courseList");
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

    @Test
    public void findAll() {
        List<PageBean> list = pageBeanMapper.findAll();
        for (PageBean pageBean : list) {
            System.out.println(pageBean);
        }
    }

    @Test
    public void findCourseByTerm() {
        List<Course> list = courseMapper.findAll();
        //两个专业12个学期的课
        Map<String, List<String>> map = new HashMap<>(12);
        List<String> courseList = new ArrayList<>();
        int term = 1;
        for (Course course : list) {
            //当学期变化时 把list放入map
            if ("计算机科学与技术".equalsIgnoreCase(course.getCMaj())) {
                if (course.getCTerm() != term) {
                    map.put("计科" + term, courseList);
                    // 重新new 不能用 courseList.clear() map放入的是list的引用
                    courseList = new ArrayList<>();
                    term++;
                }
                //将每学期的课程名加入list
                courseList.add(course.getCName());
            }
            //!!!!!!!这句没加找了好久的错误
            if (term == 6) map.put("计科" + term, courseList);
        }
        term = 1;
        for (Course course : list) {
            if ("会计".equalsIgnoreCase(course.getCMaj())) {
                if (course.getCTerm() != term) {
                    map.put("会计" + term, courseList);
                    // 重新new 不能用 courseList.clear() map放入的是list的引用
                    courseList = new ArrayList<>();
                    term++;
                }
                //将每学期的课程名加入list
                courseList.add(course.getCName());
            }
            if (term == 6) map.put("会计" + term, courseList);
        }

       // redisTemplate.opsForHash().putAll("scoreMap",map);
        //Map<Object, Object> scoreMap = redisTemplate.opsForHash().entries("scoreMap");

        redisTemplate.opsForValue().multiSet(map);
        //System.out.println(redisTemplate.opsForValue().get("计科1"));
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }
}

