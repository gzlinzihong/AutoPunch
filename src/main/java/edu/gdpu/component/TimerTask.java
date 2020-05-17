package edu.gdpu.component;

import edu.gdpu.domain.AutoPunch;
import edu.gdpu.domain.AutoPunchFailedException;
import edu.gdpu.domain.Punch;
import edu.gdpu.mapper.InfoMapper;
import edu.gdpu.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ilanky
 * @date 2020年 05月16日 10:11:04
 */
@Component
public class TimerTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private InfoMapper infoMapper;

    @Autowired
    private UserMapper userMapper;

    @Scheduled(cron = "0 0 1 * * ?")
    public void autoPunch(){
        Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("定时任务开始..");
        List<Punch> all = infoMapper.findAll();
        for(Punch punch:all){
            try {
                logger.info("账号为"+punch.getUsername());
                new AutoPunch(punch).start();
                logger.info("执行完毕...");
                sendEmail(punch, sd.format(now)+"时间打卡成功!");
            } catch (AutoPunchFailedException e) {
                logger.warn("账号为"+punch.getUsername()+"异常了..."+e.getMessage());
                sendEmail(punch,sd.format(now)+e.getMessage());
            }catch (Exception e){
                try {
                    logger.warn("账号为"+punch.getUsername()+"异常了..."+e.getMessage());
                    logger.info("尝试发送邮箱..");
                    sendEmail(punch,sd.format(now)+e.getMessage());
                }catch (Exception e1){
                    logger.warn("账号为"+punch.getUsername()+"异常了..."+e.getMessage());
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.warn(e.getLocalizedMessage());
            }
        }
    }

    public void sendEmail(Punch punch,String msg){
        SimpleMailMessage m=new SimpleMailMessage();
        m.setFrom("lzhooslq@outlook.com");
        System.out.println(userMapper.findById(punch.getUserId()).getEmail());
        m.setTo(userMapper.findById(punch.getUserId()).getEmail());
        m.setText(msg);
        m.setSubject("打卡任务提醒");//主题
        javaMailSender.send(m);
    }
}
