package com.rookied.student.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rookied.student.bean.Score;
import com.rookied.student.bean.Student;
import com.rookied.student.mapper.StudentMapper;
import com.rookied.student.service.ScoreService;
import com.rookied.student.service.StudentService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private ScoreService scoreService;

    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 通过学号查找所有成绩
     *
     * @param stuId  学号
     * @param cterm  学期
     * @param stuMaj 专业
     * @return 成绩列表
     */
/*    @Override
    @SuppressWarnings("all")
    public List<Map<String, String>> findAllScore(String stuId, int cterm, String stuMaj) {
        //如果学号为空
        boolean flag1 = "".equals(stuId) || stuId == null;
        boolean flag2 = "".equals(stuMaj) || stuMaj == null;
        //按专业查询 分别赋给一个默认学号
        *//*if (flag1 && MAJOR_CS.equalsIgnoreCase(stuMaj)){
            stuId = "20141308001";
        }
        if (flag1 && MAJOR_AC.equalsIgnoreCase(stuMaj)){
            stuId = "20141312001";
        }*//*
     *//*if (flag1 && flag2){
            stuMaj = MAJOR_CS;
        }*//*
        //查询某个学生所有学期的课程 0代表所有学期
        List<Student> list = studentMapper.findAllScore(stuId, cterm, stuMaj);
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

        return stuLists;
    }*/
    @Override
    @SuppressWarnings("all")
    public List<Map<String, String>> findAllScore(String stuId, String cterm, String stuMaj) {
        List<Student> students = studentMapper.findAllScore(stuId, "主要课程".equals(cterm)?0:Integer.valueOf(cterm), stuMaj);
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

            for (List<String> s : map.get(("计算机科学与技术".equals(stuMaj)?"计科":"会计") + cterm).values()) {
                index++;
                scoreMap.put("course" + index, s.get(num));
            }
            num++;

            stuLists.add(scoreMap);
        }
        return stuLists;
    }


    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Student> findAll() {
        return studentMapper.findAllScore("", 0, "");
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

    /**
     * 将list放进redis
     *
     * @param list 获取的list
     */
    @Override
    @SuppressWarnings("unchecked")
    public void pushInRedis(String key, List<Student> list) {
        //查询所有学生成绩并放入redis
        for (Student o : list) {
            redisTemplate.opsForList().rightPush(key, o);
        }
    }

    /**
     * 从redis中获取list
     *
     * @param key 存放的key值
     * @return 获取的list
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Student> rangeFromRedis(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
