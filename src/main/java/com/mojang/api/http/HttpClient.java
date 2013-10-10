package com.mojang.api.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public interface HttpClient {
    public String post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException;
    public String post(URL url, Proxy proxy, HttpBody body, List<HttpHeader> headers) throws IOException;
}
