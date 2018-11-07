package com.zhanglijun.springbootdemo.web.MyDataTimeFormatAnnotationFormatFactory;

import org.springframework.format.datetime.DateFormatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * @author 夸克
 * @date 2018/9/18 17:51
 */
public class MyStartDateFormatter extends DateFormatter {

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        return super.parse(text, locale);

    }
}
