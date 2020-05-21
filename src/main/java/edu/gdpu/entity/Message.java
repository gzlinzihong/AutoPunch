package edu.gdpu.entity;

/**
 * @author ilanky
 * @date 2020年 05月20日 22:37:56
 */
public class Message {
    private int id;
    private String content;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
