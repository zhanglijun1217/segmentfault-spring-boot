package com.zhanglijun.springbootdemo.exception;

import com.alibaba.fastjson.JSON;
import com.zhanglijun.springbootdemo.domain.base.BaseResult;
import com.zhanglijun.springbootdemo.domain.base.ResultUtil;
import com.zhanglijun.springbootdemo.domain.constant.BizError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常捕获器 优化了多种异常的处理
 * @author 夸克
 * @create 2018/8/19 19:32
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 输出日志的message
     */
    public static final String message = "response: {}, request uri: {}, params : {}, ex message: {},";

    /**
     * 默认内部错误 code
     */
    private static final int DEFAULT_ERROR_CODE = 500;

    /**
     * 默认参数错误 code
     */
    private static final int DEFAULT_ARG_ERROR_CODE = 400;

    /**
     * 兜底的Exception的捕获
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public BaseResult handleEx(HttpServletRequest request, Exception ex) {
        log.warn(message, "Exception", request.getRequestURI(),
                JSON.toJSONString(request.getParameterMap()), ex.getMessage(), ex);
        return ResultUtil.buildExResponse(DEFAULT_ERROR_CODE, ex.getMessage());
    }

    /**
     * 空指针捕获
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public BaseResult handleEx(HttpServletRequest request, NullPointerException ex) {
        log.warn(message, "NullPointerException", request.getRequestURI(),
                JSON.toJSONString(request.getParameterMap()), ex.getMessage(), ex);
        return ResultUtil.buildExResponse(DEFAULT_ERROR_CODE, ex.getMessage());
    }

    /**
     * 业务异常捕获
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(BizException.class)
    public BaseResult handleEx(HttpServletRequest request, BizException ex) {
        log.warn(message, "BizException", request.getRequestURI(),
                JSON.toJSONString(request.getParameterMap()), ex.getMessage(), ex);
        return ResultUtil.buildExResponse(DEFAULT_ERROR_CODE, ex.getMessage());
    }

    /**
     * IllegalArgumentException 异常捕获
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResult handleEx(HttpServletRequest request, IllegalArgumentException ex) {
        log.warn(message, "IllegalArgumentException", request.getRequestURI(),
                JSON.toJSONString(request.getParameterMap()), ex.getMessage(), ex);
        return ResultUtil.buildExResponse(DEFAULT_ARG_ERROR_CODE, ex.getMessage());
    }

    /**
     * MethodArgumentNotValidException 异常捕获
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseResult handleEx(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.warn(message, "MethodArgumentNotValidException", request.getRequestURI(),
                JSON.toJSONString(request.getParameterMap()), ex.getMessage(), ex);
        BindingResult bindingResult = ex.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return ResultUtil.buildExResponse(DEFAULT_ARG_ERROR_CODE, fieldErrors.get(0).getDefaultMessage());
    }


}
