package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.dispatch.constraint.DispatchConstraintSolver;

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
public class DispatchConstraintSolverTest {

    @Autowired DispatchConstraintSolver constraintSolver;

    @Autowired PlatformDispatchConstraintCheckRepository constraintRepo;

    @Test
    void testDispatchConstraintSolverScenarios() {
        // Dispatch capacity constraint checks over 40 iterations
        for (int i = 1; i <= 40; i++) {
            constraintSolver.verifyConstraints((long) i, "Capacity");
        }

        List<PlatformDispatchConstraintCheck> checks = constraintRepo.findAll();
        assertTrue(checks.size() >= 40);
        assertEquals("Capacity", checks.get(0).getConstraintType());
        assertEquals("PASSED", checks.get(0).getStatus());
        assertEquals("INFO", checks.get(0).getSeverity());
    }
}
