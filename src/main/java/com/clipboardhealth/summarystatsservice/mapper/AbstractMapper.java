package com.clipboardhealth.summarystatsservice.mapper;

public interface AbstractMapper<E, V> {
    void entityToView(E e, V v);
}
