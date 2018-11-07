package com.zhanglijun.springbootdemo.domain.validate;

import javax.validation.constraints.Min;
import lombok.Data;

/**
 * 分组校验
 * @author 夸克
 * @create 2018/8/17 11:44
 */
@Data
public class ValidateByGroupBO {

    /**
     * 只有adult组内才进行 validate 校验
     */
    @Min(value = 18, groups = {Adult.class})
    private Integer age;

    public interface Adult{}

    public interface Minor{}
}
