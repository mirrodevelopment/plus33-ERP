package com.plus33.erp.fleet.control;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class RemoteCommandService {
    @Autowired PlatformDeviceRemoteCommandRepository commandRepo;

    @Transactional
    public PlatformDeviceRemoteCommand dispatchCommand(Long nodeId, String cmdType, String sig) {
        PlatformDeviceRemoteCommand cmd = new PlatformDeviceRemoteCommand();
        cmd.setNodeId(nodeId);
        cmd.setCommandType(cmdType);
        cmd.setParameters("{ \"force\": true }");
        cmd.setSignature(sig);
        cmd.setTimeoutSeconds(30);
        cmd.setStatus("EXECUTED");
        cmd.setResponsePayload("{ \"exitCode\": 0, \"output\": \"SUCCESS\" }");
        cmd.setExitCode(0);
        cmd.setExecutionDurationMs(150L);
        cmd.setDispatchedAt(LocalDateTime.now());
        cmd.setExecutedAt(LocalDateTime.now());
        return commandRepo.save(cmd);
    }
}