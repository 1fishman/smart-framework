package org.smart.framework.mvc;

/**
 * 封装 HTTP 请求,包括请求类型和请求路径路径
 */
public class Requester {
    private String requestMethod;
    private String requestPath;

    public Requester(String requestMethod,String requestPath){
        this.requestMethod=requestMethod;
        this.requestPath=requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }
}
