package org.smart.framework.core.impl.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.util.ClassUtil;
import org.smart.framework.util.StringUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 用于获取类的模板类
 */

public abstract class ClassTemplate {
    private static final Logger logger = LoggerFactory.getLogger(ClassTemplate.class);

    protected final String packageName;

    protected ClassTemplate(String packageName){
        this.packageName = packageName;
    }

    /**
     *  获取基础基础包名下的类列表
     * @return
     */
    public final List<Class<?>> getClassList(){
        List<Class<?>> classList = new ArrayList<>();
        try{
            // 获取包名下的文件名文具
            Enumeration<URL> urls = ClassUtil.getClassLoader().getResources(packageName);
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url!=null){
                    //获取协议名(分为file与jar)
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
                        //如果在class目录中,则添加类操作  url中没有空格,所以会用%20代替,这里替换回来
                        String packagePath = url.getPath().replaceAll("%20"," ");
                        addClass(classList,packagePath,packageName);
                    }else if(protocol.equals("jar")){
                        //若在jar包中,则解析jar包中的entry
                        JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()){
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            //判断entry是否为class
                            if(jarEntryName.endsWith(".class")){
                                // 获取类名
                                String className = jarEntryName.substring(0,
                                        jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                doAddClass(classList,className);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("获取类出错!",e);
        }
        return classList;
    }

    private void addClass(List<Class<?>> classList,String packagePath,String packageName){
        try {
            //获取包名路径下的class文件或目录
            File[] files = new File(packagePath).listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return (file.isFile() && file.getName().endsWith(".class"))||
                            file.isDirectory();
                }
            });
            //遍历文件和目录
            for (File file: files){
                String fileName = file.getName();
                if(file.isFile()){
                    // 获取文件的类名字
                    String className = fileName.substring(0,fileName.lastIndexOf("."));
                    //获取类名
                    if(StringUtil.isNotEmpty(packageName)){
                        className = packageName+"."+className;
                    }
                    //执行添加类操作
                    doAddClass(classList,className);
                }else{
                    //获取子包
                    String subPackagePath = fileName;
                    if(StringUtil.isNotEmpty(packagePath)){
                        subPackagePath = packagePath+"/"+subPackagePath;
                    }
                    //子包名
                    String subPackageName = fileName;
                    if(StringUtil.isNotEmpty(packageName)){
                        subPackageName = packageName+"."+subPackageName;
                    }
                    //递归添加类
                    addClass(classList,subPackagePath,subPackageName);
                }
            }
        }catch (Exception e){
            logger.error("添加类出错",e);
        }
    }

    private void doAddClass(List<Class<?>> classList,String className){
        Class<?> cls = ClassUtil.loadClass(className,false);
        if(checkAddClass(cls)){
            classList.add(cls);
        }
    }

    /**
     * 自己实现,验证是否允许添加类
     * @param cls
     * @return
     */
    public abstract boolean checkAddClass(Class<?> cls);
}
