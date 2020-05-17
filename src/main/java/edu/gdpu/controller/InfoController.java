package edu.gdpu.controller;

import edu.gdpu.domain.Respon;
import edu.gdpu.entity.Info;
import edu.gdpu.entity.User;
import edu.gdpu.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author ilanky
 * @date 2020年 05月16日 05:07:44
 */
@Controller
public class InfoController {

    @Autowired
    private InfoService infoService;

    @PostMapping("/submit")
    public String submit(Info info,HttpSession session){
        User user = (User) session.getAttribute("user");
        infoService.submit(info,user.getId());
        return "redirect:/formPage";
    }

    @GetMapping("/formPage")
    public String get(HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        Info info = infoService.get(user.getId());
        if(info==null){
            return "redirect:/form";
        }
        else {
            session.setAttribute("info",info);
            return "redirect:/update";
        }
    }

    @GetMapping("/update")
    public String updatePage(){
        return "update";
    }

    @PostMapping("/update")
    public String update(Info info,HttpSession session){
        User user = (User) session.getAttribute("user");
        infoService.update(info,user.getId());
        session.setAttribute("info",info);
        return "update";
    }
}
