package com.busradeniz.nightswatch.service.like;

import com.busradeniz.nightswatch.service.login.LoginRequest;
import com.busradeniz.nightswatch.service.login.LoginResponse;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by busradeniz on 29/12/15.
 */
public interface LikeService {

    @POST("/nights-watch/userLike")
    Observable<Like> like(@Body Like like);



}
