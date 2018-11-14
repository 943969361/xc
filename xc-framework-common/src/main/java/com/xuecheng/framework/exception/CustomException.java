package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 作者: lin
 * 描述: 异常定义类
 * 日期: 2018/11/1 14:14
 */
public class CustomException extends RuntimeException {

    ResultCode resultCode;

    // 构造方法
    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode(){
        return this.resultCode;
    }


}
