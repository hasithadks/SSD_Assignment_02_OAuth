package com.ssd.ssd_assignment_02_v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PageController {

    @GetMapping(value="/")
    public String getRoot(HttpServletRequest request, HttpServletResponse response) {
        if(request.getSession(false)== null) {// check the authentication in session
            return "redirect:/index.html";// if session is null then redirect to login page(index)
        }
        return "redirect:/home.html";
    }

    @GetMapping(value="/index.html")
    public String getIndex(HttpServletRequest request, HttpServletResponse response) {
        if(request.getSession(false)== null) {// check the authentication in session
            return "/index.html";// if session is null then redirect to login page(index)
        }
        return "redirect:/home.html";// if not redirect to home page
    }

    @GetMapping(value="/home.html")
    public String getHome(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getSession(false) == null) {// check the authentication in session
            return "redirect:/index.html";// if session is null then redirect to login page(index)
        }
        return "/home.html";// if not redirect to home page
    }
}
