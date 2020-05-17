package edu.gdpu.entity;


/**
 * @author ilanky
 * @date 2020年 05月16日 05:04:59
 */
public class Info {

    private Integer infoId;

    private String username;

    private String password;

    private String school;

    private String location;

    private String ob;

    private String health;
    private String de;
    private String temp;

    private Integer userId;



    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOb() {
        return ob;
    }

    public void setOb(String ob) {
        this.ob = ob;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Info{" +
                "infoId=" + infoId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", school='" + school + '\'' +
                ", location='" + location + '\'' +
                ", ob='" + ob + '\'' +
                ", health='" + health + '\'' +
                ", de='" + de + '\'' +
                ", temp='" + temp + '\'' +
                ", userId=" + userId +
                '}';
    }
}
