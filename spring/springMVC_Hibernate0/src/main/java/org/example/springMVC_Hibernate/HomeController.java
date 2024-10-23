package org.example.springMVC_Hibernate;

import org.example.springMVC_Hibernate.model.Alien;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
