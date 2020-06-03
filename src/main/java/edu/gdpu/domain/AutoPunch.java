package edu.gdpu.domain;

import edu.gdpu.exception.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 嘿 林梓鸿
 * @date 2020年 05月16日 15:16:12
 * 自动打卡
 */
public class AutoPunch {
    public static final Logger LOGGER = LoggerFactory.getLogger(AutoPunch.class);
    public static final String LOGIN_URL = "http://eswis.gdpu.edu.cn/login.aspx";
    public static final String USER_AGENT = "User-Agent";
    public static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36";
    private String submitUrl;
    private Map<String,String> cookies = new HashMap<>();
    private Punch punch;
    /**
     * 提交事件，不可更改
     */
    public static final String SUBMIT= "提交保存";





    /**
     * 第一次访问获取其Cookie
     * @param username
     * @param password
     * @return
     */
    public Map<String,String> firstVisit(String username,String password) throws  IOException {
        //获取连接
        Connection con = Jsoup.connect(LOGIN_URL);

        // 配置模拟浏览器USER_AGENT
        con.header(USER_AGENT, USER_AGENT_VALUE);


        Connection.Response rs = null;

        try {
            rs = con.timeout(60000).execute();
        } catch (IOException e) {
            LOGGER.warn("第一次访问获取其Cookie时出异常,账户为"+punch.getUsername());
            LOGGER.warn(e.getLocalizedMessage());
            throw new IOException(e);
        }
        Document document = Jsoup.parse(rs.body());
        Map<String,String> map = new HashMap<>();
        Elements input = document.getElementsByTag("input");
        String name = null;

        //获取input组件的值并封装好post请求的data
        for(Element inp:input){
            name = inp.attr("name");
            if(name.contains("username")){
                map.put(name,username);
            }
            else if(name.contains("password")){
                map.put(name,password);
            }
            else if(name.equals("__EVENTTARGET")){
                map.put(name,"logon");
            }
            else {
                map.put(name,inp.val());
            }
        }
        //封装cookie
        cookies.putAll(rs.cookies());
        return map;
    }

    /**
     * 登陆
     * @param map
     */
    public void login(Map<String,String> map) throws LoginException,  IOException {
        Connection con = Jsoup.connect(LOGIN_URL);
        con.header(USER_AGENT, USER_AGENT_VALUE);
        con.method(Connection.Method.POST);
        Connection.Response login = null;
        Element punch = null;
        con.header("Content-Type","application/x-www-form-urlencoded");
        try {
            login = con.ignoreContentType(true).followRedirects(true).timeout(60000)
                    .data(map).cookies(cookies).execute();

            //找到健康打卡这个节点获取其Href
            Document parse = login.parse();
            punch = parse.select("a:contains(健康打卡)").first();
            submitUrl = punch.attr("href");

            //更新Cookie
            cookies.putAll(login.cookies());
        } catch (NullPointerException e){
            throw new LoginException("登陆时出现异常");
        }catch(IOException e) {
            LOGGER.warn("登陆时出异常,账户为"+this.punch.getUsername());
            LOGGER.warn(e.getLocalizedMessage());
            throw new IOException(e);
        }
    }

    /**
     * 健康打卡(点开始填报那个页面)
     * 获取进入健康打卡页面的验证参数
     * @return
     */
    public Map<String,String> submitStart() throws IOException {
        Connection connect = Jsoup.connect("http://eswis.gdpu.edu.cn/"+submitUrl);
        connect.header(USER_AGENT, USER_AGENT_VALUE);
        connect.method(Connection.Method.GET);
        HashMap<String,String> da = new HashMap<>();
        connect.header("Content-Type","application/x-www-form-urlencoded");
        try {
            Document document1 = connect.cookies(cookies).timeout(60000).get();
            Elements input1 = document1.getElementsByTag("input");
            for(Element element:input1){
                String name1 = element.attr("name");
                if(name1.equals("ctl00$cph_right$e_ok")){
                    da.put(name1,"on");
                }else {
                    da.put(name1,element.val());
                }
            }
        } catch (IOException e) {
            LOGGER.warn("健康打卡(点开始填报那个页面)出异常,账户为"+this.punch.getUsername());
            LOGGER.warn(e.getLocalizedMessage());
            throw new IOException(e);
        }

        return da;
    }

    /**
     * 进入填报信息页面
     * 获取最终提交的验证参数
     * @param map
     * @return
     */
    public Map<String,String> enterSubmit(Map<String,String> map) throws IOException {
        Connection connect = Jsoup.connect("http://eswis.gdpu.edu.cn/"+submitUrl);
        connect.header(USER_AGENT, USER_AGENT_VALUE);
        connect.method(Connection.Method.POST);
        connect.header("Content-Type","application/x-www-form-urlencoded");
        Map<String,String> data = new HashMap<>();
        try {
            Connection.Response response1 = connect.ignoreContentType(true).timeout(60000).followRedirects(true)
                    .data(map).cookies(cookies).execute();
            Document parse = response1.parse();
            Elements input = parse.getElementsByTag("input");
            for(Element element:input){
                String name1 = element.attr("name");
                data.put(name1,element.val());
            }
        } catch (IOException e) {
            LOGGER.warn("进入填报信息页面出异常,账户为"+this.punch.getUsername());
            LOGGER.warn(e.getLocalizedMessage());
            throw new IOException(e);
        }
        return data;
    }

    /**
     * 最终提交
     * @param map
     */
    public void submit(Map<String,String> map) throws AutoPunchFailedException, IOException {
        Connection connect = Jsoup.connect("http://eswis.gdpu.edu.cn/"+submitUrl);
        connect.header(USER_AGENT, USER_AGENT_VALUE);
        connect.method(Connection.Method.POST);
        connect.header("Content-Type","application/x-www-form-urlencoded");


        Map<String,String> data = new HashMap<>();

        //验证参数值
        data.put("__LASTFOCUS",map.get("__LASTFOCUS"));
        data.put("__VIEWSTATEGENERATOR",map.get("__VIEWSTATEGENERATOR"));
        data.put("__EVENTARGUMENT",map.get("__EVENTARGUMENT"));
        data.put("__VIEWSTATE",map.get("__VIEWSTATE"));
        data.put("__EVENTVALIDATION",map.get("__EVENTVALIDATION"));
        data.put("__EVENTTARGET",map.get("__EVENTTARGET"));

        //填报数据
        data.put("ctl00$cph_right$e_atschool", punch.getSchool());
        data.put("ctl00$cph_right$e_location",punch.getLocation());
        data.put("ctl00$cph_right$e_observation",punch.getOb());
        data.put(punch.getHealth(),"on");
        data.put("ctl00$cph_right$e_temp",punch.getTemp());
        data.put("ctl00$cph_right$e_describe",punch.getDe());
        data.put("ctl00$cph_right$e_submit",SUBMIT);
        try {
            Connection.Response response = connect.ignoreContentType(true).timeout(90000).followRedirects(true)
                    .data(data).cookies(cookies).execute();
            if(response.statusCode()!=200){
                throw new AutoPunchFailedException("自动打卡异常,状态码为"+response.statusCode());
            }
        } catch (IOException e) {
            LOGGER.warn("最终提交出异常,账户为"+this.punch.getUsername());
            LOGGER.warn(e.getLocalizedMessage());
            throw new IOException(e);
        }
    }


    public void autoPunch() throws Exception {
        Map<String, String> stringStringMap = firstVisit(punch.getUsername(),punch.getPassword());
        login(stringStringMap);
        Map<String, String> map = submitStart();
        Map<String, String> data = enterSubmit(map);
        submit(data);
    }

    public void start() throws Exception {
        autoPunch();
    }

    public AutoPunch(Punch punch) {
        this.punch = punch;
    }
}
