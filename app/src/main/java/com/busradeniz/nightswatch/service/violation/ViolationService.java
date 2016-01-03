package com.busradeniz.nightswatch.service.violation;

import com.busradeniz.nightswatch.service.Response;
import com.busradeniz.nightswatch.service.fileupload.Media;
import com.busradeniz.nightswatch.service.like.Like;
import com.busradeniz.nightswatch.service.watch.Watch;
import retrofit.http.*;
import rx.Observable;

import java.util.List;

/**
 * Created by busradeniz on 27/12/15.
 */
public interface ViolationService {

    @GET("/nights-watch/violation")
    Observable<List<ViolationResponse>> getNearbyViolations(@Query("longitude") double longitude, @Query("latitude") double latitude);


    @GET("/nights-watch/violation/top20/mostLiked")
    Observable<List<ViolationResponse>> getTopViolations();


    @GET("/nights-watch/violation/top20/newest")
    Observable<List<ViolationResponse>> getNewestViolations();

    @GET("/nights-watch/violation/top20/owned")
    Observable<List<ViolationResponse>> getUserViolations(@Query("violationStatus") String[] violationStatus);

    @GET("/nights-watch/violation/top20/watched")
    Observable<List<ViolationResponse>> getUserWatchedViolations(@Query("violationStatus") String[] violationStatus);

    @GET("/nights-watch/violationGroup")
    Observable<List<ViolationGroup>> getViolationGroups();

    @POST("/nights-watch/violation")
    Observable<ViolationResponse> createNewViolation(@Body CreateViolationRequest createViolationRequest);


    @PUT("/nights-watch/violation/{id}")
    Observable<ViolationResponse> updateViolation(@Path("id") int violationId , @Body CreateViolationRequest createViolationRequest);


    @POST("/nights-watch/violation/{id}/addMedia")
    Observable<ViolationResponse> addMediaToViolation(@Path("id") int id, @Body Media media);


    @GET("/nights-watch/violation/{id}/userLikes")
    Observable<List<Like>> getViolationLikes(@Path("id") int id);


    @GET("/nights-watch/violation/{id}/userWatches")
    Observable<List<Watch>> getViolationWatches(@Path("id") int id);

    @DELETE("/nights-watch/violation/{id}")
    Observable<Response> deleteViolation(@Path("id") int id);
}