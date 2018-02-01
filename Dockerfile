FROM maven:3.5-jdk-8-alpine
EXPOSE 8083
COPY . /tmp/xiaon-core/
COPY doc/settings.xml /root/.m2/
WORKDIR /tmp/xiaon-core/
# 设置时区
RUN echo "Asia/Shanghai" > /etc/timezone; 
RUN mvn package -DskipTests
RUN mkdir -p /app/xiaon-core
RUN mv target/xiaon-core-*.jar /app/xiaon-core/xiaon-core.jar
# 删除临时目录
RUN rm -rf /tmp/xiaon-core
WORKDIR /app/xiaon-core/
CMD ["java","-jar","/app/xiaon-core/xiaon-core.jar"]