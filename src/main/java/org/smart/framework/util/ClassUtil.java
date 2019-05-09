package org.smart.framework.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mysql.fabric.FabricCommunicationException;

import java.net.URL;

public class ClassUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    public static String getClassPath(){
        String classpath = "";
        URL resource = getClassLoader().getResource("");
        if(resource != null){
            classpath = resource.getPath();
        }
        return classpath;
    }

    /**
     * 获取类加载器
     * 直接获取当前线程上下文加载器
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类(将自动初始化) 这里的自动初始化指的是是否执行静态代码块中的代码
     * @param className
     * @return
     */

    public static Class<?> loadClass(String className){
        return loadClass(className,true);
    }

    /**
     * 加载类
     * @param className
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> clazz;
        try{
            clazz = Class.forName(className,isInitialized,getClassLoader());
        }catch (ClassNotFoundException e){
            logger.error("加载类"+className+"出错!",e);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    public static boolean isInt(Class<?> type){
        return type.equals(int.class)||type.equals(Integer.class);
    }

    public static boolean isLong(Class<?> type){
        return type.equals(long.class)||type.equals(Long.class);
    }

    public static boolean isDouble(Class<?> type){
        return type.equals(double.class)||type.equals(Double.class);
    }
    public static boolean isString(Class<?> type){
        return type.equals(String.class);
    }

/*
    */
/**
     * 获取指定包名下的所有类
     * @param packageName
     * @return
     *//*

    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<>();
        try{
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url!=null){
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
                        String packagePath = url.getPath().replaceAll("%20"," ");
                        addClass(classSet,packagePath,packageName);
                    }else if(protocol.equals("jar")){
                        JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                        if(jarURLConnection != null ){
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if(jarFile != null){
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while(jarEntries.hasMoreElements()){
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if(jarEntryName.endsWith(".class")){
                                        String className = jarEntryName.substring(0,
                                                jarEntryName.lastIndexOf(".")).
                                                replaceAll("/",".");
                                        doAddClass(classSet,className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("get class set failure",e);
            e.printStackTrace();
        }
        return classSet;
    }

    */
/**
     * 将包路径下的包中的class加载到set集合中
     * @param classSet
     * @param packagePath
     * @param packageName
     *//*

    public static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class"))||file.isDirectory();
            }
        });
        for(File file : files){
            String fileName = file.getName();
            if(file.isFile()){
                String className = fileName.substring(0,fileName.lastIndexOf("."));
                if(StringUtil.isNotEmpty(packageName)){
                    className = packageName+"."+className;
                    doAddClass(classSet,className);
                }
            }else{
                String subPackagePath = fileName;
                if(StringUtil.isNotEmpty(packagePath)){
                    subPackagePath = packagePath+"/"+subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtil.isNotEmpty(packageName)){
                    subPackageName = packageName+"."+subPackageName;
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }

    public static void doAddClass(Set<Class<?>> classSet , String className){
        Class<?> cls = loadClass(className,false);
        classSet.add(cls);
    }
*/


}
