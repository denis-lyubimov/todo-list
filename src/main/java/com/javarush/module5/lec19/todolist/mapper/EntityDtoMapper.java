package com.javarush.module5.lec19.todolist.mapper;

import java.util.List;

public interface EntityDtoMapper<T, V> {
    V entityToDto(T t);

    List<V> entityListToDtoList(List<T> t);

    T dtoToEntity(V v);

    List<T> dtoListToEntityList(List<V> v);
}
