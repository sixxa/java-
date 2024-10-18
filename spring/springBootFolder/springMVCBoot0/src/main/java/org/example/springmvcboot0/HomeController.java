package org.example.springmvcboot0;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.springmvcboot0.model.Alien;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @ModelAttribute
    public void modelData(ModelMap m) {
        m.addAttribute("name","Aliens");
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/add")
    public String add(@RequestParam("num1") int i,@RequestParam("num2") int j, ModelMap m){
        int sum = i+j;
        m.addAttribute("sum",sum);
        return "result";
    }
    @RequestMapping("/addAlien")
    public String addalien(@ModelAttribute Alien a){
        return "result";
    }


}
