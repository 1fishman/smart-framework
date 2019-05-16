## 总体描述
smart-framework是一个小型的web框架,集成了 Ioc AOP,事务处理与数据库封装等特性.     
此项目参考的开源中国上黄勇老师的smart-framework框架, 自己也跟着黄勇老师也做了一个. 实现了仅有其中的一部分功能.另外也集成了一下mybatis框架,数据库可以不用自己的api.

## 基本配置 
首先要在classpath路径下新建一个名叫smart.properties的文件，里面包含如下属性.
~~~
app.name=smart-sample                       //表示app的名字
smart.framework.app.base_package=sample     // 基础包名，也就是项目源码的基础包名
smart.framework.app.jsp_path=/WEB-INF/jsp/  // jsp的路径
smart.framework.app.home_page=/index.jsp    // 首页的路径

数据库连接
smart.framework.jdbc.driver=com.mysql.jdbc.Driver
smart.framework.jdbc.url=jdbc:mysql://localhost:3306/
smart.framework.jdbc.username=root
smart.framework.jdbc.password=

对于mybatis支持:
mybatis.switch=true
mybatis.configLocation=/config
~~~ 
配置完成后就可以新建项目包了。

## AOP 概述 
aop 主要实现了代理类.实现的是链式代理.实现原理:
首先代理类需要继承自AspectProxy类. 在初始化的时候就会扫描包下面的所有的类,找到所有继承子AspectProxy的类.然后在根据继承的这些类找到他们需要代理的类.代理类中需要声明需要代理的对象,通过注解来实现.Aspect注解,注解中有三个参数:
~~~java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect{
    /**
     * 包名  代理包名下的所有类
     * @return
     */
    String pkg() default "";

    /**
     * 类名  ,代理此类
     */
    String cls() default "";

    /**
     * 注解, 代理有此注解的包名下的类
     */
    Class<? extends Annotation> annotation() default Aspect.class;

}
~~~
可以看到三个参数都有默认值,具体流程下面代码:
~~~java
private static List<Class<?>> createTargetClassList(Aspect aspect) throws Exception{
        List<Class<?>> targetClassList = new ArrayList<>();
        // 获取 Aspect 注解的相关属性
        String pkg = aspect.pkg();
        String cls = aspect.cls();
        Class<? extends Annotation> annotation = aspect.annotation();
        //如果包名不为空,则需要进一步判断类名是否为空
        if(StringUtil.isNotEmpty(pkg)){
            if (StringUtil.isNotEmpty(cls)){
                //如果类名不为空,则仅仅添加该类
                targetClassList.add(ClassUtil.loadClass(pkg+"."+cls,false));
            }else{
                    //如果注解不为空并且不是Aspect注解,则添加指定包名下带有该注解的所有类
                if(annotation != null && !annotation.equals(Aspect.class)){
                    targetClassList.addAll(classScanner.getClassListByAnnotation(pkg,annotation));
                }else{
                    //添加该包名下的所有类
                    targetClassList.addAll(classScanner.getClassList(pkg));
                }
            }
        }else{
            // 若注解不为空,并且不是Aspect注解,则添加应用包名下带有该注解的所有类
            if(annotation != null && !annotation.equals(Aspect.class)){
                targetClassList.addAll(ClassHelper.getclassListByAnnotation(annotation));
            }
        }
        return targetClassList;
    }
~~~
在这里添加代理类的就是首先看包名,其次看类名和注解.   
项目中实现的是链式代理.通过一个链来进行代理,也 提供了一个AspectOrder注解来指明代理顺序.

大体流程: 首先扫描所有继承自AspectProxy的类,然后从这里类的注解上面提取出他们要代理的目标类.然后在排序这里所有类.根据AspectOrder注解或者名字.然后在将这个代理类到代理目标类列表的映射转换成目标类到代理类的映射.最后,将Bean容器中的目标类转换成代理类.

## Ioc概述 
流程: 扫描所有指定包下的类,将包下的所有类都注入到bean容器中,这里调用的是所有的类的默认构造方法,属性值都为初始值.随后开始进行依赖注入,扫描Bean容器中的所有实例,寻找标注了Inject注解的字段,然后通过类型在Bean容器中找到此类型的类实例,将此实例注入到此类实例中,知道扫描完所有的类位置.   
具体实现请看代码中Ioc模块.

## 数据库事务的实现:
事务实现也是通过代理来实现的.定义了事务代理类, 当一个类如果被标记了@Service注解,并且方法上有Transaction注解的话,将会进行代理进行事务管理.这里只实现了事务重入,当一个方法进入了事务处理,其中处理的所有方法,包括调用的非事务方法也都将会包含在事务中.事务的具体实现.
~~~java
public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = flagContainer.get();
        Method method = proxyChain.getTargetMethod();
        //若当前线程未进行事务处理,并且在目标方法上定义了Transaction注解,则说明该方法需要进行事务处理
        if(!flag && method.isAnnotationPresent(Transaction.class)){
            //设置当前线程已经进行事务处理
            flagContainer.set(true);
            try{
                //开启事务
                DatabaseHelper.beginTransaction();
                logger.debug("[Smart] begin transaction");
                //执行目标方法
                result = proxyChain.doProxyChain();
                //提交事务
                DatabaseHelper.commitTransaction();
                logger.debug("[Smart] commit transaction");
            }catch (Exception e){
                DatabaseHelper.rollbackTransaction();
                logger.debug("[Smart] rollback transaction");
                throw e;
            }finally {
                //移除线程的局部变量
                flagContainer.remove();
            }
        }else{
            result = proxyChain.doProxyChain();
        }
        return result;
    }
~~~

## DB方面
DB中实现了自己的一套ORM框架,当然也集成了Mybatis框架,如果需要使用Mybatis框架的话,需要引入相关依赖包.然后再建立mybatis-config.xml文件.在smart.properties文件中指定mybatis.switch=true,使用mybatis,然后在设置mybatis-config.xml文件的路径.如果在resources目录下,可以不指定,如果不在,需要手动制定mybatis.configLocaltion的位置.
如果使用Mybatis需要在Mapper接口中使用@SMapper注解来表明是一个mapper接口,SMapper主要是为了和Mybatis区分开.

集成Mybatis的这方面主要也是使用代理来实现.
首先通过配置文件获得一个SqlSessionFactory.
之后在通过为Dao层创建一个代理,来实现依赖注入后每次调用使用不同的SqlSession.
代理的具体实现:
~~~java
@Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Method targetMethod = proxyChain.getTargetMethod();
        Class<?> targetClass = proxyChain.getTargetClass();
        // 如果是事务,需要有异常回滚
        if (targetMethod.isAnnotationPresent(Transaction.class)){
            SqlSessionFactory sqlSessionFactory = MybatisHelper.getSqlSessionFactory();
            // 新建一个SqlSession
            SqlSession sqlSession = sqlSessionFactory.openSession();
            try {
                // 获取Mapper类实例
                Object targetMapper = sqlSession.getMapper(targetClass);
                // 唤醒方法.
                Object result = targetMethod.invoke(targetMapper,proxyChain.getMethodParams());
                return result;
            }catch (Exception e){
                // 出现异常则回滚
                sqlSession.rollback();
                sqlSession.close();
                throw new MybatisException(e);
            }
        }else{
            // 非事务类型直接提交
            try{
                SqlSessionFactory sqlSessionFactory = MybatisHelper.getSqlSessionFactory();
                SqlSession sqlSession = sqlSessionFactory.openSession(false);
                Object targetMapper = sqlSession.getMapper(targetClass);
                Object result = targetMethod.invoke(targetMapper,proxyChain.getMethodParams());
                sqlSession.commit();
                sqlSession.close();
                return result;
            }catch (Exception e){
                throw new MybatisException(e);
            }

        }
    }
~~~

## MVC控制
有一个核心的DispatchServlet.通过匹配得到路径来得到响应的Handler.通过Handler来唤醒响应的方法.有两种返回格式.
View和Result格式,通过向View设置jsp页面来返回页面数据.View.addModel()想页面中添加请求数据.

### **实现思路** 
Handler用来封装 Action 和方法的相关信息.包含有请求路径和关联的方法. 在保存一个Map来保存Requester到Handler的映射.
~~~java
public class Handler {
    // handler 类型信息,备注了注解的类
    private Class<?> actionClass;
    // 对应的 Action 方法
    private Method actionMethod;
    private Matcher requestPathMatcher;

    public Handler(Class<?> actionClass,Method actionMethod){
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public Method getActionMethod(){
        return actionMethod;
    }

    public Matcher getRequestPathMatcher(){
        return requestPathMatcher;
    }

    public void setRequestPathMatcher(Matcher requestPathMatcher){
        this.requestPathMatcher = requestPathMatcher;
    }

}
~~~
Requester封装请求的方法和路径.
~~~java
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
~~~

再通过中央转发器来实现处理请求.通过判断返回的对象是View类型或者Result类型来处理不同的请求.如果是View请求,则需要转发请求,如果是Result则向请求回写数据.

~~~java
protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求编码方式
        req.setCharacterEncoding(FrameworkConstant.UTF_8);
        //获取当前请求相关数据
        String currentRequestMethod = req.getMethod();
        String currentRequestPath = WebUtil.getRequestPath(req);
        logger.debug("[Smart]{}:{}",currentRequestMethod,currentRequestPath);

        //将"/" 请求重定向到首页
        if(currentRequestPath.equals("/")){
            WebUtil.redirectRequest(FrameworkConstant.HOME_PAGE,req,resp);
        }

        //去掉当前请求路径的"/"
        if(currentRequestPath.endsWith("/")){
            currentRequestPath = currentRequestPath.substring(0,currentRequestPath.length()-1);
        }
        //获取 Handler
        Handler handler = handlerMapping.getHandler(currentRequestMethod,currentRequestPath);

        //如果没有找到对应的 Action ,则跳转到 404页面
        if(handler == null){
            WebUtil.sendError(HttpServletResponse.SC_NOT_FOUND,"",resp);
            return;
        }
        //初始化 DataContext
        DataContext.init(req,resp);
        try {
            // 这里实现安全检查,不用filter实现,只是让用户在发现没有权限的时候抛出异常,到这里调用
            // 调用 Handler的 处理方法, 处理方法中添加逻辑
            handlerInvoker.invokedHandler(req,resp,handler);
        } catch (Exception e) {
            // 处理 Action 异常
            handlerExceptionResolver.resolveHandlerException(req,resp,e);
        }finally {
            DataContext.destroy();
        }
    }
~~~



## 具体使用.
参看同目录下的Sample包.
几个包必须有
- @Inject是依赖注入功能
- @Bean是设置设置Bean容器
- @Service启动事务管理,在包上注解
- @Transaction在方法上使用,启动事务功能
- @Request.Get()/Post()方法是用来匹配get/post请求的.value匹配路径.支持restful风格

