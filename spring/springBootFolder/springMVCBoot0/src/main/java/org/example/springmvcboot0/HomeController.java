package org.example.springmvcboot0;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.springmvcboot0.model.Alien;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @GetMapping("getAliens")
    public String getAliens(ModelMap m){
        List<Alien> aliens = Arrays.asList(new Alien(101,"Toka"),new Alien(102,"Navin"));
        m.addAttribute("result",aliens);
        return "showAliens";
    }

    @PostMapping("addAlien")
    public String addAlien(@ModelAttribute Alien a){

        return "result";
    }


}
