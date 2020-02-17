package com.amirnadiv.project.utils.common.able;

import java.util.UUID;

public interface Identifiable {

    void setIdentification(UUID id);

    UUID getIdentification();
}
