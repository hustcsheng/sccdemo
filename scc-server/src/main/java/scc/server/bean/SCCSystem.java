package scc.server.bean;

/**
 * 子系统信息
 */
public class SCCSystem {

    private int sysId;

    private String sysName;


    public SCCSystem(int sysId, String sysName) {
        this.sysId = sysId;
        this.sysName = sysName;
    }

    public SCCSystem() {
    }

    public int getSysId() {
        return sysId;
    }

    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
}
