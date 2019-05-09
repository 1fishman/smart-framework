package org.smart.framework.mvc.bean;

import org.smart.framework.core.bean.BaseBean;

public class Result extends BaseBean {
    private static final long serialVersionUID = 5317444799424229600L;

    //成功标志
    private boolean success ;
    //错误代码
    private int error;
    //相关数据
    private Object data;

    public Result(Boolean success){
        this.success=success;
    }

    public Result error(int error){
        this.error = error;
        return this;
    }

    public Result data(Object data){
        this.data = data;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
