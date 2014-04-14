package com.mojang.api.profiles;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mojang.api.http.BasicHttpClient;
import com.mojang.api.http.HttpBody;
import com.mojang.api.http.HttpClient;
import com.mojang.api.http.HttpHeader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpProfileRepository implements ProfileRepository {

    private static final int MAX_NAMES_LENGTH = 100;
    private static Gson gson = new Gson();
    private HttpClient client;

    public HttpProfileRepository() {
        this(BasicHttpClient.getInstance());
    }

    public HttpProfileRepository(HttpClient client) {
        this.client = client;
    }

    @Override
    public Profile[] findProfilesByCriteria(ProfileCriteria... profileCriteria) {
        try {
            List<Profile> profiles = new ArrayList<Profile>();
            for(ProfileCriteria criteria : profileCriteria) {
                if(criteria.getNames().length > MAX_NAMES_LENGTH) {
                    throw new RuntimeException("Maximum length in a single query is 100, please use multiple queries.");
                }
                HttpBody body = new HttpBody(gson.toJson(criteria.getNames()));
                List<HttpHeader> headers = new ArrayList<HttpHeader>();
                headers.add(new HttpHeader("Content-Type", "application/json"));
                ProfileSearchResult result = post(new URL("https://api.mojang.com/profiles/minecraft"), body, headers);
                profiles.addAll(Arrays.asList(result.getProfiles()));
            }
            return profiles.toArray(new Profile[profiles.size()]);
        } catch (Exception e) {
            // TODO: logging and allowing consumer to react?
            return new Profile[0];
        }
    }

    private ProfileSearchResult post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException {
        String response = client.post(url, body, headers);
        JsonParser parser = new JsonParser();
        JsonArray arr = (JsonArray) parser.parse(response);
        Profile[] profiles = new Profile[arr.size()];
        for(int i=0; i<profiles.length; i++) {
            profiles[i] = gson.fromJson(arr.get(i), Profile.class);
        }
        return new ProfileSearchResult(profiles);
    }

}
