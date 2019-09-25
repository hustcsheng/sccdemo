package scc.server.bean;


import org.apache.commons.lang3.StringUtils;

/**
 * 用户信息类
 */
public class SCCUser {

    private transient  long uniqueId;  //不参与序列化

    private String userName;

    private String  lastLoginDateTime;

    public SCCUser(String userName, String lastLoginDate, String lastLoginTime) {
        this.userName = userName;
        if(StringUtils.isAnyEmpty(lastLoginDate,lastLoginTime)){
            this.lastLoginDateTime = null;
        }else{
            this.lastLoginDateTime = lastLoginDate + "" + lastLoginTime;
        }

    }

    public SCCUser() {
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastLoginDateTime() {
        return lastLoginDateTime;
    }

    public void setLastLoginDateTime(String lastLoginDateTime) {
        this.lastLoginDateTime = lastLoginDateTime;
    }
}
