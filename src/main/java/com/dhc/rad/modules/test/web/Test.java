package com.dhc.rad.modules.test.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dhc.rad.modules.test.service.OnhandNumService;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/spring-context*.xml");
		OnhandNumService onhandNumService = context.getBean("onhandNumService",OnhandNumService.class);
		try {
//			boolean b = onhandNumService.wwyl("PM160728000215," +
//					"PM160705000018,PM160705000017,PM160705000016,PM160721000310," +
//					"PM160721000309,PM160721000308,PM160627000066,PM151120000092," +
//					"PM160606000147,PM160530000331,PM160526000016,PM160517000028," +
//					"PM160517000026,PM160310000370,PM151201000204,PM151116000056");
//			boolean b = onhandNumService.blyl("PM160728000224,PM160801000362,PM160802000139,PM160729000021," +
//					"PM160729000019,PM160801000363,PM160725000259,PM160724000001");
			boolean b = onhandNumService.ljcksl();
			if (b) {
				System.out.println("成功");
			} else {
				System.out.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常");
		}
	}

}
