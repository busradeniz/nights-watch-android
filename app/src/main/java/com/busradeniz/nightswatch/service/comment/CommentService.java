package com.busradeniz.nightswatch.service.comment;

import com.busradeniz.nightswatch.service.watch.Watch;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by busradeniz on 29/12/15.
 */
public interface CommentService {

    @POST("/nights-watch/userWatch")
    Observable<Watch> watch(@Body Watch watch);

}
