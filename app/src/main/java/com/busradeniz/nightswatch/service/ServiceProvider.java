package com.busradeniz.nightswatch.service;

import com.busradeniz.nightswatch.service.login.LoginService;
import com.busradeniz.nightswatch.service.signup.SignUpService;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.LoggingInterceptor;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by busradeniz on 27/12/15.
 */
public class ServiceProvider {

    private static Retrofit retrofit;

    private static void initializeRetrofit() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient();
            LoggingInterceptor interceptor = new LoggingInterceptor();
            client.interceptors().add(interceptor);


            retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static LoginService getLoginService(){
        initializeRetrofit();
        return retrofit.create(LoginService.class);
    }

    public static SignUpService getSignUpService(){
        initializeRetrofit();
        return retrofit.create(SignUpService.class);
    }

}
