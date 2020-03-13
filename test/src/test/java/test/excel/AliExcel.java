package test.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AbstractIgnoreExceptionReadListener;
import com.alibaba.excel.event.SyncReadListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.util.*;

@Slf4j
public class AliExcel {

    /**
     * 最简单的读
     * <p>1. 创建excel对应的实体对象 参照{@link }
     * <p>2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>3. 直接读即可
     */
    @Test
    public void simpleRead() {
//        String fileName = "demo" + File.separator + "demo.xlsx";
        String fileName = "F:\\data\\aaaa.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭

//        EasyExcel.read(fileName, new ExcelListener()).sheet().doRead();

//        SyncReadListener listener = new SyncReadListener();
//        EasyExcel.read(fileName, listener).sheet().headRowNumber(1).doRead();
//        log.info("a {}", listener.getList());

        // 写法2：
//        ExcelReader excelReader = EasyExcel.read(fileName, new ExcelListener()).build();
//        ReadSheet readSheet = EasyExcel.readSheet(0).build();
//        excelReader.read(readSheet);
//        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
//        excelReader.finish();



//        SyncReadListener listener = new SyncReadListener();
//        ExcelReaderBuilder reader = EasyExcel.read(fileName);
//
//        List<ReadSheet> sheets = reader.build().excelExecutor().sheetList();
//        log.info("{}", JSON.toJSONString(sheets));



//        DemoDataListener listener = new DemoDataListener();
//        EasyExcel.read(fileName, DemoData.class, listener).sheet().headRowNumber(1).doRead();
//        log.info("a {}", listener.getList());

    }

    /**
     * 读多个或者全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>
     * 3. 直接读即可
     */
    @Test
    public void repeatedRead() {
        String fileName = "F:\\data\\aaaa.xlsx";
        // 读取全部sheet
        // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        SyncReadListener listener = new SyncReadListener();
        EasyExcel.read(fileName, listener).doReadAll();

        log.info("{}", listener.getList());

        // 读取部分sheet
//        fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
//        ExcelReader excelReader = EasyExcel.read(fileName).build();
//        // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
//        ReadSheet readSheet1 =
//                EasyExcel.readSheet(0).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
//        ReadSheet readSheet2 =
//                EasyExcel.readSheet(1).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
//        // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
//        excelReader.read(readSheet1, readSheet2);
//        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
//        excelReader.finish();
    }


    /**
     * 最简单的写
     * <p>1. 创建excel对应的实体对象 参照{@link }
     * <p>2. 直接写即可
     */
    @Test
    public void simpleWrite() {
        String fileName = "F:\\data\\test.xlsx";
        // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName).sheet("模板").doWrite(data());
    }

    private List<DownloadData> data() {
        List<DownloadData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DownloadData data = new DownloadData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    private List<Map> data2() {
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            list.add(Optional.of(new HashMap<String, Object>()).map(var -> {
                var.put("string", "字符串" + finalI);
                var.put("date", new Date());
                var.put("doubleData", 0.56);

                return var;
            }).get());
        }
        return list;
    }




}
