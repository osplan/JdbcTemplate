package com.zenoc.core.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

//@Component
public class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext ctx = null;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ctx = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return ctx;
	}
	public static ApplicationContext getAppContext() {
		return ctx;
	}
	public static <T> Map<String , T> getBeansByType(Class<T> cl){
		return getAppContext().getBeansOfType(cl);
	}
	public static <T> T getBeanByType(String beanName, Class<T> cl){
		Map<String, T>m = getBeansByType(cl);
		return m!=null?m.get(beanName):null;
//		return getAppContext().getBean(beanName, cl);
	}
	public <T> T getBean(String beanName, Class<T> cl){
		return getBeanByType(beanName, cl);
	}
}
