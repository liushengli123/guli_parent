package com.atguigu.eduservice.controller;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectExcel;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class MySubjectListener extends AnalysisEventListener<SubjectExcel>{


    public EduSubjectService eduSubjectService;
    public MySubjectListener() {
    }
    public MySubjectListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }


    @Override
    public void invoke(SubjectExcel data, AnalysisContext context) {
        if(data == null) {
            throw new GuliException(20001,"文件数据为空");
        }

        //一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
        //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(data.getOneSubjectName(),eduSubjectService);
        if(existOneSubject == null) { //没有相同一级分类，进行添加
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(data.getOneSubjectName());//一级分类名称
            eduSubjectService.save(existOneSubject);
        }

        //获取一级分类id值
        String pid = existOneSubject.getId();

        //添加二级分类
        //判断二级分类是否重复
        EduSubject existTwoSubject = this.existTwoSubject(data.getTwoSubjectName(),eduSubjectService,pid);
        if(existTwoSubject == null) {
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(data.getTwoSubjectName());//二级分类名称
            eduSubjectService.save(existTwoSubject);
        }
    }

        //一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
        //判断一级分类是否重复


        //获取一级分类id值


        //添加二级分类
        //判断二级分类是否重复

        //判断一级分类不能重复添加
        private EduSubject existOneSubject(String name,EduSubjectService eduSubjectService){
            QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
            wrapper.eq("title",name);
            wrapper.eq("parent_id",0);
            EduSubject oneSubject = eduSubjectService.getOne(wrapper);
            return  oneSubject;
        }

        //判断二级分类不能重复添加
        private EduSubject existTwoSubject(String name,EduSubjectService eduSubjectService,String pid){
            QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
            wrapper.eq("title",name);
            wrapper.eq("parent_id",pid);
            EduSubject twoSubject = eduSubjectService.getOne(wrapper);
            return twoSubject;
        }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
