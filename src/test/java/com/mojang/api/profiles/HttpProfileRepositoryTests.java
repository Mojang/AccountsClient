package com.mojang.api.profiles;

import com.google.gson.Gson;
import com.mojang.api.http.HttpBody;
import com.mojang.api.http.HttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
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
    public void findProfilesByCriteria_profileCriteriaSpanningSeveralPagesProvided_returnsAllProfiles() throws Exception{
        client = mock(HttpClient.class);
        setProfilesForUrl(client, new URL("https://api.mojang.com/profiles/page/1"), new Profile[50]);
        setProfilesForUrl(client, new URL("https://api.mojang.com/profiles/page/2"), new Profile[23]);
        setProfilesForUrl(client, new URL("https://api.mojang.com/profiles/page/3"), new Profile[0]);
        HttpProfileRepository repository = new HttpProfileRepository(client);
        ProfileCriteria criteria = new ProfileCriteria("someName", "someAgent");

        Profile[] actual = repository.findProfilesByCriteria(criteria);

        assertThat(actual.length, is(equalTo(73)));
    }

    private void setProfilesForUrl(HttpClient mock, URL url, Profile[] profiles) throws IOException {
        ProfileSearchResult results = new ProfileSearchResult();
        results.setProfiles(profiles);
        results.setSize(profiles.length);
        String jsonString = gson.toJson(results);
        when(mock.post(eq(url), any(HttpBody.class), anyList())).thenReturn(jsonString);
    }

}
