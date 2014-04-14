package com.mojang.api.profiles;

import com.google.gson.Gson;
import com.mojang.api.http.HttpBody;
import com.mojang.api.http.HttpClient;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class HttpProfileRepositoryTests {

    private HttpClient client;
    private Gson gson = new Gson();

    @Test
    public void findProfilesByCriteria_someProfileNames_returnsExpectedProfiles() throws Exception{
        client = mock(HttpClient.class);
        String someAgent = "someAgent";

        Profile someProfile = getProfile("someName");
        Profile someOtherProfile = getProfile("someOtherName");
        Profile[] profiles = {someProfile, someOtherProfile};

        setProfilesForUrl(client, new URL("https://api.mojang.com/profiles/" + someAgent), profiles);
        ProfileRepository repository = new HttpProfileRepository(someAgent, client);

        Profile[] actual = repository.findProfilesByNames("someName", "someOtherName");

        assertThat(actual.length, is(equalTo(2)));
        assertThat(actual, hasItemInArray(hasProperty("name", CoreMatchers.is("someName"))));
        assertThat(actual, hasItemInArray(hasProperty("name", CoreMatchers.is("someOtherName"))));
    }

    private void setProfilesForUrl(HttpClient mock, URL url, Profile[] profiles) throws IOException {
        String jsonString = gson.toJson(profiles);
        when(mock.post(eq(url), any(HttpBody.class), anyList())).thenReturn(jsonString);
    }

    private static Profile getProfile(String name) {
        Profile profile = new Profile();
        profile.setName(name);
        return profile;
    }

}
