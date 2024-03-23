package io.hhplus.tdd.point;

import io.hhplus.tdd.service.PointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PointService pointService;

    String url = "/point/";

    @Test
    void point() throws Exception {
        long id = 1L;

        pointService.updateUserPoint(id, 10000L, TransactionType.CHARGE);

        mockMvc.perform(get(url + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.data[0].amount").value("10000"))
                .andDo(print());
    }

//    @Test
//    @DisplayName("Test \"http://127.0.0.1:port/point/{id}/histories\"")
//    void history() {
//        long userId = 1L;
//
//        pointServiceStub.updateUserPoint(userId, 30000L, TransactionType.CHARGE);
//        pointServiceStub.updateUserPoint(userId, 20000L, TransactionType.USE);
//        pointServiceStub.updateUserPoint(userId, 55000L, TransactionType.CHARGE);
//
//        List<PointHistory> histories = restTemplate.getForObject(url + userId + "/histories", List.class, new Object());
//
//        assertThat(userId)
//                .isEqualTo(histories.get(0).userId())
//                .isEqualTo(histories.get(1).userId())
//                .isEqualTo(histories.get(2).userId());
//
//        assertThat(histories.get(0).amount()).isEqualTo(30000L);
//        assertThat(histories.get(1).amount()).isEqualTo(30000L - 20000L);
//        assertThat(histories.get(2).amount()).isEqualTo(30000L - 20000L + 55000L);
//
//        assertThat(histories.get(0).type()).isEqualTo(TransactionType.CHARGE);
//        assertThat(histories.get(1).type()).isEqualTo(TransactionType.USE);
//        assertThat(histories.get(2).type()).isEqualTo(TransactionType.CHARGE);
//    }
//
//    @Test
//    void charge() {
//        long userId = 1L;
//
//        Map<String, String> param = new HashMap<>();
//
//        param.put("amount","20000");
//
//        UserPoint userPoint = restTemplate.patchForObject(url + userId + "/charge", param, UserPoint.class);
//
//        assertThat(userId).isEqualTo(userPoint.id());
//        assertThat(20000L).isEqualTo(userPoint.point());
//    }
//
//    @Test
//    void use() {
//        long userId = 1L;
//
//        pointServiceStub.updateUserPoint(userId, 50000L, TransactionType.CHARGE);
//
//        Map<String, String> param = new HashMap<>();
//
//        param.put("amount","20000");
//
//        UserPoint userPoint = restTemplate.patchForObject(url + userId + "/use", param, UserPoint.class);
//
//        assertThat(userId).isEqualTo(userPoint.id());
//        assertThat(50000L - 20000L).isEqualTo(userPoint.point());
//    }
}