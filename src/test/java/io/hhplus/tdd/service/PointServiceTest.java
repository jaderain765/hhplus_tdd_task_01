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
import io.hhplus.tdd.stub.PointHistoryRepositoryStub;
import io.hhplus.tdd.stub.UserPointRepositoryStub;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.*;

class PointServiceTest {

    private PointService pointService;
    private UserPointRepositoryStub userPointRepositoryStub;
    private PointHistoryRepositoryStub pointHistoryRepositoryStub;

    public PointServiceTest(){
        userPointRepositoryStub = new UserPointRepositoryStub();
        pointHistoryRepositoryStub = new PointHistoryRepositoryStub();

        this.pointService = new PointServiceImpl(userPointRepositoryStub, pointHistoryRepositoryStub);
    }

    /*
     * 각 케이스를 실행하기 전에 DB의 정보를 초기화 한다.
     */
    @AfterEach
    void afterEach(){
        userPointRepositoryStub.clear();
        pointHistoryRepositoryStub.clear();
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
        assertThat(pointHistoryList.get(1).amount()).isEqualTo(5000L);
        assertThat(pointHistoryList.get(1).type()).isEqualTo(TransactionType.USE);
        // state3
        assertThat(pointHistoryList.get(2).userId()).isEqualTo(userId);
        assertThat(pointHistoryList.get(2).amount()).isEqualTo(100L);
        assertThat(pointHistoryList.get(2).type()).isEqualTo(TransactionType.CHARGE);
    }

    @Nested
    @DisplayName("UpdateUserPointTest")
    class UpdateUserPoint {
        @Test
        void updateUserPoint() {
            long userId1 = 1L;
            long userId2 = 2L;
            long userId3 = 3L;

            UserPoint userPoint1 = pointService.updateUserPoint(userId1, 10000L, TransactionType.CHARGE);
            UserPoint userPoint2 = pointService.updateUserPoint(userId2, 20000L, TransactionType.CHARGE);
            UserPoint userPoint3 = pointService.updateUserPoint(userId3, 30000L, TransactionType.CHARGE);

            UserPoint searchUserPoint1 = pointService.searchUserPoint(userId1);
            UserPoint searchUserPoint2 = pointService.searchUserPoint(userId2);
            UserPoint searchUserPoint3 = pointService.searchUserPoint(userId3);

            assertThat(userPoint1).isEqualTo(searchUserPoint1);
            assertThat(userPoint2).isEqualTo(searchUserPoint2);
            assertThat(userPoint3).isEqualTo(searchUserPoint3);
        }

        @Test
        void updateUserPoint_포인트가_모자랄_때() {
            long userId1 = 1L;

            UserPoint state1 = UserPoint.empty(userId1);

            try{
                state1 = pointService.updateUserPoint(userId1, 10000L, TransactionType.CHARGE);
                pointService.updateUserPoint(userId1, 20000L, TransactionType.USE);
            }catch (RuntimeException e){
                assertThat(e.getMessage()).isEqualTo("포인트가 모자랍니다.");
                assertThat(pointService.searchUserPoint(userId1)).isEqualTo(state1);
            }
        }

        /**
         * overflow로 인해 음수로 변했거나, 변경하려는 포인트 값이 음수 일 경우
         */
        @Test
        void updateUserPoint_point_overflow() {
            long userId1 = 1L;

            Assertions.assertThrows(RuntimeException.class, () -> {
                pointService.updateUserPoint(userId1, Long.MAX_VALUE, TransactionType.CHARGE);
                pointService.updateUserPoint(userId1, 20000L, TransactionType.CHARGE);
            });
        }

        @Test
        void updateUserPoint_음수가_변경값으로_들어올_수_없다() {
            Assertions.assertThrows(RuntimeException.class, () -> {
                pointService.updateUserPoint(1L, 20000L, TransactionType.CHARGE);
                pointService.updateUserPoint(1L, -10000L, TransactionType.CHARGE);
            });
        }

        @Test
        @DisplayName("포인트 충전,사용 시 동시성 테스트")
        void updateUserPoint_동시성_테스트() throws InterruptedException {
            // given
            Long userId = 1L;
            Long amount = 1000L;

            int THREAD_COUNT = 1000; // 금액 사용할 횟수

            CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

            // when
            for(int i = 0; i < THREAD_COUNT; i++){
                Thread thread = new Thread(()->{
                    pointService.updateUserPoint(userId, amount, TransactionType.CHARGE);
                    countDownLatch.countDown();
                });

                thread.start();
            }

            countDownLatch.await();

            // then
            // 1000번 충전 했기에 1000 * 1000 = 1,000,000 만큼 있어야 한다.
            assertThat(pointService.searchUserPoint(userId).point())
                    .isEqualTo(1000L * THREAD_COUNT);
            // 1000번 충전 했기에 1000번의 기록이 있어야 한다.
            assertThat(pointService.searchUserHistory(userId).size())
                    .isEqualTo(THREAD_COUNT);
        }
    }
}