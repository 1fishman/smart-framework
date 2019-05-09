package org.smart.framework.core.impl.support;

public abstract class SupperClassTemplate extends ClassTemplate {

    protected final Class<?> superClass;

    protected SupperClassTemplate(String packageName,Class<?> superClass) {
        super(packageName);
        this.superClass = superClass;
    }

}
