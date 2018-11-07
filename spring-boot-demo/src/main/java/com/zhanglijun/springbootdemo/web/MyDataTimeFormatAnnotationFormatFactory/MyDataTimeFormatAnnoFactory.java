package com.zhanglijun.springbootdemo.web.MyDataTimeFormatAnnotationFormatFactory;

import com.zhanglijun.springbootdemo.domain.anno.MyDateTimeFormat;
import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.*;

/**
 * 注解处理工厂
 * @author 夸克
 * @date 2018/9/18 17:29
 */
public class MyDataTimeFormatAnnoFactory extends EmbeddedValueResolutionSupport implements AnnotationFormatterFactory<MyDateTimeFormat> {

    private static final Set<Class<?>> FIELD_TYPES;

    static {
        Set<Class<?>> fieldTypes = new HashSet<Class<?>>(4);
        fieldTypes.add(Date.class);
        fieldTypes.add(Calendar.class);
        fieldTypes.add(Long.class);
        FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public Printer<?> getPrinter(MyDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(MyDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    protected Formatter<Date> getFormatter(MyDateTimeFormat annotation, Class<?> fieldType) {
        // 拿到annotation中的type
        if (annotation.type() == MyDateTimeFormat.Type.START) {
            MyStartDateFormatter formatter = new MyStartDateFormatter();
            formatter.setStylePattern(resolveEmbeddedValue(annotation.style()));
            formatter.setIso(annotation.iso());
            formatter.setPattern(resolveEmbeddedValue(annotation.pattern()));
            return formatter;
        } else if (annotation.type() == MyDateTimeFormat.Type.END){
            MyEndDateFormatter formatter = new MyEndDateFormatter();
            formatter.setStylePattern(resolveEmbeddedValue(annotation.style()));
            formatter.setIso(annotation.iso());
            formatter.setPattern(resolveEmbeddedValue(annotation.pattern()));
            return formatter;
        } else {
            throw new IllegalArgumentException("无效Type参数");
        }
    }

}
