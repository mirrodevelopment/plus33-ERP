package com.plus33.erp.workforce.entity;

import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_regions")
@NoArgsConstructor
@AllArgsConstructor
public class UserRegion {

    @EmbeddedId
    private UserRegionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("regionId")
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserRegionId implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "region_id")
        private Long regionId;
    }
}
