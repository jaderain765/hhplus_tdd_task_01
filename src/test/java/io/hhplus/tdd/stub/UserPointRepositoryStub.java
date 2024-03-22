package io.hhplus.tdd.stub;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.repository.UserPointRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserPointRepositoryStub implements UserPointRepository {
    private static Map<Long, UserPoint> store = new HashMap<>();

    public void clear(){
        store.clear();
    }

    @Override
    public UserPoint save(UserPoint userPoint) {
        store.put(userPoint.id(), userPoint);
        return userPoint;
    }

    @Override
    public Optional<UserPoint> findById(Long id) {
        return Optional.ofNullable(store.getOrDefault(id, UserPoint.empty(id)));
    }
}
