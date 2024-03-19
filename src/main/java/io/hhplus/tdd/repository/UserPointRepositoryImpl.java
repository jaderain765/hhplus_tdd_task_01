package io.hhplus.tdd.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserPointRepositoryImpl implements UserPointRepository{
    private UserPointTable userPointTable;

    @Autowired
    public UserPointRepositoryImpl(UserPointTable userPointTable){
        this.userPointTable = userPointTable;
    }

    @Override
    public UserPoint save(UserPoint object) {
        return userPointTable.insertOrUpdate(object.id(), object.point());
    }

    @Override
    public Optional<UserPoint> findById(Long id) {
        return Optional.ofNullable(userPointTable.selectById(id));
    }

}
