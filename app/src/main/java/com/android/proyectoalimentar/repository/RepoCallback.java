package com.android.proyectoalimentar.repository;

public interface RepoCallback<T> {

    void onSuccess(T value);

    void onError(String error);

}
