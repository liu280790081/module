package orange.onl_table.common.engine;


import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class FreemarkerHelper {

    private FreemarkerHelper() {}

    public static final String TABLE_TEMPLATE = "com/ido85/icip/system/table/common/engine/tableTemplate.ftl";

    private static Configuration b;

    static {
        b = new Configuration(Configuration.VERSION_2_3_28);
        b.setNumberFormat("0.#####################");
        b.setClassForTemplateLoading(FreemarkerHelper.class, "/");
    }

    public static String generate(String templatePath, String charset, Map<String, Object> param) {
        try {
            Template var4 = b.getTemplate(templatePath, charset);
            StringWriter var3 = new StringWriter();
            var4.process(param, var3);
            return var3.toString();
        } catch (Exception var5) {
            log.error(var5.getMessage(), var5);
            return var5.toString();
        }
    }

    public static String generateByUTF8(String templatePath, Map<String, Object> param) {
        return generate(templatePath, "utf-8", param);
    }

}

