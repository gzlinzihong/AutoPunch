package edu.gdpu.service;

import edu.gdpu.entity.Info;
import edu.gdpu.entity.User;
import edu.gdpu.exception.AccountDuplicationException;

public interface InfoService {

    void submit(Info info,int userId) throws AccountDuplicationException;

    Info get(int userId);

    void update(Info info,int userId);

    void delete(User user);
}
