package project.vinyl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class bootTestController {

    @GetMapping("/test")
    public String tst(){
        return "bootStrapTest/postWrite";
    }

}
