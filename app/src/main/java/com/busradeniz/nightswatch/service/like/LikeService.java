package com.busradeniz.nightswatch.service.like;

import com.busradeniz.nightswatch.service.Response;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by busradeniz on 29/12/15.
 */
public interface LikeService {

    @POST("/nights-watch/userLike")
    Observable<Like> like(@Body Like like);

    @DELETE ("/nights-watch/userLike/{id}")
    Observable<Response> unlike(@Path("id") int likeId);
}
