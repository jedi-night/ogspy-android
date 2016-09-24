package com.ogsteam.ogspy.network.download;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Damien on 24/09/2016.
 */

public interface DownloadInterface {

    @GET("{baseUrl}/mod/xtense/xtense.php")
    String getDataFromServer(
            @Path("baseUrl") String baseUrl,
            @Query("toolbar_version") String toolbarVersion,
            @Query("toolbar_type") String toolbarType,
            @Query("type") String platformType,
            @Query("mod_min_version") String modMinVersion,
            @Query("user") String user,
            @Query("password") String password,
            @Query("univers") String univers,
            @Query("versionAndroid") String androidVersion,
            @Query("versionOgspy") String versionOgspy,
            @Query("device") String device,
            @Query("action") String action
    );

}
