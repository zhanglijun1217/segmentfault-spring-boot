package com.zhanglijun.springbootdemo.web.MyDataTimeFormatAnnotationFormatFactory;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 重载parse接口 添加处理起止时间业务逻辑
 * @author 夸克
 * @date 2018/9/18 17:32
 */
@Component
public class MyEndDateFormatter extends DateFormatter {
    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        Date target = super.parse(text, locale);
        //+1天
        Date date = DateUtils.ceiling(new Date(target.getTime() + 1), Calendar.DATE);
        //减1ms，得出23:59:59
        Date result =new Date(date.getTime()-1);
        return result;
    }
}
