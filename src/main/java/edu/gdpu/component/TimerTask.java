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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ilanky
 * @date 2020年 05月16日 10:11:04
 */
@Component
public class TimerTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static List<Punch> punches = new ArrayList<>();

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private InfoMapper infoMapper;

    @Autowired
    private UserMapper userMapper;

    private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "0 0 1 * * ?")
    public void autoPunch(){
        logger.info("定时任务开始..");
        List<Punch> all = infoMapper.findAll();
        for(Punch punch:all){
            try {
                logger.info("账号为"+punch.getUsername());
                new AutoPunch(punch).start();
                logger.info("执行完毕...");
                sendEmail(punch, sd.format(System.currentTimeMillis())+"时间打卡成功!");
            } catch (AutoPunchFailedException e) {
                logger.warn("账号为"+punch.getUsername()+"异常了..."+e.getMessage());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(baos));
                logger.warn(baos.toString());
                logger.info("尝试发送提示失败邮箱..");
                sendEmail(punch,sd.format(System.currentTimeMillis())+"异常原因:"+e.getMessage()+"  10分钟后会再尝试打卡一次...若仍然失败请手动打卡...");
                logger.info("发送提示失败的邮箱成功!..");
                punches.add(punch);
            }catch (Exception e){
                try {
                    logger.warn("账号为"+punch.getUsername()+"异常了..."+e.getMessage());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    logger.warn(baos.toString());
                    logger.info("尝试发送提示失败邮箱..");
                    if(e instanceof SocketTimeoutException){
                        sendEmail(punch,sd.format(System.currentTimeMillis())+"打卡失败...原因:超时  10分钟后会再尝试打卡一次...若仍然失败请手动打卡...");
                        punches.add(punch);
                    }
                    else {
                        sendEmail(punch,sd.format(System.currentTimeMillis())+"打卡失败...请手动打卡...原因可能是由于你填写的学管账号密码不正确而失败。请检查账号密码是否正确，若确认无误仍然打卡失败，可回复此邮箱告知");
                    }
                    logger.info("发送提示失败的邮箱成功!..");
                }catch (Exception e1){
                    logger.warn("账号为"+punch.getUsername()+"发错误提示邮箱异常了..."+e.getMessage());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    logger.warn(baos.toString());
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.warn(e.getLocalizedMessage());
            }
        }
    }

    @Scheduled(cron = "0 10 1 * * ?")
    public void secondPunch(){
        logger.info("第二次打卡开始...");
        logger.info("候选人为");
        for(Punch punch:punches){
            logger.info(punch.getUsername());
        }

        for(Punch punch:punches){
            try {
                logger.info("账号为"+punch.getUsername());
                new AutoPunch(punch).start();
                logger.info("执行完毕...");
                sendEmail(punch, sd.format(System.currentTimeMillis())+"时间第二次打卡成功!");
            } catch (AutoPunchFailedException e) {
                logger.warn("账号为"+punch.getUsername()+"异常了..."+e.getMessage());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(baos));
                logger.warn(baos.toString());
                logger.info("尝试发送提示失败邮箱..");
                sendEmail(punch,sd.format(System.currentTimeMillis())+"异常原因:"+e.getMessage()+"  第二次打卡仍然失败，请手动打卡...");
                logger.info("发送提示失败的邮箱成功!..");
            }catch (Exception e){
                try {
                    logger.warn("账号为"+punch.getUsername()+"异常了..."+e.getMessage());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    logger.warn(baos.toString());
                    logger.info("尝试发送提示失败邮箱..");
                    sendEmail(punch,sd.format(System.currentTimeMillis())+"第二次打卡失败...请手动打卡...");
                    logger.info("发送提示失败的邮箱成功!..");
                }catch (Exception e1){
                    logger.warn("账号为"+punch.getUsername()+"发错误提示邮箱异常了..."+e.getMessage());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    logger.warn(baos.toString());
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.warn(e.getLocalizedMessage());
            }
        }
        punches.clear();
        logger.info("候选人名单已清除");
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
