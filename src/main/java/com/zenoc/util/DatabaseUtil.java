package com.zenoc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class DatabaseUtil {
	private final static Logger logger = LoggerFactory
			.getLogger(DatabaseUtil.class);
	private final static String MACHINE_CODE ="03";
	private static Long preVal = new Long(0);

	/**
	 * 生成唯一主键值
	 * 
	 * @description:
	 * @create :2012-6-1
	 * @author :Simon
	 * @return
	 * @return :Long
	 */
	public static synchronized Long getDatabasePriykey() {
		String timeStamp = System.currentTimeMillis() + ""; // 13 + 2 = 14
		timeStamp = timeStamp.substring(1, timeStamp.length()); //
		BigDecimal bd = new BigDecimal(timeStamp); // 17位
		if (preVal.longValue() >= bd.longValue()) {
			preVal = preVal + 1;
			timeStamp = preVal + MACHINE_CODE; // 13 + 2 = 15
		} else {
			preVal = new Long(timeStamp);
			timeStamp = timeStamp + MACHINE_CODE; // 13 + 2 = 15
		}
		bd = new BigDecimal(timeStamp); // 17
		return bd.longValue();
	}

//	public static String getDateFlowNumber(String pattern, int countlen) {
//		if (!StringUtil
//				.isTrue(PropertyConfig.getPropertyValue("redis.enabled")))
//			throw new RuntimeException("没有redis提供支持，功能不可用");
//		Timestamp t = new Timestamp(System.currentTimeMillis());
//		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//		String prefix = sdf.format(t),key = RedisServerKeys.COUNTER+prefix;
//
//		long n = RedisService.incr(key);
//		if(n==1){
//			RedisService.expire(key, 24*60*60);
//		}
//
//		return prefix + StringUtil.leftPad(n, countlen);
//	}
}
