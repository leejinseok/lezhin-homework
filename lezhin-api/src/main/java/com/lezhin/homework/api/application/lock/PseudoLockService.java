package com.lezhin.homework.api.application.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"!prod"})
@Slf4j
@Service
public class PseudoLockService implements LockService {

    @Override
    public boolean getLock(String lockKey, int timeoutInSeconds) {
        log.info("get lock!");
        return true;
    }

    @Override
    public void releaseLock(String lockKey) {
        log.info("release lock!");
    }

}
