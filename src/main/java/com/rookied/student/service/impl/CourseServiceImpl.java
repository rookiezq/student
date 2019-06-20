package com.rookied.student.service.impl;

import com.rookied.student.bean.Course;
import com.rookied.student.mapper.CourseMapper;
import com.rookied.student.service.CourseService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-30 13:40
 **/
@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据学期查找课程
     *
     * @return
     */
    @Override
    public Map<String, List<String>> findCourseByTerm() {
        List<Course> list = courseMapper.findAll();
        //两个专业12个学期的课
        Map<String, List<String>> map = new HashMap<>(12);
        List<String> courseList = new ArrayList<>();
        int term = 1;
        for (Course course : list) {
            //当学期变化时 把list放入map
            if ("计算机科学与技术".equalsIgnoreCase(course.getCmaj())) {
                if (course.getCterm() != term) {
                    map.put("计科" + term, courseList);
                    // 重新new 不能用 courseList.clear() map放入的是list的引用
                    courseList = new ArrayList<>();
                    term++;
                }
                //将每学期的课程名加入list
                courseList.add(course.getCname());
            }
            //!!!!!!!这句没加找了好久的错误
            if (term == 6) {
                map.put("计科" + term, courseList);
            }
        }
        term = 1;
        for (Course course : list) {
            if ("会计".equalsIgnoreCase(course.getCmaj())) {
                if (course.getCterm() != term) {
                    map.put("会计" + term, courseList);
                    // 重新new 不能用 courseList.clear() map放入的是list的引用
                    courseList = new ArrayList<>();
                    term++;
                }
                //将每学期的课程名加入list
                courseList.add(course.getCname());
            }
            if (term == 6) {
                map.put("会计" + term, courseList);
            }
        }
/*        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }*/
        return map;
    }

    /**
     * 从redis中查找课程
     *
     * @param cmaj  专业
     * @param cterm 学期
     * @return 课程表
     */
    @Override
    @SuppressWarnings("all")
    public List<String> findCourseByRedis(String cmaj, String cterm) {
        //redisTemplate.opsForHash().putAll("scoreMap",map);
        //Map<Object, Object> scoreMap = redisTemplate.opsForHash().entries("scoreMap");
        //redisTemplate.opsForValue().multiSet(map);

        //默认
        String key = cmaj+cterm;
        Cursor<Map.Entry<Object, Object>> curosr = redisTemplate.opsForHash().scan("courseMap", ScanOptions.NONE);
        Map<String,List<String>> map = new HashMap<>();
        while(curosr.hasNext()){
            Map.Entry<Object, Object> entry = curosr.next();
            map.put((String)entry.getKey(),(List<String>)entry.getValue());
        }
        return map.get(key);
    }

    /**
     * 查找课程对应成绩
     *
     * @return 课程对应成绩集的map
     */
    @Override
    public Map<String, List<String>> findCourseAndScore() {

        return null;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Course> findAll() {
        return courseMapper.findAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public List<Course> findById(Long id) {
        return null;
    }

    /**
     * 添加
     *
     * @param course
     */
    @Override
    public void create(Course course) {

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
     * @param course
     */
    @Override
    public void update(Course course) {

    }

    /**
     * 将list放进redis
     *
     * @param list 获取的list
     */
    @Override
    public void pushInRedis(String key,List<Course> list) {

    }

    /**
     * 从redis中获取list
     *
     * @param key 存放的key值
     * @return 获取的list
     */
    @Override
    public List<Course> rangeFromRedis(String key) {
        return null;
    }
}
