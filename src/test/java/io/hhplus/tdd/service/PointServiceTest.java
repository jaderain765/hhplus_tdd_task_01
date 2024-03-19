package io.hhplus.tdd.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.repository.PointHistoryRepository;
import io.hhplus.tdd.repository.PointHistoryRepositoryImpl;
import io.hhplus.tdd.repository.UserPointRepository;
import io.hhplus.tdd.repository.UserPointRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PointServiceTest {

    private PointService pointService;

    /*
     * 각 케이스를 실행하기 전에 DB의 정보를 초기화 한다.
     */
    @BeforeEach
    void beforeEach(){
        PointHistoryTable pointHistoryTable = new PointHistoryTable();
        UserPointTable userPointTable = new UserPointTable();
        // Repository
        PointHistoryRepository pointHistoryRepository = new PointHistoryRepositoryImpl(pointHistoryTable);
        UserPointRepository userPointRepository = new UserPointRepositoryImpl(userPointTable);
        // Service
        this.pointService = new PointService(userPointRepository, pointHistoryRepository);
    }

    @Test
    void searchUserPoint() {
        long userId1 = 1L;
        long userId2 = 2L;
        long userId3 = 3L;

        UserPoint user1 = pointService.updateUserPoint(userId1, 1000L, TransactionType.CHARGE);
        UserPoint user2 = pointService.updateUserPoint(userId2, 5000L, TransactionType.CHARGE);
        UserPoint user3 = pointService.updateUserPoint(userId3, 100000L, TransactionType.CHARGE);

        UserPoint result1 = pointService.searchUserPoint(userId1);
        UserPoint result2 = pointService.searchUserPoint(userId2);
        UserPoint result3 = pointService.searchUserPoint(userId3);

        assertThat(result1).isEqualTo(user1);
        assertThat(result2).isEqualTo(user2);
        assertThat(result3).isEqualTo(user3);
    }

    @Test
    void searchUserHistory() {
        long userId = 1L;

        pointService.updateUserPoint(userId, 10000L, TransactionType.CHARGE);
        pointService.updateUserPoint(userId, 5000L, TransactionType.USE);
        pointService.updateUserPoint(userId, 100L, TransactionType.CHARGE);

        List<PointHistory> pointHistoryList = pointService.searchUserHistory(1L);

        // state1
        assertThat(pointHistoryList.get(0).userId()).isEqualTo(userId);
        assertThat(pointHistoryList.get(0).amount()).isEqualTo(10000L);
        assertThat(pointHistoryList.get(0).type()).isEqualTo(TransactionType.CHARGE);
        // state2
        assertThat(pointHistoryList.get(1).userId()).isEqualTo(userId);
        assertThat(pointHistoryList.get(1).amount()).isEqualTo(10000L - 5000L);
        assertThat(pointHistoryList.get(1).type()).isEqualTo(TransactionType.USE);
        // state3
        assertThat(pointHistoryList.get(2).userId()).isEqualTo(userId);
        assertThat(pointHistoryList.get(2).amount()).isEqualTo(10000L - 5000L + 100L);
        assertThat(pointHistoryList.get(2).type()).isEqualTo(TransactionType.CHARGE);
    }

    @Test
    void updateUserPoint() {

    }
}