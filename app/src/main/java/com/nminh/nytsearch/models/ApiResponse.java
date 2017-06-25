package com.nminh.nytsearch.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nminh on 6/24/17.
 */

public class ApiResponse {
    @SerializedName("response")
    private JsonObject response;

    @SerializedName("status")
    private String status;



    public JsonObject getResponse() {
        if (response == null) {
            return new JsonObject();
        }
        return response;
    }

    public String getStatus() {
        return status;
    }
}
