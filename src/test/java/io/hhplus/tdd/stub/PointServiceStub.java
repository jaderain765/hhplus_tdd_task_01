package io.hhplus.tdd.stub;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.service.PointService;

import javax.crypto.MacSpi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointServiceStub implements PointService {

    Map<Long, UserPoint> pointStore = new HashMap<>();
    List<PointHistory> historyStore = new ArrayList<>();

    Long historySeq = 1L;

    public void clear(){
        this.pointStore.clear();
        this.historyStore.clear();
    }

    @Override
    public UserPoint searchUserPoint(long userId) {
        return pointStore.getOrDefault(userId, UserPoint.empty(userId));
    }

    @Override
    public List<PointHistory> searchUserHistory(long userId) {
        return historyStore.stream().filter(history -> history.userId() == userId).toList();
    }

    @Override
    public UserPoint updateUserPoint(long userId, long amount, TransactionType type) {
        UserPoint findUserPoint = searchUserPoint(userId);

        Long updatePoint = (type == TransactionType.USE)?
                findUserPoint.point() - amount :
                findUserPoint.point() + amount;

        UserPoint newUserPoint = new UserPoint(userId, updatePoint, System.currentTimeMillis());

        pointStore.put(userId, newUserPoint);

        historyStore.add(new PointHistory(historySeq++, userId, amount, type, System.currentTimeMillis()));

        return newUserPoint;
    }
}
