package com.example.saint.musicappzensoft.ui;

public interface LifeCycle<V> {

    void bind(V view);

    void unbind();
}
