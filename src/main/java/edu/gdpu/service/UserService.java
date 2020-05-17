package edu.gdpu.service;

import edu.gdpu.entity.User;
import edu.gdpu.exception.PasswordIncorrectException;

public interface UserService {

    User login(User user) throws PasswordIncorrectException;

    User register(User user);

    boolean isRegistered(String username);
}
