package org.smart.framework.mvc;

/**
 * 处理器映射
 */
public interface HandlerMapping {

    /**
     * 获取Handler
     * @param currentRequestMethod
     * @param currentRequestPath
     * @return
     */
    Handler getHandler(String currentRequestMethod, String currentRequestPath);
}
