package com.busradeniz.nightswatch.service.watch;

import com.busradeniz.nightswatch.service.Response;
import com.busradeniz.nightswatch.service.like.Like;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by busradeniz on 29/12/15.
 */
public interface WatchService {

    @POST("/nights-watch/userWatch")
    Observable<Watch> watch(@Body Watch watch);

    @DELETE("/nights-watch/userWatch/{id}")
    Observable<Response> unWatch(@Path("id") int id);
}
