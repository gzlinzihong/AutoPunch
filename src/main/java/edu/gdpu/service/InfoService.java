package edu.gdpu.service;

import edu.gdpu.entity.Info;

public interface InfoService {

    void submit(Info info,int userId);

    Info get(int userId);

    void update(Info info,int userId);
}
