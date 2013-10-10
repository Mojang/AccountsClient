package com.mojang.api.profiles;

public class ProfileCriteria {

    private final String name;
    private final String agent;

    public ProfileCriteria(String name, String agent) {
        this.name = name;
        this.agent = agent;
    }

    public String getAgent() {
        return agent;
    }

    public String getName() {
        return name;
    }
}
