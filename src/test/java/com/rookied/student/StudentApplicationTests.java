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
import org.junit.Before;
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
@SuppressWarnings("all")
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
    //主要课程cid
    //计科
    Integer[] major1 = {1, 4, 9, 13, 19, 27, 39, 40, 68, 69, 77, 92, 94, 98, 102, 105};
    //会计
    Integer[] major2 = {125, 128, 130, 142, 151, 156, 157, 166, 186, 194, 199, 200, 218, 223, 237, 242};
    @Test
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

    @Before
    public void init(){

    }

    /**
     * 插入主要课程
     */
    public void mainCourseInsert() {

    }

    /**
     * Step 1
     * 插入所有课程，并放入redis
     * @throws IOException
     */
    @Test
    public void courseInsert() throws IOException {
        Map<String,Object> map = IOUtils.getAllCoursesAndScore();
        List<Course> list = (List<Course>) map.get("courseList");
        Map<String,List<String>> map1 = new HashMap<>();
        List<String> courseList = new ArrayList<>();
        //主要课程
        List<String> mainCourse = new ArrayList<>();
        //将两个专业主要课程id数组转为list
        List<Integer> cidList1 = new ArrayList<Integer>(major1.length);
        List<Integer> cidList2 = new ArrayList<Integer>(major2.length);
        Collections.addAll(cidList1, major1);
        Collections.addAll(cidList2, major2);
        int term = 1;
        for (Course course : list) {
            //插入数据库
            courseMapper.create(course);
            //当学期变化时 把courseList放入map1
            if ("计算机科学与技术".equalsIgnoreCase(course.getCmaj())) {
                if (course.getCterm() != term) {
                    map1.put("计科" + term, courseList);
                    // 重新new 不能用 courseList.clear() map1放入的是map2的引用
                    courseList = new ArrayList<>();
                    term++;
                }
                courseList.add(course.getCname());

                //如果存放cid的list中包含该课程id，将课程名放入mainCourse
                if (cidList1.contains(course.getCid())) {
                    mainCourse.add(course.getCname());
                }
            }
        }
        //!!!!!!!这句没加找了好久的错误
        if (term == 6) map1.put("计科" + term, courseList);
        courseList = new ArrayList<>();
        //主要课程放入map1
        map1.put("计科主要课程", mainCourse);
        mainCourse = new ArrayList<>();
        term = 1;
        for (Course course : list) {
            if ("会计".equalsIgnoreCase(course.getCmaj())) {
                if (course.getCterm() != term) {
                    map1.put("会计" + term, courseList);
                    // 重新new 不能用 courseList.clear() map1放入的是map2的引用
                    courseList = new ArrayList<>();
                    term++;
                }
                //将每学期的课程名加入list
                courseList.add(course.getCname());

                //如果存放cid的list中包含该课程id，将课程名放入mainCourse
                if (cidList2.contains(course.getCid())) {
                    mainCourse.add(course.getCname());
                }
            }
        }
        if (term == 6) map1.put("会计" + term, courseList);
        //主要课程放入map1
        map1.put("会计主要课程", mainCourse);
        redisTemplate.opsForHash().putAll("courseMap", map1);
    }

    /**
     * Step 2
     * 插入所有成绩
     * @throws IOException
     */
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

    /**
     * Step 3
     * 将课程和对应成绩从数据库中查出放入redis
     */
    @Test
    public void findCourseByTerm() {
        List<Course> list = courseMapper.findCourseAndScore();
        //两个专业12个学期的课的成绩
        Map<String, Map<String, List<String>>> map1 = new LinkedHashMap<>();
        //存放259门科目
        Map<String, List<String>> map2 = new LinkedHashMap<>();
        //存放主要课程
        Map<String, List<String>> map4 = new LinkedHashMap<>();
        //存放每门科目对应的成绩
        List<String> scoreList;
        //将两个专业主要课程id数组转为list
        List<Integer> cidList1 = new ArrayList<Integer>(major1.length);
        List<Integer> cidList2 = new ArrayList<Integer>(major2.length);
        Collections.addAll(cidList1, major1);
        Collections.addAll(cidList2, major2);
        int term = 1;
        for (Course course : list) {
            //当学期变化时 把map2放入map1
            if ("计算机科学与技术".equalsIgnoreCase(course.getCmaj())) {
                if (course.getCterm() != term) {
                    map1.put("计科" + term, map2);
                    // 重新new 不能用 courseList.clear() map1放入的是map2的引用
                    map2 = new LinkedHashMap<>();
                    term++;
                }
                //将每学期的课程名加入list
                //存放成绩
                scoreList = new ArrayList<>();
                for (Score score : course.getCscores()) {
                    scoreList.add(score.getScore());
                }
                map2.put(course.getCname(), scoreList);
                //如果存放cid的list中包含该课程id，将课程名和对应成绩放入map4
                if (cidList1.contains(course.getCid())) {
                    map4.put(course.getCname(), scoreList);
                }
            }
        }
        //!!!!!!!这句没加找了好久的错误
        if (term == 6) map1.put("计科" + term, map2);
        map2 = new LinkedHashMap<>();
        map1.put("计科主要课程", map4);
        map4 = new LinkedHashMap<>();
        term = 1;
        for (Course course : list) {
            if ("会计".equalsIgnoreCase(course.getCmaj())) {
                if (course.getCterm() != term) {
                    map1.put("会计" + term, map2);
                    // 重新new 不能用 courseList.clear() map1放入的是map2的引用
                    map2 = new LinkedHashMap<>();
                    term++;
                }
                //将每学期的课程名加入list
                //存放成绩
                scoreList = new ArrayList<>();
                for (Score score : course.getCscores()) {
                    scoreList.add(score.getScore());
                }
                map2.put(course.getCname(), scoreList);

                //如果存放cid的list中包含该课程id，将课程名和对应成绩放入map4
                if (cidList2.contains(course.getCid())) {
                    map4.put(course.getCname(), scoreList);
                }
            }
        }
        if (term == 6) map1.put("会计" + term, map2);
        map1.put("会计主要课程", map4);
        redisTemplate.opsForHash().putAll("scoreMap", map1);

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
        // redisTemplate.opsForHash().putAll("courseMap", map3);
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
    public void show() {
        String cmaj = "计算机科学与技术";
        String cterm = "1";
        String key = "计科";
        List<Student> students = studentMapper.findAllScore("", 1, cmaj);
        Cursor<Map.Entry<Object, Object>> curosr = redisTemplate.opsForHash().scan("scoreMap", ScanOptions.NONE);
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        while (curosr.hasNext()) {
            Map.Entry<Object, Object> entry = curosr.next();
            map.put((String) entry.getKey(), (Map<String, List<String>>) entry.getValue());
        }

        //封装返回的页面展示 {{stuid:'2051337025'},{course1:70},{course2:85}....,'}
        List<Map<String, String>> stuLists = new ArrayList<>();
        //Map
        //K:V 课程名:成绩
        Map<String, String> scoreMap;
        //课程名
        //StringBuffer courseName = new StringBuffer();
        int num = 0;
        for (Student student : students) {
            scoreMap = new HashMap<>();
            scoreMap.put("stuid", student.getStuId());
            //课程数
            int index = 0;
            //学生数
            for (List<String> s : map.get(key + cterm).values()) {
                index++;
                scoreMap.put("course" + index, s.get(num));
            }
            num++;

            stuLists.add(scoreMap);
        }
        System.out.println(stuLists);
    }
}

