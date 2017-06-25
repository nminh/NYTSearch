package com.nminh.nytsearch.utils;

import com.nminh.nytsearch.BuildConfig;
import com.nminh.nytsearch.models.ApiResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitUtils {

    private static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private static final Gson GSON = new Gson();

    // Create retrofit instance
    public static Retrofit get() {

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .client(client())
                .build();
    }

    // Make client
    public static OkHttpClient client() {
        return new OkHttpClient.Builder()
                .addInterceptor(apiInterceptor())
                .addInterceptor(apiKeyInterceptor())
                .build();
    }

    private static Interceptor apiKeyInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // Send request and proceed the response
                Request request = chain.request();
                Response response = chain.proceed(request);
                // Converts JSON String to ApiResponse.class using GSON (Serializable etc.)

                // problem in this line
                ApiResponse apiResponse = GSON.fromJson(response.body().string(), ApiResponse.class);
//                apiResponse.getResponse();

                String a = GSON.toJson(apiResponse.getResponse());

                // Then get only Response part from the apiResponse object and convert back to JSON
                return response.newBuilder()
                        .body(ResponseBody.create(mediaType, GSON.toJson(apiResponse.getResponse())))
                        .build();
            }
        };
    }


    // Extra method for building request
    private static Interceptor apiInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                HttpUrl url = request.url()
                        .newBuilder()
                        .addQueryParameter("api-key", BuildConfig.API_KEY)
                        .build();
                request = request.newBuilder()
                        .url(url)
                        .build();

                return chain.proceed(request);
            }
        };
    }
}

