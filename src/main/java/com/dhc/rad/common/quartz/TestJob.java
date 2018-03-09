package com.dhc.rad.common.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务测试类
 * Created by yunqi on 2017/6/15.
 */
public class TestJob implements Job {
    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d = new Date();
    String returnstr = DateFormat.format(d);

    @Override
    public void execute(JobExecutionContext arg0) {
        // TODO Auto-generated method stub
        System.out.println(returnstr + "★★★★★★★★★★★");
    }

}  