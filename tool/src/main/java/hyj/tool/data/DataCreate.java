package hyj.tool.data;

import hyj.tool.excel.ExcelUtil;
import hyj.tool.io.TextIo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据生成器
 */
public class DataCreate {
    /**
     * 读取数据模板
     */
    public static void dateCreateToFile(String dataTypeFile, String jsonTemplateFile, int count, String outPath) throws Exception {
        List<String> dataList = dateCreate(dataTypeFile, jsonTemplateFile, count);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\r\n");
        for(int i = 0; i < dataList.size();i++){
            stringBuilder.append(dataList.get(i));
            if(i != dataList.size() - 1){
                stringBuilder.append(",").append("\r\n");
            }
        }
        stringBuilder.append("\r\n]");

        TextIo.outPut(outPath, stringBuilder.toString());

    }

    /**
     * 读取数据模板
     */
    public static List<String> dateCreate(String dataTypeFile, String jsonTemplateFile, int count) throws Exception {
        Workbook workbook = ExcelUtil.getWorkbook(dataTypeFile);
        Sheet sheet = workbook.getSheetAt(0);
        String[] paramsArray = {"name", "code", "typeName", "format", "min", "max", "area"};
        ExcelUtil.Type[] typeArray = {ExcelUtil.Type.S, ExcelUtil.Type.S, ExcelUtil.Type.S, ExcelUtil.Type.S, ExcelUtil.Type.S, ExcelUtil.Type.S, ExcelUtil.Type.S};

        String jsonTemplate = (new TextIo()).input(jsonTemplateFile);
        List<String> resultList = new ArrayList<String>(count);
        List<Map<String, Object>> dataTypeList = ExcelUtil.readExcelMsg(sheet, paramsArray, typeArray, 0, 1);
        for(int i = 0;i < count; i++) {
            String template = jsonTemplate;
            for (Map<String, Object> dataTypeMap : dataTypeList) {
                if(dataTypeMap.get("typeName") == null || dataTypeMap.get("typeName").equals("")){
                    continue;
                }

                template = template.replace("${" + dataTypeMap.get("code") + "}",
                                createData(dataTypeMap.get("typeName").toString(),
                                (String) dataTypeMap.get("format"),
                                (String) dataTypeMap.get("min"),
                                (String) dataTypeMap.get("max"),
                                (String) dataTypeMap.get("area")
                                ));
            }

            resultList.add(template);
        }
        return resultList;
    }

    /**
     * 创建数据
     * @param dataType 数据格式
     * @param format   小数长度（日期格式）
     * @param min      数字最小值（日期最小值）
     * @param max      数字最大值（日期最大值）
     * @return
     */
    public static String createData(String dataType,
                                    String format,
                                    String min,
                                    String max,
                                    String area) throws ParseException {
        Object o = null;
        if ("名字".equals(dataType)) {
            o = DataCreateUtils.createName();
        } else if ("手机号".equals(dataType)) {
            o = DataCreateUtils.createPhone();
        } else if ("Number".equals(dataType)) {
            o = DataCreateUtils.createNumber(format, min, max);
        } else if ("String".equals(dataType)) {
            o = DataCreateUtils.createString(area);
        } else if ("Date".equals(dataType)) {
            o = DataCreateUtils.createDate(format, min, max);
        }

        return o.toString();
    }

    public static void main(String arg[]) throws Exception {
        dateCreateToFile("E:\\hyj\\dataCreate\\数据格式.xlsx",
                "E:\\hyj\\dataCreate\\formate.txt",
                5,
                "E:\\hyj\\dataCreate\\out.txt");
    }
}
