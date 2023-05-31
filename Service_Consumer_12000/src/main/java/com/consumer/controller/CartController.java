package com.consumer.controller;

import com.consumer.entity.CommentResult;
import com.consumer.entity.User;
import com.consumer.feign.UserFeignService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/cart")
//@LoadBalancerClient(name="provider-server",configuration= CustomLoadBalancedConfig.class)
public class CartController {

    @Autowired
    private UserFeignService userFeignService;

    @GetMapping("/hello")
    public String hello(){

        return userFeignService.Hello();
    }

    @GetMapping("/addCart/{userId}")
    //@RateLimiter(name="rate-limiterA", fallbackMethod = "fallback")
    @CircuitBreaker(name="backendA", fallbackMethod = "fallback")
//    @Bulkhead(name="bulkheadA", fallbackMethod = "fallback", type=Bulkhead.Type.THREADPOOL)
    public CommentResult<User> addCart(@PathVariable Integer userId) throws InterruptedException{

        System.out.println("进入方法");

        //Thread.sleep(10000L);//阻塞10s
        CommentResult<User> list = userFeignService.getUserById(userId);
        System.out.println("离开方法");
        return list;
    }

    public CommentResult<User> fallback(Integer userId, Throwable e){
        e.printStackTrace();
        System.out.println("fallback已经调用!");

        CommentResult<User> result = new CommentResult<>(400, "当前用户服务不正常，请稍后再试！", new User());
        return result;
    }

}