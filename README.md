# xiaoyun-core
小云工作室的后端项目，只处理后端业务逻辑，并提供接口给前端调用，基于spring boot开发。前端项目为[xiaoyun-web](http://git.oschina.net/xiaoyun_studio/xiaoyun-web)

### 使用教程
1. 使用git或者svn下载源码，也可以在本页面直接下载zip压缩包
2. 命令行中进入项目根目录，然后执行命令： mvn spring-boot:run 。maven会自动下载jar，编译源码，然后在内置的tomcat中运行项目。项目的所有配置都提供了默认值，
不需要修改就可以运行。数据库默认使用h2，系统启动时会自动创建数据库、表，自动导入用户、权限等初始数据。
3. 浏览器输入http://localhost:8083  就可以看到首页


由于maven中央仓库速度很慢，分享一个阿里巴巴的maven仓库，速度非常快。在线搜索地址：[maven.aliyun.com](http://maven.aliyun.com/nexus/),
仓库地址：http://maven.aliyun.com/nexus/content/groups/public/ ，修改仓库地址的方法：找到C:\Users\用户名\ .m2\setting.xml，将文件中注释掉的mirrors取消注释，
并修改为：
```
<mirrors>
    <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
</mirrors>
```

