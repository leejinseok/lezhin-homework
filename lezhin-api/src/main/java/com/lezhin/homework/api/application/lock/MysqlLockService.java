package com.lezhin.homework.api.application.lock;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"prod"})
@Service
@RequiredArgsConstructor
public class MysqlLockService implements LockService {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public boolean getLock(final String lockKey, final int timeoutInSeconds) {
        Query query = entityManager.createNativeQuery("SELECT GET_LOCK(?1, ?2)");
        query.setParameter(1, lockKey);
        query.setParameter(2, timeoutInSeconds);

        // 실행 결과 처리
        Object result = query.getSingleResult();
        if (result instanceof Number) {
            int lockResult = ((Number) result).intValue();
            return lockResult == 1;
        } else {
            return false;
        }
    }

    @Override
    public void releaseLock(final String lockKey) {
        Query query = entityManager.createNativeQuery("SELECT RELEASE_LOCK(?1)");
        query.setParameter(1, lockKey);
        query.executeUpdate();
    }

}
