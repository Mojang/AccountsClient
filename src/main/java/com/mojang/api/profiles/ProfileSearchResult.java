package com.mojang.api.profiles;

public class ProfileSearchResult {
    private Profile[] profiles;
    private int size;

    public Profile[] getProfiles() {
        return profiles;
    }

    public void setProfiles(Profile[] profiles) {
        this.profiles = profiles;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
