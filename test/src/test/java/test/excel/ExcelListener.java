package test.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelListener extends AnalysisEventListener<Object> {

    @Override
    public void invoke(Object object, AnalysisContext analysisContext) {
        log.info("aaaa {}", object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
