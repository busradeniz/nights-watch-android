package com.busradeniz.nightswatch.util;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;


public class LoggingInterceptor implements Interceptor {

    private AtomicLong atomicLong = new AtomicLong(0L);

    public Response intercept(Interceptor.Chain chain) throws IOException {
        final long id = atomicLong.incrementAndGet();
        Request request = chain.request();
        printRequest(id, request);

        Response response = chain.proceed(request);
        printResponse(id, response);

        return response;
    }

    private void printResponse(long id, Response response) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nResponse------------------------------");
        stringBuilder.append("\nID: " + id);
        stringBuilder.append("\nResponse-Code: ");
        stringBuilder.append(response.code());
        stringBuilder.append("\nMessage: ");
        stringBuilder.append(response.message());
        stringBuilder.append("\nMethod: ");
        stringBuilder.append(response.request().method());
        stringBuilder.append("\nAddress: ");
        stringBuilder.append(response.request().urlString());
        stringBuilder.append("\nHeaders: {\n\t");
        stringBuilder.append(response.headers().toString().replace("\n", "\n\t"));
        stringBuilder.append("}");
        stringBuilder.append("\n--------------------------------------");
        System.out.println(stringBuilder.toString());
    }

    private void printRequest(long id, Request request) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nRequest------------------------------");
        stringBuilder.append("\nID: " + id);
        stringBuilder.append("\nAddress: ");
        stringBuilder.append(request.urlString());
        stringBuilder.append("\nMethod: ");
        stringBuilder.append(request.method());
        if (request.body() != null) {
            stringBuilder.append("\nContent-Type: " + request.body().contentType());
        }
        stringBuilder.append("\nHeaders: {\n\t");
        stringBuilder.append(request.headers().toString().replace("\n", "\n\t"));
        stringBuilder.append("}");
        stringBuilder.append("\n--------------------------------------");
        System.out.println(stringBuilder.toString());
    }

}
