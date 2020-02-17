package com.amirnadiv.project.utils.common.access;

import java.util.Map;
import java.util.Set;

public interface ResourceHeader {

    String encode();

    String ext();

    String getAttribute(String name);

    ResourceHeader setAttribute(String name, String value);

    Set<Map.Entry<String, String>> getInfo();

    ResourceHeader encode(String encode);

    ResourceHeader ext(String ext);

}
