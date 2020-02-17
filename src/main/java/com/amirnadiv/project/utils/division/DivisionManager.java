package com.amirnadiv.project.utils.common.division;

import java.util.List;

public interface DivisionManager {


    public void reloadDivision() throws DivisionException;

    public Division getDivisionById(int id);

    public List<Division> getDivisionByZip(String zip);

    public List<Division> getDivisionByName(String name);

    public List<Division> getDivisionByAbbName(String abbName);

    public List<Division> getObscureDivisionByName(String name);

    public List<Division> getObscureDivisionByTName(String name);

    public List<Division> getDivisionByTName(String name);

    public List<Division> getProvinceDisivion();

    public boolean isProvinceDivision(int id);

    public boolean isProvinceNameDivision(String provName);

    public boolean isCityDivision(int id);

    public boolean isCityNameDivision(String cityName);

    public boolean isRegionDivision(int id);

    public boolean isRegionNameDivision(String regionName);

}
