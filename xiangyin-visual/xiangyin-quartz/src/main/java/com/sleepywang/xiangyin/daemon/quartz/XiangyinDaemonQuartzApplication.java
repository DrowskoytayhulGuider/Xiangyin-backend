package com.sleepywang.xiangyin.daemon.quartz;

import com.sleepywang.xiangyin.common.feign.annotation.EnablePigFeignClients;
import com.sleepywang.xiangyin.common.security.annotation.EnablePigResourceServer;
import com.sleepywang.xiangyin.common.swagger.annotation.EnableOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author frwcloud
 * @date 2019/01/23 定时任务模块
 */
@EnableOpenApi("job")
@EnablePigFeignClients
@EnablePigResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class XiangyinDaemonQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiangyinDaemonQuartzApplication.class, args);
	}

}
