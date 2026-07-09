/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.shared
 * File              : StateMachine.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StateMachineController
 * Related Service   : StateMachineService, StateMachineServiceImpl
 * Related Repository: StateMachineRepository
 * Related Entity    : StateMachine
 * Related DTO       : N/A
 * Related Mapper    : StateMachineMapper
 * Related DB Table  : state_machines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.shared;

import java.util.Map;
import java.util.Set;

public class StateMachine<TState extends Enum<TState>> {
    private final Map<TState, Set<TState>> transitionMatrix;

    public StateMachine(Map<TState, Set<TState>> transitionMatrix) {
        this.transitionMatrix = transitionMatrix;
    }

    /**
     * Validates business rules and constraints for transition.
     *
     * @param current the current input value
     * @param target the target input value
     * @return true if operation succeeded, false otherwise
     * @throws BusinessException if a business rule is violated
     */
    public boolean validateTransition(TState current, TState target) {
        Set<TState> allowed = transitionMatrix.get(current);
        return allowed != null && allowed.contains(target);
    }
}
