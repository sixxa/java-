package org.example.springmvcboot0;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/add")
    public ModelAndView add(@RequestParam("num1") int i,@RequestParam("num2") int j){
        ModelAndView mv = new ModelAndView("result");
        int sum = i+j;
        mv.addObject("sum",sum);
        return mv;
    }
}
