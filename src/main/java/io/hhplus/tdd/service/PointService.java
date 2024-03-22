package io.hhplus.tdd.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointService {
    public UserPoint searchUserPoint(long userId);
    public List<PointHistory> searchUserHistory(long userId);
    public UserPoint updateUserPoint(long userId, long amount, TransactionType type);
}
