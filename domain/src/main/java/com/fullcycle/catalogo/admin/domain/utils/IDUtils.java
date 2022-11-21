package com.fullcycle.catalogo.admin.domain.utils;

import java.util.UUID;

public interface IDUtils {
    static String uuid() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }
}
