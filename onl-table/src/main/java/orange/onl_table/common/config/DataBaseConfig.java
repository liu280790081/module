package orange.onl_table.common.config;

import lombok.Data;
import orange.onl_table.common.exception.OnlException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.master")
public class DataBaseConfig {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    private String dbName;
    private String schema;
    private String dialect;

    public void setUrl(String url) {
        this.url = url;
        this.schema = url.contains("?") ? url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?")) : url.substring(url.lastIndexOf("/") + 1);
        this.dbName = url.split(":")[1];
        this.dialect = dialect(dbName);
    }

    private String dialect(String database) {
        switch (database) {
            case "mysql":
                return "org.hibernate.dialect.MySQL5InnoDBDialect";
            case "oracle":
                return "org.hibernate.dialect.OracleDialect";
            case "postgresql":
                return "org.hibernate.dialect.PostgreSQL82Dialect";
            case "sqlserver":
                return "org.hibernate.dialect.SQLServerDialect";
            default:
                throw new OnlException("此数据库方言不存在");
        }
    }

}
