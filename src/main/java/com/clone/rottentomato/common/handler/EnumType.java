package com.clone.rottentomato.common.handler;

public interface EnumType {
    default String getKey(){
        return null;
    };
    String getValue();
}