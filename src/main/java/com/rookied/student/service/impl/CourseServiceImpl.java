package com.rookied.student.service.impl;

import com.rookied.student.bean.Course;
import com.rookied.student.mapper.CourseMapper;
import com.rookied.student.service.CourseService;
import org.springframework.data.redis.core.RedisTemplate;
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
            if (term == 6) {
                map.put("计科" + term, courseList);
            }
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
    public List<String> findCourseByRedis(String cmaj, int cterm) {
        //redisTemplate.opsForHash().putAll("scoreMap",map);
        //Map<Object, Object> scoreMap = redisTemplate.opsForHash().entries("scoreMap");
        //redisTemplate.opsForValue().multiSet(map);

        //默认
        String key = cmaj+cterm;


        return (List<String>) redisTemplate.opsForValue().get(key);
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
}
