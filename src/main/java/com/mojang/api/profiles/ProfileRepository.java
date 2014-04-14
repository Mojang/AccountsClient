package com.mojang.api.profiles;

public interface ProfileRepository {
    public Profile[] findProfilesByNames(String... names);
}
