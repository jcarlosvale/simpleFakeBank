package com.saltpay.bank.controller;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtil {
    public static Long extractId(String locationUrl, String url) {
        url = url + "/";
        return Long.valueOf(locationUrl.replace(url, ""));
    }
}
