package com.busradeniz.nightswatch.service.signup;


import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by busradeniz on 27/12/15.
 */
public interface SignUpService {

    @POST("/nights-watch/signup")
    Observable<SignUpResponse> signup(@Body SignUpRequest signUpRequest);


}
