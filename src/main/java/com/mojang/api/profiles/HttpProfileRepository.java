package com.mojang.api.profiles;

import com.google.gson.Gson;
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

    private static final int MAX_PAGES_TO_CHECK = 100;
    private static Gson gson = new Gson();
    private HttpClient client;

    public HttpProfileRepository() {
        this(BasicHttpClient.getInstance());
    }

    public HttpProfileRepository(HttpClient client) {
        this.client = client;
    }

    @Override
    public Profile[] findProfilesByCriteria(ProfileCriteria... criteria) {
        try {
            HttpBody body = new HttpBody(gson.toJson(criteria));
            List<HttpHeader> headers = new ArrayList<HttpHeader>();
            headers.add(new HttpHeader("Content-Type", "application/json"));
            List<Profile> profiles = new ArrayList<Profile>();
            for (int i = 1; i <= MAX_PAGES_TO_CHECK; i++) {
                ProfileSearchResult result = post(new URL("https://api.mojang.com/profiles/page/" + i), body, headers);
                if (result.getSize() == 0) {
                    break;
                }
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
        return gson.fromJson(response, ProfileSearchResult.class);
    }

}
