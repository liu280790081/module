package orange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCaching
@EnableAsync
@EnableSwagger2
@SpringBootApplication
public class OnlTableApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(OnlTableApp.class, args);
    }
}
