package com.busradeniz.nightswatch.service.violation;

import com.busradeniz.nightswatch.service.fileupload.Media;
import com.busradeniz.nightswatch.service.like.Like;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Created by busradeniz on 27/12/15.
 */
public interface ViolationService {

    @GET("/nights-watch/violation")
    Observable<List<ViolationResponse>> getNearbyViolations(@Query("longitude") double longitude , @Query("latitude") double latitude);


    @GET("/nights-watch/violation/top20/mostLiked")
    Observable<List<ViolationResponse>> getTopViolations();


    @GET("/nights-watch/violation/top20/newest")
    Observable<List<ViolationResponse>> getNewestViolations();

    @GET("/nights-watch/violation/top20/owned")
    Observable<List<ViolationResponse>> getUserViolations();

    @GET("/nights-watch/violation/top20/watched")
    Observable<List<ViolationResponse>> getUserWatchedViolations(@QueryMap Map<String,String> violationStatus);

    @GET("/nights-watch/violationGroup")
    Observable<List<ViolationGroup>> getViolationGroups();

    @POST("/nights-watch/violation")
    Observable<ViolationResponse> createNewViolation(@Body CreateViolationRequest createViolationRequest);

    @POST("/nights-watch/violation/{id}/addMedia")
    Observable<ViolationResponse> addMediaToViolation(@Path("id") int id, @Body Media media);


    @GET("/nights-watch/violation/{id}/userLikes")
    Observable<List<Like>> getViolationLikes(@Path("id") int id);
}
