package com.daishuhua.eprj.controller;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daishuhua.eprj.dto.Greeting;

@RestController
public class GreetingController {

	@Autowired
	private  HttpServletRequest request;
	
	private static final String template = "Hello, %s![client info: IP=%s HOST=%s]";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name, request.getRemoteAddr(), request.getRemoteHost()));
    }
}
