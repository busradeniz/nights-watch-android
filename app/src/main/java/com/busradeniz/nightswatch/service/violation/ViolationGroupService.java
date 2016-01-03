package com.busradeniz.nightswatch.service.violation;

import com.busradeniz.nightswatch.service.Response;
import retrofit.http.*;
import rx.Observable;

import java.util.List;

public interface ViolationGroupService {

    @GET("/nights-watch/violationGroup")
    Observable<List<ViolationGroup>> getAll();

    @POST("/nights-watch/violationGroup")
    Observable<ViolationGroup> create(@Body ViolationGroup violationGroup);

    @DELETE("/nights-watch/violationGroup/{id}")
    Observable<Response> delete(@Path("id") int id);

    @PUT("/nights-watch/violationGroup/{id}")
    Observable<ViolationGroup> update(@Path("id") int id, @Body ViolationGroup violationGroup);
}
