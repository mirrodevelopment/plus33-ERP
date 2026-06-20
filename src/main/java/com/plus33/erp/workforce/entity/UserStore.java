package com.plus33.erp.workforce.entity;

import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_stores")
@NoArgsConstructor
@AllArgsConstructor
public class UserStore {

    @EmbeddedId
    private UserStoreId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("storeId")
    @JoinColumn(name = "store_id")
    private Store store;

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
    public static class UserStoreId implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "store_id")
        private Long storeId;
    }
}
