package com.camunda.savingsaccount.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

public class Utils {

    public static final int SUCCESS_ERROR_CODE = 0;
    public static final int UN_SUCCESS_ERROR_CODE = 1;
    public static final String SUCCESS_ERROR_DESCRIPTION = "Success";
    public static final String UN_SUCCESS_ERROR_DESCRIPTION = "Un success";



    @Autowired
    private static HttpHeaders httpHeaders;

    public static HttpHeaders getHttpHeaders(){
        return httpHeaders;
    }
}
