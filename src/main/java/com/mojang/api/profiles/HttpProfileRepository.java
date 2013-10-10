package com.mojang.api.profiles;

import com.google.gson.Gson;
import com.mojang.api.http.BasicHttpClient;
import com.mojang.api.http.HttpBody;
import com.mojang.api.http.HttpHeader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpProfileRepository implements ProfileRepository {

    private static Gson gson = new Gson();

    @Override
    public Profile[] findProfilesByCriteria(ProfileCriteria... criteria) {
        try {
            HttpBody body = new HttpBody(gson.toJson(criteria));
            List<HttpHeader> headers = new ArrayList<HttpHeader>();
            headers.add(new HttpHeader("Content-Type", "application/json"));
            String response = BasicHttpClient.getInstance().post(new URL("https://api.mojang.com/profiles"), body, headers);
            ProfileSearchResult result = gson.fromJson(response, ProfileSearchResult.class);

            return result.getProfiles();
        } catch (Exception e) {
            // TODO: logging and allowing consumer to react?
            return new Profile[0];
        }
    }

}
