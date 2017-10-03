package wj.com.socketchat;

/**
 * Created by wj on 2017/9/29.
 */

public class Msg {
    private String msg;
    private String ip;
    private int type;//1 left  0 right 2 system

    public Msg(String msg, int type,String ip) {
        this.msg = msg;
        this.type = type;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
