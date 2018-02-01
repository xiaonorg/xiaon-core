# xiaon-core
“小恩的世界”的后端项目，只处理后端业务逻辑，并提供接口给前端调用，基于spring boot开发。前端项目为[xiaon-web](https://github.com/xiaonorg/xiaon-core)

## 使用教程
1. 使用git下载源码，也可以在本页面直接下载zip压缩包
2. 命令行中进入项目根目录，然后执行命令： mvn spring-boot:run 。maven会自动下载jar，编译源码，然后在内置的tomcat中运行项目。项目的所有配置都提供了默认值，
不需要修改就可以运行。数据库默认使用h2，系统启动时会自动创建数据库、表，自动导入用户、权限等初始数据。
3. 浏览器输入localhost:8083 ,就可以看到首页