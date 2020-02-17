package com.amirnadiv.project.utils.common.access.strategy;

import java.io.File;

import com.amirnadiv.project.utils.common.access.AccessStrategy;
import com.amirnadiv.project.utils.common.collection.Stack;

public class DivideThousand implements AccessStrategy {

    private static final int BASE = 1000;

    private int base = BASE;

    public DivideThousand() {

    }

    public DivideThousand(int base) {
        this.base = base;
    }

    @Override
    public String find(long id) {
        Stack<Long> stack = new Stack<Long>();

        for (; id * base >= base; id = id / base) {
            stack.push(id);
        }

        StringBuilder builder = new StringBuilder();

        for (; !stack.isEmpty();) {
            builder.append(stack.pop()).append(File.separator);
        }

        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public void setBase(int base) {
        this.base = base;
    }
}
