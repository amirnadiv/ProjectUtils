package com.amirnadiv.project.utils.common.division;

import java.io.Serializable;
import java.util.List;

import com.amirnadiv.project.utils.common.lang.ToString;

public class Division extends ToString implements Serializable, Comparable<Division> {
    private static final long serialVersionUID = -7459417562260342641L;

    private int divisionId;

    private String divisionName;
    private String divisionAbbName;
    private String divisionZip;

    private List<Division> childDivision;

    private Division parentDivision;

    public int compareTo(Division other) {
        if (other == null) {
            return -1;
        }

        return (divisionId < other.divisionId ? -1 : (divisionId == other.divisionId ? 0 : 1));

    }

    public List<Division> getChildDivision() {
        return childDivision;
    }

    public void setChildDivision(List<Division> childDivision) {
        this.childDivision = childDivision;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDivisionAbbName() {
        return divisionAbbName;
    }

    public void setDivisionAbbName(String divisionAbbName) {
        this.divisionAbbName = divisionAbbName;
    }

    public String getDivisionZip() {
        return divisionZip;
    }

    public void setDivisionZip(String divisionZip) {
        this.divisionZip = divisionZip;
    }

    public Division getParentDivision() {
        return parentDivision;
    }

    public void setParentDivision(Division parentDivision) {
        this.parentDivision = parentDivision;
    }

}
