package com.example.module_common.entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseEntity<T> extends BaseEntity {
    public T data;
    public boolean isError;
    public String errorCode;
    public String errorMessage;

    @Override
    @NotNull
    public String[] uniqueKey() {
        return new String[0];
    }

    public static class Data implements Serializable {
        public String msg;
    }

    public static class ListData<T> extends Data {
        public int count;
        public boolean hasNext;
        public ArrayList<T> list;
    }

}
