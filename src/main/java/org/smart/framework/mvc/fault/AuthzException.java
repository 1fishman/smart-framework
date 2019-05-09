package org.smart.framework.mvc.fault;

/**
 * 授权异常 (当权限无效时抛出)
 */
public class AuthzException extends RuntimeException {
    private static final long serialVersionUID = 3561270841352246235L;

    public AuthzException() {
        super();
    }

    public AuthzException(String message) {
        super(message);
    }

    public AuthzException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthzException(Throwable cause) {
        super(cause);
    }
}
