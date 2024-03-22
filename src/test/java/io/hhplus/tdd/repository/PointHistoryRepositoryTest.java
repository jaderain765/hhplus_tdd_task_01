package io.hhplus.tdd.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.stub.PointHistoryRepositoryStub;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PointHistoryRepositoryTest {

    PointHistoryRepository pointHistoryRepository;

    PointHistoryTable pointHistoryTable = new PointHistoryTable();

    public PointHistoryRepositoryTest(){
        pointHistoryRepository = new PointHistoryRepositoryImpl(pointHistoryTable);
    }

    @AfterEach
    void afterEach(){
        pointHistoryTable = new PointHistoryTable();
    }

    @Test
    void save() {
        List<PointHistory> histories = List.of(
                new PointHistory(1L, 1L, 1000L, TransactionType.CHARGE, 0L),
                new PointHistory(2L, 1L, 500L, TransactionType.USE, 0L),
                new PointHistory(3L, 2L, 10000L, TransactionType.CHARGE, 0L)
        );

        histories.stream().forEach(history -> {
            pointHistoryRepository.save(history);
        });

        List<PointHistory> findAll = pointHistoryRepository.findAllById(1L);

        findAll.stream().forEach(elem -> {
            Assertions.assertThat(elem).isIn(histories);
        });
    }

    @Test
    void findAllById() {
    }
}