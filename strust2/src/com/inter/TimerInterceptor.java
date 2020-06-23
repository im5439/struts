package com.inter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class TimerInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(TimerInterceptor.class);
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		//인터셉터 실행전
		long start = System.currentTimeMillis();

		//인터셉터 stack의 다음 인터셉터 또는 마지막 인터셉터 액션 실행
		String result = invocation.invoke();
		
		//인터셉터 실행후
		long end = System.currentTimeMillis();
		
		log.info("실행시간:" + (end - start) + "ms");
		
		
		return result;
	}
	
	
	
	
	
}
