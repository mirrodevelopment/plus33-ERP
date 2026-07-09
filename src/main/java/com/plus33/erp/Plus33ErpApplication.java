package com.plus33.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <b>PLUS33 Coffee ERP -- Plus33ErpApplication.java Module</b>
 *
 * <p><b>Class  :</b> {@code Plus33ErpApplication}</p>
 * <p><b>Package:</b> {@code com.plus33.erp}</p>
 * <p><b>Layer  :</b> Component of Plus33ErpApplication.java Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Plus33ErpApplication {

    /**
     * Performs the main operation in this module.
     *
     * @param args the args input value
     */
	public static void main(String[] args) {
		SpringApplication.run(Plus33ErpApplication.class, args);
	}

}