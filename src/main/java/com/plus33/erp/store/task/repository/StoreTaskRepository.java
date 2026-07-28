package com.plus33.erp.store.task.repository;

import com.plus33.erp.store.task.entity.StoreTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreTaskRepository extends JpaRepository<StoreTask, Long> {

    @Query("SELECT t FROM StoreTask t WHERE " +
           ":email IS NULL OR :email = '' OR " +
           "t.delegationMode = 'BROADCAST_ALL_EMPLOYEES' OR " +
           "t.assignedEmployeeEmail = 'all@plus33.com' OR " +
           "t.delegationMode = 'SUPERVISOR_DELEGATION' OR " +
           "LOWER(t.assignedEmployeeEmail) LIKE LOWER(CONCAT('%', :prefix, '%')) OR " +
           "LOWER(t.assignedEmployeeName) LIKE LOWER(CONCAT('%', :prefix, '%')) OR " +
           "LOWER(:email) LIKE LOWER(CONCAT('%', t.assignedEmployeeEmail, '%')) OR " +
           "LOWER(t.assignedEmployeeEmail) LIKE LOWER(CONCAT('%', :email, '%')) " +
           "ORDER BY t.dueDate ASC")
    List<StoreTask> findTasksForEmployeeOrRole(@Param("email") String email, @Param("prefix") String prefix);

    List<StoreTask> findByAssignedEmployeeIdOrderByDueDateAsc(Long employeeId);

    List<StoreTask> findByStoreIdOrderByDueDateAsc(Long storeId);

    List<StoreTask> findByParentTaskId(Long parentTaskId);

    @Query("SELECT t FROM StoreTask t WHERE LOWER(t.assignedEmployeeEmail) = LOWER(:email) AND t.status IN :statuses")
    List<StoreTask> findByAssignedEmployeeEmailAndStatusIn(@Param("email") String email, @Param("statuses") List<String> statuses);

    Optional<StoreTask> findFirstByAssignedEmployeeEmailAndStatus(String email, String status);

    List<StoreTask> findByExtensionStatus(String extensionStatus);

    @Query("SELECT COUNT(t) FROM StoreTask t WHERE (:email IS NULL OR LOWER(t.assignedEmployeeEmail) LIKE LOWER(CONCAT('%', :email, '%'))) AND t.dueDate >= :startOfDay AND t.dueDate <= :endOfDay")
    long countTodayTasks(@Param("email") String email, @Param("startOfDay") ZonedDateTime startOfDay, @Param("endOfDay") ZonedDateTime endOfDay);

    @Query("SELECT COUNT(t) FROM StoreTask t WHERE (:email IS NULL OR LOWER(t.assignedEmployeeEmail) LIKE LOWER(CONCAT('%', :email, '%'))) AND t.status = 'COMPLETED' AND t.updatedAt >= :startOfMonth AND t.updatedAt <= :endOfMonth")
    long countMonthlyCompletedTasks(@Param("email") String email, @Param("startOfMonth") ZonedDateTime startOfMonth, @Param("endOfMonth") ZonedDateTime endOfMonth);

    @Query("SELECT COUNT(t) FROM StoreTask t WHERE (:email IS NULL OR LOWER(t.assignedEmployeeEmail) LIKE LOWER(CONCAT('%', :email, '%'))) AND t.status <> 'COMPLETED' AND t.createdAt >= :startOfMonth AND t.createdAt <= :endOfMonth")
    long countMonthlyUncompletedTasks(@Param("email") String email, @Param("startOfMonth") ZonedDateTime startOfMonth, @Param("endOfMonth") ZonedDateTime endOfMonth);
}
