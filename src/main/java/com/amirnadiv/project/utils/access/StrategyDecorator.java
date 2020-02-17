package com.amirnadiv.project.utils.common.access;

public class StrategyDecorator implements AccessStrategy {

    private AccessStrategy strategy;

    public StrategyDecorator(AccessStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String find(long id) {
        return strategy.find(id);
    }

}
