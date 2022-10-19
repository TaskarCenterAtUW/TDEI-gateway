package com.tdei.gateway.middleware;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        System.out.println("Prehandle called");
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getMethod());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
//        System.out.println("Post handle called");
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getMethod());


        // appInsightsClient.trackHttpRequest(request.getRequestURI(),new Date(), 0, "200", true);
        // request.get
        // HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        // appInsightsClient.trackMetric(new MetricTelemetry("User logged in", 1));

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        // System.out.println("After completion called");
    }
}
