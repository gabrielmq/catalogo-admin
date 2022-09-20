package com.fullcycle.catalogo.admin.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface CollectionUtils {

    static <IN, OUT> Set<OUT> mapTo(final Set<IN> list, final Function<IN, OUT> mapper) {
        return list.stream().map(mapper).collect(Collectors.toSet());
    }
}
