package edu.gdpu.domain;

/**
 * @author ilanky
 * @date 2020年 05月16日 04:35:10
 */
public class Respon {

    private String msg;

    private int status;

    private Object object;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Respon{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", object=" + object +
                '}';
    }
}
