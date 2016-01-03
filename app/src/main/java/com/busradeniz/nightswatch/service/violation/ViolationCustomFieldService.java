package com.busradeniz.nightswatch.service.violation;

import com.busradeniz.nightswatch.service.Response;
import retrofit.http.*;
import rx.Observable;

import java.util.List;

public interface ViolationCustomFieldService {

    @GET("/nights-watch/violationProperty")
    Observable<List<ViolationCustomField>> getAll();

    @POST("/nights-watch/violationProperty")
    Observable<ViolationCustomField> create(@Body ViolationCustomField violationCustomField);

    @DELETE("/nights-watch/violationProperty/{id}")
    Observable<Response> delete(@Path("id") int id);

    @GET("/nights-watch/violationProperty/{id}")
    Observable<ViolationCustomField> get(@Path("id") int id);

    @PUT("/nights-watch/violationProperty/{id}")
    Observable<ViolationCustomField> update(@Path("id") int id, @Body ViolationCustomField violationCustomField);
}
