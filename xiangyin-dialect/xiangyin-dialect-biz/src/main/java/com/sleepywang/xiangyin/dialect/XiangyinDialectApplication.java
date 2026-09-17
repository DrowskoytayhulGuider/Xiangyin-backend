package com.sleepywang.xiangyin.dialect;

import com.sleepywang.xiangyin.common.swagger.annotation.EnableOpenApi;
import com.sleepywang.xiangyin.common.security.annotation.EnablePigResourceServer;
import com.sleepywang.xiangyin.common.datasource.annotation.EnableDynamicDataSource;
import com.sleepywang.xiangyin.common.feign.annotation.EnablePigFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableOpenApi("order")        // swagger 文档分组
@EnablePigResourceServer       // 开启资源服务鉴权（想被别人带 token 调就必须有）
@EnableDiscoveryClient         // ← 注册到 Nacos 的关键
@EnableDynamicDataSource       // 用数据库才加
@EnablePigFeignClients         // 要调用其它服务才加
@SpringBootApplication
public class XiangyinDialectApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiangyinDialectApplication.class, args);
    }
}