package com.busradeniz.nightswatch.service.watch;

import com.busradeniz.nightswatch.service.like.Like;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by busradeniz on 29/12/15.
 */
public interface WatchService {

    @POST("/nights-watch/userWatch")
    Observable<Watch> watch(@Body Watch watch);
}
