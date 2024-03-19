package io.hhplus.tdd.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository{

    private PointHistoryTable pointHistoryTable;

    @Autowired
    public PointHistoryRepositoryImpl(PointHistoryTable pointHistoryTable){
        this.pointHistoryTable = pointHistoryTable;
    }

    @Override
    public PointHistory save(PointHistory pointHistory) {
        long userId = pointHistory.userId();
        long amount = pointHistory.amount();
        TransactionType type = pointHistory.type();
        long updateMillis = pointHistory.updateMillis();

        return pointHistoryTable.insert(userId, amount, type, updateMillis);
    }

    @Override
    public List<PointHistory> findAllById(Long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}
