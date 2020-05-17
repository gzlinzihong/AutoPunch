package edu.gdpu.service.impl;

import edu.gdpu.entity.User;
import edu.gdpu.exception.PasswordIncorrectException;
import edu.gdpu.mapper.UserMapper;
import edu.gdpu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ilanky
 * @date 2020年 05月16日 03:51:46
 */
@Service
public class UserServiceImpl implements UserService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;


    @Override
    public User login(User user) throws PasswordIncorrectException {
        User user1 = userMapper.findByUsername(user.getUsername());
        if(user1==null){
            return null;
        }
        if(!user.getPassword().equals(user1.getPassword())){
            throw new PasswordIncorrectException("密码错误");
        }
        return user1;
    }

    @Override
    public User register(User user) {
        userMapper.save(user);
        logger.info(user.getUsername()+"注册了...");
        return user;
    }

    @Override
    public boolean isRegistered(String username) {
        User byUsername = userMapper.findByUsername(username);
        if(byUsername==null){
            return false;
        }else {
            return true;
        }
    }
}
