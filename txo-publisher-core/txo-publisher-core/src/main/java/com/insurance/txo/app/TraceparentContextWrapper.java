package com.insurance.txo.app;

public interface TraceparentContextWrapper {

    void runInContext(String traceparent, Runnable runnable);

}
