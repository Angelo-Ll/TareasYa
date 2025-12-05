package com.sise.tareasya.data.common;

public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public BaseResponse() {}

    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}