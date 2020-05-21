package edu.gdpu.punch;

import edu.gdpu.entity.Message;
import edu.gdpu.mapper.MessageMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PunchApplicationTests {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSave(){
        Message message = new Message();
        message.setContent("我是测试公告1");
        messageMapper.save(message);
    }

    @Test
    public void testFind(){
        System.out.println(messageMapper.findNewest());
    }

}
