package com.amirnadiv.project.utils.common.able;

public interface Transformer<FROM, TO> {
    TO transform(FROM from);
}
