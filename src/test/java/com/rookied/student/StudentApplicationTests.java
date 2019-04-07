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
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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
    @SuppressWarnings("all")
    public void stuList() {
        int term = 0;
        //查询某个学生所有学期的课程 0代表所有学期
        List<Student> list = studentMapper.findAllScore("", 0, "");
        for (Student o : list) {
            redisTemplate.opsForList().rightPush("students", o);
        }
        //redisTemplate.opsForList().leftPushAll("students",list);
        List<Object> studentlist = redisTemplate.opsForList().range("students", 0, -1);
        //List<Student> studentList = (List<Student>)studentlist.get(0);
        for (Object o : studentlist) {
            System.out.println((Student) o);
        }
        //System.out.println(list.size());
        for (Student student : list) {
            //List<Score> scores = student.getScores();
            //System.out.println(student.getStuId());
        }
        //两个专业12个学期的课
        Map<String, List<String>> map = new HashMap<>(12);
        List<String> courseList = new ArrayList<>();
        //System.out.println(list);
    }

    @Test
    @SuppressWarnings("all")
    public void stu() {
        //查询某个学生所有学期的课程 0代表所有学期
        List<Student> list = studentMapper.findAllScore("", 1, "计算机科学与技术");
        //封装返回的页面展示 {{stuid:'2051337025'},{course1:70},{course2:85}....,'}
        List<Map<String, String>> stuLists = new ArrayList<>();
        //Map
        //K:V 课程名:成绩
        Map<String, String> map;
        //课程名
        //StringBuffer courseName = new StringBuffer();
        for (Student student : list) {
            map = new HashMap<>();
            map.put("stuid", student.getStuId());
            //课程数
            int index = 0;
            for (Score score : student.getScores()) {
                index++;
                map.put("course" + index, score.getScore());
            }
            stuLists.add(map);
        }
        for (Map<String, String> stuList : stuLists) {
            System.out.println(stuList);
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

    @Test
    public void findStuAndScores() {

    }

    /**
     * 插入所有课程
     * @throws IOException
     */
    @Test
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
    @SuppressWarnings("all")
    public void findCourseByTerm() {
        List<Course> list = courseMapper.findCourseAndScore();
        //两个专业12个学期的课的成绩
        Map<String, Map<String, List<String>>> map1 = new HashMap<>(12);
        //存放259门科目
        Map<String, List<String>> map2 = new HashMap<>();
        //存放每门科目对应的成绩
        List<String> scoreList;
        int term = 1;
        for (Course course : list) {
            //当学期变化时 把map2放入map1
            if ("计算机科学与技术".equalsIgnoreCase(course.getCmaj())) {
                if (course.getCterm() != term) {
                    map1.put("计科" + term, map2);
                    // 重新new 不能用 courseList.clear() map1放入的是map2的引用
                    map2 = new HashMap<>();
                    term++;
                }
                //将每学期的课程名加入list
                //存放成绩
                scoreList = new ArrayList<>();
                for (Score score : course.getCscores()) {
                    scoreList.add(score.getScore());
                }
                map2.put(course.getCname(), scoreList);
            }
        }
        //!!!!!!!这句没加找了好久的错误
        if (term == 6) map1.put("计科" + term, map2);
        map2 = new HashMap<>();
        term = 1;
        for (Course course : list) {
            if ("会计".equalsIgnoreCase(course.getCmaj())) {
                if (course.getCterm() != term) {
                    map1.put("会计" + term, map2);
                    // 重新new 不能用 courseList.clear() map1放入的是map2的引用
                    map2 = new HashMap<>();
                    term++;
                }
                //将每学期的课程名加入list
                //存放成绩
                scoreList = new ArrayList<>();
                for (Score score : course.getCscores()) {
                    scoreList.add(score.getScore());
                }
                map2.put(course.getCname(), scoreList);
            }
            if (term == 6) map1.put("会计" + term, map2);
        }
        //System.out.println(map1.get("计科6"));
        redisTemplate.opsForHash().putAll("scoreMap", map1);
        //Map<Object, Object> scoreMap = redisTemplate.opsForHash().entries("scoreMap");

        //redisTemplate.opsForValue().multiSet(map1);
        //System.out.println(redisTemplate.opsForValue().get("计科1"));

        //存放各个专业每个学期的课程
        List<String> courseList ;
        Map<String,List<String>> map3 = new HashMap<>();
        for (Map.Entry<String, Map<String, List<String>>> entry : map1.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            courseList = new ArrayList<>();
            Map<String, List<String>> value = entry.getValue();
            Set<String> strings = value.keySet();
            for (String string : strings) {
                courseList.add(string);
            }
            map3.put(entry.getKey(),courseList);
        }
        redisTemplate.opsForHash().putAll("courseMap", map3);
    }

    @Test
    public void findCourseAndScore() {
        List<Course> list = courseMapper.findCourseAndScore();
        System.out.println(list.size());
        //存放259门科目
        Map<String, List<String>> map2 = new HashMap<>(259);
        //存放每门科目对应的成绩
        List<String> scoreList;
        int i = 0;
        for (Course course : list) {
            //System.out.println(course.getCname());
            scoreList = new ArrayList<>();
            for (Score score : course.getCscores()) {
                scoreList.add(score.getScore());
            }
            map2.put(course.getCname(), scoreList);
        }
        System.out.println(map2.size());
        for (Map.Entry<String, List<String>> entry : map2.entrySet()) {
        }
    }

    @Test
    @SuppressWarnings("all")
    public void show() {
        Cursor<Map.Entry<Object, Object>> curosr = redisTemplate.opsForHash().scan("scoreMap", ScanOptions.NONE);
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        while (curosr.hasNext()) {
            Map.Entry<Object, Object> entry = curosr.next();
            map.put((String) entry.getKey(), (Map<String, List<String>>) entry.getValue());
        }
        System.out.println(map.get("计科1"));
    }
}

