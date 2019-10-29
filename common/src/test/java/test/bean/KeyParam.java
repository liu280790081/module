package test.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyParam {

    /**
     * uri
     */
    private String uri;

    /**
     * 请求方式
     */
    private String method;

    public KeyParam(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }


}
