package com.graduationProject._thYear.Test;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("api/")
public class TestController {
    
    @GetMapping("test")
    public String test() {
        return "testing";
    }
    

}
