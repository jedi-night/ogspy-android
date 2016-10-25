package com.ogsteam.ogspy.network.download;

import com.ogsteam.ogspy.network.responses.ServerOgspyResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Damien on 24/09/2016.
 */

public interface DownloadInterface {

    @GET("{baseUrl}/server")
    ServerOgspyResponse getServerData();

    @GET("{baseUrl}/alliance/{name}/users")
    Call<List<String>> getAllianceUsers(@Path("baseUrl") int groupId, @Path("name") String allianceName);
}
