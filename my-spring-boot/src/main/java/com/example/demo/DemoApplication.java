package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

//项目启动注解
@SpringBootApplication
//@ServletComponentScan使用该注解后，Servilet、Filter、Listener可以直接通过@WebServlet、@WebFilter、@WebListener注解自动注册，无须其它代码
@ServletComponentScan
//导入资源配置文件，让spring boot可以读取到 类似于<import标签>
@ImportResource(locations={"classpath:spring-mvc.xml"})
//开启异步调用
@EnableAsync
public class DemoApplication {

	public static void main(String[] args) {
		//应用程序开始的运行方法
		SpringApplication.run(DemoApplication.class, args);

	}
}
