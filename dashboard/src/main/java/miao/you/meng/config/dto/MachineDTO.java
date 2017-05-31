package miao.you.meng.config.dto;

/**
 * Created by Administrator on 2017/4/28.
 *
 * master 和 slave 的dto
 */
public class MachineDTO {

    private String machine;
    private String host;
    private String url;
    private String userName;
    private String password;

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
