package com.plus33.erp.logistics.network;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class LogisticsNetworkService {
    @Autowired PlatformLogisticsNodeRepository nodeRepo;
    @Autowired PlatformShippingLaneRepository laneRepo;

    @Transactional
    public PlatformLogisticsNode addNode(String code, String type, BigDecimal lat, BigDecimal lon, String region, String tz, int cap) {
        PlatformLogisticsNode node = new PlatformLogisticsNode();
        node.setNodeCode(code);
        node.setNodeType(type);
        node.setLatitude(lat);
        node.setLongitude(lon);
        node.setRegion(region);
        node.setTimezone(tz);
        node.setCapacity(cap);
        node.setStatus("ACTIVE");
        return nodeRepo.save(node);
    }

    @Transactional
    public PlatformShippingLane addLane(Long src, Long dest, BigDecimal distance, int duration, String mode) {
        PlatformShippingLane lane = new PlatformShippingLane();
        lane.setSourceNodeId(src);
        lane.setDestinationNodeId(dest);
        lane.setDistanceKm(distance);
        lane.setExpectedDurationMinutes(duration);
        lane.setTransportMode(mode);
        return laneRepo.save(lane);
    }
}