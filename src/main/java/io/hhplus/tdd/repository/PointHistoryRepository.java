package io.hhplus.tdd.repository;

import io.hhplus.tdd.point.PointHistory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointHistoryRepository {
    PointHistory save(PointHistory pointHistory);

    List<PointHistory> findAllById(Long userId);
}
