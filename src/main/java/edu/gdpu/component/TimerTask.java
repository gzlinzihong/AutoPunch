package edu.gdpu.component;

import edu.gdpu.domain.AutoPunch;

import edu.gdpu.domain.Punch;
import edu.gdpu.exception.LoginException;
import edu.gdpu.mapper.InfoMapper;
import edu.gdpu.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ilanky
 * @date 2020年 05月16日 10:11:04
 */
@Component
public class TimerTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static List<Punch> punches = new ArrayList<>();

    private static List<String> fails = new ArrayList<>();


    private static int successCount = 0;
    private static int failCount = 0;
    private static int count = 0;



    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private InfoMapper infoMapper;

    @Autowired
    private UserMapper userMapper;

    private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "0 0 1 * * ?")
    public void autoPunch(){
        logger.info("定时任务开始...");
        List<Punch> all = infoMapper.findAll();
        count = all.size();
        for(Punch punch:all){
            try {
                logger.info("账号为"+punch.getUsername());
                new AutoPunch(punch).start();
                logger.info("执行完毕...");
                sendEmail(punch, sd.format(System.currentTimeMillis())+"时间打卡成功!");
                successCount++;
            }catch (Exception e){
                if(e instanceof LoginException){
                    if(fails.contains(punch.getUsername())){
                        infoMapper.delete(punch);
                        sendEmail(punch,"自动打卡失败....由于连续两次因为账号密码错误导致自动打卡失败,系统已将你的信息删除。若想使用自动打卡。请登陆你注册的账号重新输入信息");
                        fails.remove(punch);
                    }else {
                        fails.add(punch.getUsername());
                        sendEmail(punch,"自动打卡失败..请手动打卡...原因:登陆时账号密码不正确。请确认账号密码是否正确，若仍然正确可回复此邮箱告知。若下次打卡仍然失败，则会将你移出自动打卡名单里");
                    }
                    failCount++;
                }else {
                    logger.warn(getExceptionDetail(e));
                    punches.add(punch);
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.warn(e.getLocalizedMessage());
            }
        }
        secondPunch();
    }

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
                sendEmail(punch, sd.format(System.currentTimeMillis())+"时间打卡成功!");
                successCount++;
            } catch (Exception e){
                logger.warn(getExceptionDetail(e));
                sendEmail(punch,"第二次打卡仍然失败...请手动打卡...");
                failCount++;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.warn(e.getLocalizedMessage());
            }
        }
        punches.clear();
        logger.info("候选人名单已清除");
        logger.info("今日自动打卡"+count+"人,成功了"+ successCount +"，失败了"+ failCount);
        successCount = 0;
        failCount = 0;
        count = 0;
    }

    public void sendEmail(Punch punch,String msg){
        int count = 0;
        while (count<5){
            try{
                SimpleMailMessage m=new SimpleMailMessage();
                m.setFrom("lzhooslq@outlook.com");
                System.out.println(userMapper.findById(punch.getUserId()).getEmail());
                m.setTo(userMapper.findById(punch.getUserId()).getEmail());
                m.setText(msg);
                m.setSubject("打卡任务提醒");//主题
                javaMailSender.send(m);
                break;
            }catch (Exception e){
                logger.warn(getExceptionDetail(e));
                count++;
            }
        }

        if(count>=5){
            logger.error(userMapper.findById(punch.getUserId()).getEmail()+"尝试发送邮箱失败....");
        }

    }

    public String getExceptionDetail(Throwable e){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }
}
