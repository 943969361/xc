package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
public class CourseService {

    @Resource
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    CoursePubRepository coursePubRepository;

    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    //课程计划查询
    @Transactional
    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

    //添加课程计划
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if(teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())){

            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 获取课程id
        String courseid = teachplan.getCourseid();
        //取出父结点id
        String parentid = teachplan.getParentid();

        // 判断父节点是否为空
        if(StringUtils.isEmpty(parentid)){
            // 如没有父节点则获取根节点
            parentid = this.getTeachplanRoot(courseid);
        }
        // 取出父节点信息
        Optional<Teachplan> byId = teachplanRepository.findById(parentid);
        if(!byId.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 父节点
        Teachplan teachplan1 = byId.get();
        //父结点级别
        String parentGrade = teachplan1.getGrade();
        //设置父结点
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");//未发布

        //子结点的级别，根据父结点来判断
        if(parentGrade.equals("1")){
            teachplan.setGrade("2");
        }else if(parentGrade.equals("2")){
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(teachplan1.getCourseid());
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }



    // 查询根节点查询不到自动添加
    private String  getTeachplanRoot(String courseid){

        // 校验课程id
        Optional<CourseBase> courseBase = courseBaseRepository.findById(courseid);
        if(!courseBase.isPresent()){
            return null;
        }
        CourseBase courseBase1 = courseBase.get();

        List<Teachplan> courseidAndParentid = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if(courseidAndParentid == null || courseidAndParentid.size()<=0){
            // 找不到根节点，自动添加
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseid);
            teachplanRoot.setPname(courseBase1.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");//1级
            teachplanRoot.setStatus("0");//未发布
            teachplanRepository.save(teachplanRoot);
            return teachplanRoot.getId();

        }
        // 找到节点
        Teachplan teachplan = courseidAndParentid.get(0);
        return teachplan.getId();

    }

    // 获取课程基础信息
    public CourseBase getCourseBaseById(String courseId) {

        CourseBase courseBase = new CourseBase();
        if(courseId != null){
            Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
            if(optional.isPresent()){
                courseBase = optional.get();
            }
        }
        if(courseBase !=null ){
            return courseBase;
        }else {
            return null;
        }
    }

    // 修改课程信息
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {

        ResponseResult result = new ResponseResult();
        CourseBase courseBaseMessage = new CourseBase();
        // 用id查询
        if(courseBase == null){
            courseBase = new CourseBase();
        }
        if(id != null){
            Optional<CourseBase> optional = courseBaseRepository.findById(id);
            // 查到有基础信息
            if(optional.isPresent()){
                courseBaseMessage = optional.get();
                System.out.println(courseBase);
                System.out.println(courseBaseMessage);
                //把s1的属性值拷贝到S2中
                //BeanUtils.copyProperties(courseBase,courseBaseMessage);
                //courseBaseMessage.setId(id);
                if(courseBase.getName() != null){
                    courseBaseMessage.setName(courseBase.getName());
                }
                courseBaseRepository.save(courseBaseMessage);
                result.setMessage("success");
            }
        }

        return result;
    }


    //保存CoursePub
    public CoursePub saveCoursePub(String id, CoursePub coursePub){

        if(StringUtils.isEmpty(id)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CoursePub coursePubNew = null;
        Optional<CoursePub> optional = coursePubRepository.findById(id);
        if(optional.isPresent()){
            CoursePub coursePub1 = optional.get();
        }
        if(coursePubNew == null){
            coursePubNew = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub,coursePubNew);
        //设置主键
        coursePubNew.setId(id);
        //更新时间戳为最新时间
        coursePub.setTimestamp(new Date());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-DD-MM HH:mm:ss");
        String s = format.format(new Date());
        coursePub.setPubTime(s);
        coursePubRepository.save(coursePub);
        return  coursePub;
    }

    //创建coursePub对象
    private CoursePub createCoursePub(String id){
        CoursePub coursePub = new CoursePub();
        coursePub.setId(id);

        // 基础信息
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if(optional == null){
            CourseBase courseBase = optional.get();
            BeanUtils.copyProperties(courseBase,coursePub);
        }

        Optional<CoursePic> optional1 = coursePicRepository.findById(id);
        if(optional1 == null){
            CoursePic coursePic = optional1.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic, coursePub);
        }
        //课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if(marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket, coursePub);
        }

        //课程计划
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        if(teachplanNode != null){

            String jsonString = JSON.toJSONString(teachplanNode);
            coursePub.setTeachplan(jsonString);
        }

        return  coursePub;
    }
}























