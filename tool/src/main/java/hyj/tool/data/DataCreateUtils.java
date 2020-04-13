package hyj.tool.data;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 数据生成器方式
 */
public class DataCreateUtils {
    /**
     * 创建人名
     * @return
     */
    public static String createName(){
        return NameBuilder.build();
    }

    /**
     * 创建手机号
     * @return
     */
    public static String createPhone(){
        return createNumber("0", "13000000001", "13999999999");
    }

    /**
     * 创建数字
     * @param length 小数位长度
     * @return
     */
    public static String createNumber(String length, String min, String max){
        if(length == null){
            length = "0";
        }else{
            length = ((Double) Double.parseDouble(length)).intValue() + "";
        }

        Double maxValue, minValue;

        if(min == null){
            minValue = Double.MIN_VALUE;
        }else{
            minValue = Double.parseDouble(min);
        }

        if(max == null){
            maxValue = Double.MAX_VALUE;
        }else{
            maxValue = Double.parseDouble(max);
        }

        Double random = RandomUtils.nextDouble(minValue, maxValue);
        return String.format("%." + length + "f", random);
    }


    public static Object createString(String area) {
        if(StringUtils.isEmpty(area)){
            return "";
        }

        String[] areaArray = area.split(",");
        int index = RandomUtils.nextInt(0, areaArray.length);

        return areaArray[index];
    }


    public static Object createDate(String format, String min, String max) throws ParseException {
        Long startDateLong, endDateLong;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        if(StringUtils.isEmpty(min)){
            startDateLong = 1l;
        }else{
            startDateLong = simpleDateFormat.parse(min).getTime();
        }

        if(StringUtils.isEmpty(max)){
            endDateLong = 1l;
        }else{
            endDateLong = simpleDateFormat.parse(max).getTime();
        }

        Long time = RandomUtils.nextLong(startDateLong, endDateLong);

        simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }
}
