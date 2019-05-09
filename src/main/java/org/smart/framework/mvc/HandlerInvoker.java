package org.smart.framework.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler 调用器
 */


public interface HandlerInvoker {
    /**
     * 调用 Handler
     * @param request
     * @param response
     */
    void invokedHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception;
}
