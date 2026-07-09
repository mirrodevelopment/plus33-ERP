package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.Attendance;
import com.plus33.erp.workforce.entity.AttendanceCorrection;
import com.plus33.erp.workforce.entity.CountryWorkPolicy;
import com.plus33.erp.workforce.entity.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceService {

    Map<String, Object> getCurrentState(Employee employee);

    Map<String, Object> getDashboard(Employee employee);

    List<Map<String, Object>> getCalendar(Employee employee, int year, int month);

    List<Map<String, Object>> getHistory(Employee employee);

    Map<String, Object> getOvertime(Employee employee);

    Map<String, Object> getReports(Employee employee);

    Attendance checkIn(Employee employee, String gps, String device, String ip, String userAgent);

    Attendance checkOut(Employee employee, String ip, String userAgent);

    void startBreak(Employee employee, String ip, String userAgent);

    void endBreak(Employee employee, String ip, String userAgent);

    AttendanceCorrection submitCorrection(Employee employee, LocalDate date, String reason, String checkIn, String checkOut, String ip, String userAgent);

    AttendanceCorrection approveCorrection(Long id, Long approverUserId, String ip, String userAgent);

    Map<String, Object> getSettings();

    CountryWorkPolicy getCountryWorkPolicy(String countryCode);
}
