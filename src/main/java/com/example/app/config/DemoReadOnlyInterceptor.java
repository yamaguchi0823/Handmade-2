package com.example.app.config;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class DemoReadOnlyInterceptor implements HandlerInterceptor{
	
	private static final Set<String> READ_METHODS = 
			Set.of("GET","HEAD","OPTIONS");
	
	@Value("${app.demo-read-only:false}")
	private boolean readOnly;
	
	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler
			) throws IOException {
		
		if (!readOnly || READ_METHODS.contains(request.getMethod())) {
			return true;
		}
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(
				"{\"message\":\"公開デモは閲覧専用です。登録・編集・削除はできません。\"}"
				);
		return false;
	}

}
