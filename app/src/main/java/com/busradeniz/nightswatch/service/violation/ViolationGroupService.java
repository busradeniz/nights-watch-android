package com.busradeniz.nightswatch.service.violation;

import retrofit.http.GET;
import rx.Observable;

import java.util.List;

public interface ViolationGroupService {

    @GET("/nights-watch/violationGroup")
    Observable<List<ViolationGroup>> getAll();
}
