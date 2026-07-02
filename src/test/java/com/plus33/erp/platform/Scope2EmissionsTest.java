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
public class Scope2EmissionsTest {

    @Autowired CarbonAccountingService carbonService;

    @Autowired PlatformEsgScope2LogRepository scope2Repo;

    @Test
    void testScope2Scenarios() {
        // Scope 2 indirect grid charging emissions over 40 iterations
        for (int i = 1; i <= 40; i++) {
            carbonService.calculateScope2((long) i, 1L);
        }

        List<PlatformEsgScope2Log> logs = scope2Repo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("US-CALI-GRID", logs.get(0).getGridRegion());
    }
}
