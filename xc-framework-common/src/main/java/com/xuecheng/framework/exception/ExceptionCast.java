package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 作者: lin
 * 描述: 异常封装类
 * 日期: 2018/11/1 14:27
 */
public class ExceptionCast {

    public static void cast(ResultCode resultCode) {

        throw new CustomException(resultCode);
    }
}
