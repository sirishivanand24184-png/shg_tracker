package com.shg.adapter;

import java.util.Map;

public interface ApiPayloadAdapter<T> {
    Map<String, Object> adapt(T source);
}
