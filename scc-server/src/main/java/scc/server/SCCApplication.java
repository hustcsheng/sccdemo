package scc.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("scc.server.dao")
public class SCCApplication {

    public static void main(String[] args) {
        SpringApplication.run(SCCApplication.class,args);
    }
}
