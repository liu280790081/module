package orange.elasticsearch.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@NoArgsConstructor
public class ESTestParam {

    /**
     * uri
     */
    @NotBlank
    private String uri;

    /**
     * 请求方式
     */
    @NotBlank
    private String method;

    /**
     * 排序
     */
    private int sort;

    /**
     * 参数
     */
    private Map<String, String> paramMap;

    public ESTestParam(@NotBlank String uri, @NotBlank String method, int sort) {
        this.uri = uri;
        this.method = method;
        this.sort = sort;
    }

    public ESTestParam(@NotBlank String uri, @NotBlank String method, Map<String, String> paramMap) {
        this.uri = uri;
        this.method = method;
        this.paramMap = paramMap;
    }
}
