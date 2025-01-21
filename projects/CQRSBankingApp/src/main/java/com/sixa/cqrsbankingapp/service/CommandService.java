package com.sixa.cqrsbankingapp.service;

public interface CommandService<T> {

    void create(T object);
}
