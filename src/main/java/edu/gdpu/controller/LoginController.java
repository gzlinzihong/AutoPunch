package edu.gdpu.controller;

import edu.gdpu.domain.Respon;
import edu.gdpu.entity.User;
import edu.gdpu.exception.PasswordIncorrectException;
import edu.gdpu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author ilanky
 * @date 2020年 05月16日 03:38:40
 */

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @PostMapping("/register")
    public String register(User user,HttpSession session,Model model){
        Respon registered = isRegistered(user.getUsername());
        if(registered.getStatus()!=200){
            model.addAttribute("msg","用户名已被注册");
            return "register";
        }
        User register = userService.register(user);
        session.setAttribute("user",register);
        return "redirect:/formPage";
    }

    @RequestMapping("/login")
    public String login(User user, Model model, HttpSession session) {
        try {
            User login = userService.login(user);
            if(login==null){
                model.addAttribute("msg","无此用户");
                return "index";
            }
            session.setAttribute("user",login);
        } catch (PasswordIncorrectException e) {
            model.addAttribute("msg",e.getMessage());
            return "index";
        }
        return "redirect:/formPage";
    }

    @GetMapping("/loginOut")
    public String loginOut(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/isRegistered")
    @ResponseBody
    public Respon isRegistered(String username){
        boolean registered = userService.isRegistered(username);
        Respon respon = new Respon();
        if(registered==true){
            respon.setStatus(403);
            respon.setMsg("用户名已被注册");
        }
        else {
            respon.setMsg("用户名可以注册");
            respon.setStatus(200);
        }
        return respon;
    }





}
