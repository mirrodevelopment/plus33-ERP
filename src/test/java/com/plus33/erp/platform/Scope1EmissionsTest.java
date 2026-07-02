package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.esg.carbon.CarbonAccountingService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class Scope1EmissionsTest {

    @Autowired CarbonAccountingService carbonService;

    @Autowired PlatformEsgScope1LogRepository scope1Repo;

    @Test
    void testScope1Scenarios() {
        // Scope 1 direct emissions calculation over 40 iterations
        for (int i = 1; i <= 40; i++) {
            carbonService.calculateScope1((long) i, 1L);
        }

        List<PlatformEsgScope1Log> logs = scope1Repo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("Diesel", logs.get(0).getFuelType());
        assertEquals("LitersCombustion-EPA-v3", logs.get(0).getCalculationMethod());
    }
}
