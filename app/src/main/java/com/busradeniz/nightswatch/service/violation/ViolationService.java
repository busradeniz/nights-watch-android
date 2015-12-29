package com.busradeniz.nightswatch.service.violation;

import com.busradeniz.nightswatch.service.fileupload.Media;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
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
    Observable<List<ViolationResponse>> getUserWatchedViolations(@Query("violationStatus") String violationStatus);

    @GET("/nights-watch/violationGroup")
    Observable<List<ViolationGroup>> getViolationGroups();

    @POST("/nights-watch/violation")
    Observable<ViolationResponse> createNewViolation(@Body CreateViolationRequest createViolationRequest);

    @POST("/nights-watch/violation/{id}/addMedia")
    Observable<ViolationResponse> addMediaToViolation(@Path("id") int id, @Body Media media);


}
