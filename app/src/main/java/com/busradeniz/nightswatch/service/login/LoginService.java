package com.busradeniz.nightswatch.service.login;


import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by busradeniz on 26/12/15.
 */


public interface LoginService {

    //TODO update application context for all services

    @POST("/nights-watch/signin")
    Observable<LoginResponse> signin(@Body LoginRequest loginRequest);

    @POST("/nights-watch/resetPassword")
    Observable<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

}
