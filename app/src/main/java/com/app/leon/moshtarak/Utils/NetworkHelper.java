package com.app.leon.moshtarak.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {
    private static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static long READ_TIMEOUT = 120;
    private static long WRITE_TIMEOUT = 60;
    private static long CONNECT_TIMEOUT = 10;
    private static boolean RETRY_ENABLED = true;

    private NetworkHelper() {

    }

    private static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(READ_TIMEOUT, TIME_UNIT)
                .writeTimeout(WRITE_TIMEOUT, TIME_UNIT)
                .connectTimeout(CONNECT_TIMEOUT, TIME_UNIT)
                .retryOnConnectionFailure(RETRY_ENABLED)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
//                        return chain.proceed(request);
//                    }
//                })
                .addInterceptor(interceptor).build();
        return client;
    }

    public static Retrofit getInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        String baseUrl = "http://37.191.92.130/";
        return new Retrofit.Builder()
                .baseUrl("https://www.abfaesfahan.ir")
                .client(NetworkHelper.getHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}
