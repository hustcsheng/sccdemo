package scc.server.bean;

/**
 * 返回数据格式
 */
public class RequestResult {

    /**
     * 返回状态
     */
    protected String status;

    /**
     * 错误消息
     */
    protected String errMsg;

    /**
     * 实际返回数据
     */
    protected Object data;



    public RequestResult() {

    }


    public RequestResult(String status, String errMsg, Object data) {
        this.status = status;
        this.errMsg = errMsg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
