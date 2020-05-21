package edu.gdpu.controller;

import edu.gdpu.entity.Message;
import edu.gdpu.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author ilanky
 * @date 2020年 05月20日 22:48:51
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String index(HttpSession session){
        Message one = messageService.getOne();
        session.setAttribute("mes",one);
        return "index";
    }
}
