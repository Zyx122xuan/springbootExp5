package com.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gateway.entity.CommentResult;
import com.gateway.entity.User;

@RestController
public class ConsumerServiceFallback {
    @RequestMapping(value = "/fallback",method = RequestMethod.GET)
    public CommentResult GetCommentResult(){
        System.out.println("由于ConsumerService异常，进行服务降级响应");
        return new CommentResult<>(
                403,"由于ConsumerService异常，进行服务降级响应",new User());
    }
}
