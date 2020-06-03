package edu.gdpu.controller;

import edu.gdpu.domain.Respon;
import edu.gdpu.entity.Info;
import edu.gdpu.entity.User;
import edu.gdpu.exception.AccountDuplicationException;
import edu.gdpu.exception.PasswordIncorrectException;
import edu.gdpu.service.InfoService;
import edu.gdpu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public String submit(Info info, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        try {
            infoService.submit(info,user.getId());
        } catch (AccountDuplicationException e) {
            model.addAttribute("error",e.getMessage());
            return "form";
        }

        session.setAttribute("info",info);
        model.addAttribute("msg","保存成功");
        return "update";
    }

    @GetMapping("/formPage")
    public String get(HttpSession session){
        User user = (User) session.getAttribute("user");
        Info info = infoService.get(user.getId());
        if(info==null){
            return "form";
        }
        else {
            session.setAttribute("info",info);
            return "update";
        }
    }

    @GetMapping("/update")
    public String updatePage(){
        return "update";
    }

    @PostMapping("/update")
    public String update(Info info,HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        infoService.update(info,user.getId());
        session.setAttribute("info",info);
        model.addAttribute("msg","保存成功");
        return "update";
    }

    @GetMapping("/del")
    public String delPage(){
        return "delete";
    }

    @PostMapping("/del")
    public String del(User user,Model model){
        User login = null;
        try {
            login = userService.login(user);
            if(login==null){
                model.addAttribute("msg","无此用户");
                return "delete";
            }
        } catch (PasswordIncorrectException e) {
            model.addAttribute("msg",e.getMessage());
            return "delete";
        }
        infoService.delete(login);
        model.addAttribute("msg","删除成功!");
        return "delete";
    }
}
