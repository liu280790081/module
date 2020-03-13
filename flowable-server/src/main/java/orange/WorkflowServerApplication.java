package orange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 入口启动类
 *
 * @author lishou
 */
@EnableCaching
@EnableAsync
@SpringBootApplication
public class WorkflowServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkflowServerApplication.class, args);
    }
}
