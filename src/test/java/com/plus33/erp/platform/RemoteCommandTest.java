package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.fleet.control.RemoteCommandService;

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
public class RemoteCommandTest {

    @Autowired RemoteCommandService commandService;

    @Autowired PlatformDeviceRemoteCommandRepository commandRepo;

    @Test
    void testRemoteCommandScenarios() {
        // Dispatching remote commands logs over 40 iterations
        for (int i = 1; i <= 40; i++) {
            commandService.dispatchCommand(1L, "Restart", "CRYPTOGRAPHIC-SIG-COMMAND-" + i);
        }

        List<PlatformDeviceRemoteCommand> commands = commandRepo.findAll();
        assertTrue(commands.size() >= 40);
        assertEquals("Restart", commands.get(0).getCommandType());
        assertEquals("EXECUTED", commands.get(0).getStatus());
    }
}
