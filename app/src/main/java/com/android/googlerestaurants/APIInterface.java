package com.android.googlerestaurants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("churches/ChurchesBySearch")
    Call<LocationsModel> getLocations(@Query("searchText") String searchText);
}
