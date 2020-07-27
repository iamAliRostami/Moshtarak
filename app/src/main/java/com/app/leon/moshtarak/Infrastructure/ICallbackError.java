package com.app.leon.moshtarak.Infrastructure;

public interface ICallbackError<T> {
    void execute(Throwable t);
}
