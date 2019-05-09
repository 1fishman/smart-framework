package org.smart.framework.mvc.fault;

/**
 * 认证异常 (当非法访问时抛出)
 */
public class AuthcException  extends RuntimeException{

    private static final long serialVersionUID = 8581263966198380409L;

    public AuthcException(){
        super();
    }

    public AuthcException(String message){
        super(message);
    }

    public  AuthcException(String message,Throwable cause){
        super(message,cause);
    }

    public AuthcException(Throwable cause){
        super(cause);
    }
}
