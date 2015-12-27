package com.busradeniz.nightswatch.service.violation;

import com.busradeniz.nightswatch.service.signup.SignUpResponse;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by busradeniz on 27/12/15.
 */
public interface ViolationService {

    @GET("/nights-watch/violation")
    Observable<List<Violation>> getNearbyViolations(@Query("longitude") double longitude , @Query("latitude") double latitude);


    @GET("/nights-watch/violation/top20/mostLiked")
    Observable<List<Violation>> getTopViolations();


    @GET("/nights-watch/violation/top20/newest")
    Observable<List<Violation>> getNewestViolations();

    @GET("/nights-watch/violation/top20/owned")
    Observable<List<Violation>> getUserViolations();

    @GET("/nights-watch/violation/top20/watched")
    Observable<List<Violation>> getUserWatchedViolations();



}
