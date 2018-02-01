package studio.xiaoyun.web;

import studio.xiaoyun.core.exception.XyException;

/**
 * 用于封装web请求时发生的异常信息
 */
public class WebException extends XyException{
    private final ErrorCode errorCode;

    /**
     *
     * @param message 错误信息
     */
    public WebException(String message){
        super(message);
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    /**
     *
     * @param message 错误信息
     * @param e 原始异常
     */
    public WebException(String message, Exception e){
        super(message,e);
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    /**
     *
     * @param message 错误信息
     * @param errorCode 错误码
     */
    public WebException(String message,ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    /**
     *
     * @param message 错误信息
     * @param e 原始异常
     * @param errorCode 错误码
     */
    public WebException(String message, Exception e,ErrorCode errorCode){
        super(message,e);
        this.errorCode = errorCode;
    }

    /**
     * 取得错误码
     * @return 错误码 , 如果没有设置错误码，则返回
     * {@link ErrorCode#INTERNAL_SERVER_ERROR INTERNAL_SERVER_ERROR}
     */
    public ErrorCode getErrorCode(){
        return errorCode==null?ErrorCode.INTERNAL_SERVER_ERROR:errorCode;
    }

}
