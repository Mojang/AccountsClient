package com.mojang.api.profiles;

public class ProfileCriteria {

    private final String[] name;

    @Deprecated
    public ProfileCriteria(String name, String agent) {
        this(name);
    }

    @Deprecated
    public String getAgent() {
        return null;
    }

    @Deprecated
    public String getName() {
        return name[0];
    }

    public ProfileCriteria(String... name) {
        this.name = name;
    }

    public String[] getNames() {
        return name;
    }
}
