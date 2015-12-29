package com.busradeniz.nightswatch.service.fileupload;

import com.squareup.okhttp.RequestBody;

import java.util.Map;

import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by busradeniz on 28/12/15.
 */
public interface FileUploadService {

    @Multipart
    @POST("/nights-watch/media/upload")
    Observable<Media> upload(@Query("mediaType") String mediaType , @PartMap() Map<String,RequestBody> mapFileAndName );
}
