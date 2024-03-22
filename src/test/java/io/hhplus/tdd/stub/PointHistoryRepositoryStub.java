package io.hhplus.tdd.stub;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.repository.PointHistoryRepository;

import java.util.ArrayList;
import java.util.List;

public class PointHistoryRepositoryStub implements PointHistoryRepository {

    private static List<PointHistory> store = new ArrayList<>();

    public void clear() {
        this.store.clear();
    }

    @Override
    public PointHistory save(PointHistory pointHistory) {
        store.add(pointHistory);
        return pointHistory;
    }

    @Override
    public List<PointHistory> findAllById(Long userId) {
        return store.stream().filter(ph -> userId.equals(ph.userId())).toList();
    }
}
