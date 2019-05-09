package org.smart.framework.mvc.fault;

/**
 * mybatis 异常处理
 */
public class MybatisException extends RuntimeException {
    private static final long serialVersionUID = 3561212331352246235L;

    public MybatisException() {
        super();
    }

    public MybatisException(String message) {
        super(message);
    }

    public MybatisException(String message, Throwable cause) {
        super(message, cause);
    }

    public MybatisException(Throwable cause) {
        super(cause);
    }
}
