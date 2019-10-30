package orange.onl_table.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(
        prefix = "spring.datasource.dynamic.datasource.master"
)
public class DataBaseConfig {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    private String dbName;
    private String schema;

    public void setUrl(String url) {
        this.url = url;
        this.schema = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
        this.dbName = url.split(":")[1];
    }

}
