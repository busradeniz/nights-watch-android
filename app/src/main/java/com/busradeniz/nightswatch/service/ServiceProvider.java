package com.busradeniz.nightswatch.service;

import android.util.Log;
import com.busradeniz.nightswatch.service.fileupload.FileUploadService;
import com.busradeniz.nightswatch.service.like.LikeService;
import com.busradeniz.nightswatch.service.login.LoginService;
import com.busradeniz.nightswatch.service.signup.SignUpService;
import com.busradeniz.nightswatch.service.user.UserService;
import com.busradeniz.nightswatch.service.violation.ViolationGroupService;
import com.busradeniz.nightswatch.service.violation.ViolationService;
import com.busradeniz.nightswatch.service.watch.WatchService;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.LoggingInterceptor;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

import java.io.IOException;

/**
 * Created by busradeniz on 27/12/15.
 */
public class ServiceProvider {

    private static Retrofit retrofit;
    private static Retrofit retrofitInceptor;

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

    private static void initializeRetrofitWithInterceptor() {
        if (retrofitInceptor == null) {
            OkHttpClient client = new OkHttpClient();
            LoggingInterceptor interceptor = new LoggingInterceptor();
            client.interceptors().add(interceptor);
            client.interceptors().add(new Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = null;
                    if (NightsWatchApplication.token != null) {
                        Log.i("Token", NightsWatchApplication.token);
                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", NightsWatchApplication.token)
                                .method(original.method(), original.body());

                        request = requestBuilder.build();
                    }

                    return chain.proceed(request);
                }
            });

            retrofitInceptor = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        }
    }


    public static LoginService getLoginService() {
        initializeRetrofit();
        return retrofit.create(LoginService.class);
    }

    public static SignUpService getSignUpService() {
        initializeRetrofit();
        return retrofit.create(SignUpService.class);
    }

    public static UserService getUserService() {
        initializeRetrofitWithInterceptor();
        return retrofitInceptor.create(UserService.class);
    }

    public static ViolationService getViolationService() {
        initializeRetrofitWithInterceptor();
        return retrofitInceptor.create(ViolationService.class);
    }

    public static ViolationGroupService getViolationGroupService() {
        initializeRetrofitWithInterceptor();
        return retrofitInceptor.create(ViolationGroupService.class);
    }

    public static FileUploadService getFileUploadService() {
        initializeRetrofitWithInterceptor();
        return retrofitInceptor.create(FileUploadService.class);
    }

    public static LikeService getLikeService() {
        initializeRetrofitWithInterceptor();
        return retrofitInceptor.create(LikeService.class);
    }

    public static WatchService getWatchService() {
        initializeRetrofitWithInterceptor();
        return retrofitInceptor.create(WatchService.class);
    }


    public static void reset() {
        retrofit = null;
        retrofitInceptor = null;
    }
}
