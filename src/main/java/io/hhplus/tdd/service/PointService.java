package io.hhplus.tdd.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.repository.PointHistoryRepository;
import io.hhplus.tdd.repository.UserPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {

    UserPointRepository userPointRepository;

    PointHistoryRepository pointHistoryRepository;

    @Autowired
    public PointService(UserPointRepository userPointRepository, PointHistoryRepository pointHistoryRepository){
        this.userPointRepository =  userPointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    /*
     * 사용자 포인트 조회
     */
    public UserPoint searchUserPoint(long userId){
        return userPointRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    /*
     * 사용자 포인트 사용기록 조회
     */
    public List<PointHistory> searchUserHistory(long userId){
        return pointHistoryRepository.findAllById(userId);
    }

    /*
     * 포인트 충전/사용
     */
    public UserPoint updateUserPoint(long userId, long amount, TransactionType type) {
        UserPoint findUserPoint = searchUserPoint(userId);

        // 포인트를 사용할때, 모자를 경우
        if(type == TransactionType.USE && findUserPoint.point() < amount)
            throw new RuntimeException("포인트가 모자랍니다.");

        // 포인트의 사용/충전 여부에 따라 차감
        long changePoint = type == TransactionType.USE ?
                findUserPoint.point() - amount :
                findUserPoint.point() + amount;

        UserPoint updateUserPoint = new UserPoint(
                userId,
                changePoint,
                System.currentTimeMillis()
        );

        PointHistory insertPointHistory = new PointHistory(
                0,
                userId,
                amount,
                type,
                System.currentTimeMillis()
        );

        userPointRepository.save(updateUserPoint);
        pointHistoryRepository.save(insertPointHistory);

        return updateUserPoint;
    }
}
