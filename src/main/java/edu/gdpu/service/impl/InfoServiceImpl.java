package edu.gdpu.service.impl;

import edu.gdpu.entity.Info;
import edu.gdpu.entity.User;
import edu.gdpu.exception.AccountDuplicationException;
import edu.gdpu.mapper.InfoMapper;
import edu.gdpu.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ilanky
 * @date 2020年 05月16日 05:10:00
 */
@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private InfoMapper infoMapper;

    @Override
    public void submit(Info info,int userId) throws AccountDuplicationException {
        if(infoMapper.findByUsername(info)!=null){
            throw new AccountDuplicationException("该学管账号已存在自动打卡名单内");
        }else {
            infoMapper.save(info,userId);
        }
    }

    @Override
    public Info get(int userId) {
        return infoMapper.findByUserId(userId);
    }

    @Override
    public void update(Info info, int userId) {
        infoMapper.update(info,userId);
    }

    @Override
    public void delete(User user) {
        infoMapper.deleteByUserId(user.getId());
    }
}
