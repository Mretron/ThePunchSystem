package com.yrmjhtdjxh.punch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.yrmjhtdjxh.punch.mapper")
public class PunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PunchApplication.class, args);
    }

}
