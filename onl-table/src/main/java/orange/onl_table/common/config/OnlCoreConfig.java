package orange.onl_table.common.config;

import orange.onl_table.common.util.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 解决跨域问题
 */
@Configuration
public class OnlCoreConfig {

    @Bean(name = "idGenerator")
    public IdGenerator idGenerator() {
        return new IdGenerator(1L, 0);
    }

}
