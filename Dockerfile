FROM maven:3.5-jdk-8-alpine 
MAINTAINER xiaoyun(xiaoyun-sutdio@foxmail.com)
EXPOSE 8083
COPY . /tmp/xiaoyun-core/
COPY doc/settings.xml /root/.m2/
WORKDIR /tmp/xiaoyun-core/
# 设置时区
RUN echo "Asia/Shanghai" > /etc/timezone; 
RUN mvn package -DskipTests
RUN mkdir -p /app/xiaoyun-core
RUN mv target/xiaoyun-core-*.jar /app/xiaoyun-core/xiaoyun-core.jar
# 删除临时目录
RUN rm -rf /tmp/xiaoyun-core 
WORKDIR /app/xiaoyun-core/
CMD ["java","-jar","/app/xiaoyun-core/xiaoyun-core.jar"]