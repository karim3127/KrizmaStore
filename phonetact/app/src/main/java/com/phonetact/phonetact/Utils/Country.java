package com.phonetact.phonetact.Utils;

/**
 * Created by hp on 02/06/2015.
 */
public class Country {
    String code = null;
    String name = null;

    public Country(String code, String name) {
        super();
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
