package com.churkit.targetserver.controller;


import com.churkit.targetserver.request.Request;
import com.churkit.targetserver.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Scanner;

@RestController
@RequestMapping
public class Controller {

    @GetMapping("/home")
    public String getHtmlFromMainPage(){
        return "SOMETHING";
    }

}
