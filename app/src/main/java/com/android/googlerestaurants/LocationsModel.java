package com.android.googlerestaurants;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationsModel {

    public LocationsModel()
    {

    }
    @SerializedName("Latitude")
    String latitude;

    @SerializedName("Longitude")
    String longitude;

    @SerializedName("Banners")
    String resImage;



    @SerializedName("Name")
    String name;

    @SerializedName("Response")
    private List<LocationsModel> response;

    public List<LocationsModel> getResponse() {
        return response;
    }

    public void setResponse(List<LocationsModel> response) {
        this.response = response;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getResImage() {
        return resImage;
    }

    public void setResImage(String resImage) {
        this.resImage = resImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
