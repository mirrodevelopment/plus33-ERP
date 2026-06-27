package com.plus33.erp.finance.treasury.shared;

import java.util.Map;
import java.util.Set;

public class StateMachine<TState extends Enum<TState>> {
    private final Map<TState, Set<TState>> transitionMatrix;

    public StateMachine(Map<TState, Set<TState>> transitionMatrix) {
        this.transitionMatrix = transitionMatrix;
    }

    public boolean validateTransition(TState current, TState target) {
        Set<TState> allowed = transitionMatrix.get(current);
        return allowed != null && allowed.contains(target);
    }
}
