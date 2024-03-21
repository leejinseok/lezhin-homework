package com.lezhin.homework.api.application.lock;

public interface LockService {

    boolean getLock(String lockKey, int timeoutInSeconds);
    void releaseLock(String lockKey);

}
