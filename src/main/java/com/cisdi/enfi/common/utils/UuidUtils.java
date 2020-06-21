package com.cisdi.enfi.common.utils;

import java.util.UUID;

public class UuidUtils {
    public UuidUtils() {
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().toUpperCase();
        return uuid;
    }
}
