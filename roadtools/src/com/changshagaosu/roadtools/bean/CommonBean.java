package com.changshagaosu.roadtools.bean;

/**
 * @Annotation <p>描述</p>
 * @Auth Sunny
 * @date 2017/9/9
 * @Version V1.0.0
 */

public class CommonBean {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
