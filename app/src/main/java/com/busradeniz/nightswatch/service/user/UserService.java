package com.busradeniz.nightswatch.service.user;

import com.busradeniz.nightswatch.service.signup.SignUpRequest;
import com.busradeniz.nightswatch.service.signup.SignUpResponse;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by busradeniz on 27/12/15.
 */
public interface UserService {

    @GET("/nights-watch/user/{userId}")
    Observable<SignUpResponse> getUserInfo(@Path("userId") int userId);


    @PUT ("/nights-watch/user/{userId}")
    Observable<SignUpResponse> updateUserInfo(@Path("userId") int userId , @Body SignUpResponse signUpRequest);


    @POST("/nights-watch/user/changePassword")
    Observable<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);




}
