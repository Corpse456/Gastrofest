package by.gastrofest;

import by.gastrofest.service.SchedulingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class BackendTests {

    @MockitoBean
    SchedulingService schedulingService;

    @Test
    void contextLoads() {
    }

}
