package cn.com.bonc.sce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

/**
 * @author Leucippus
 * @version 0.1
 * @since 2018/12/11 11:19
 */
@Slf4j
@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
public class DataAccessApplication {

    public static void main( String[] args ) {
        SpringApplication.run( DataAccessApplication.class, args );
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
