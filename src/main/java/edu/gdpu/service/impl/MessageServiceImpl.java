package edu.gdpu.service.impl;

import edu.gdpu.entity.Message;
import edu.gdpu.mapper.MessageMapper;
import edu.gdpu.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ilanky
 * @date 2020年 05月20日 22:49:40
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Message getOne() {
        return messageMapper.findNewest();
    }
}
