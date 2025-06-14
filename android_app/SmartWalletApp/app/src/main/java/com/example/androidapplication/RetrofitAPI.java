package com.example.androidapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    // as we are making a post request to post a data
    // so we are annotating it with post
    // and along with that we are passing a parameter as users
    @POST("billetera")

    //on below line we are creating a method to post our data.
    Call<DataModal> createPost(@Body DataModal dataModal);

    @POST("login")
        //on below line we are creating a method to post our data.
    Call<DataUser> createPost(@Body DataUser dataUser);

    @POST("sensor")
        //on below line we are creating a method to post our data.
    Call<DataSensor> createPost(@Body DataSensor dataSensor);

    @POST("evento")
        //on below line we are creating a method to post our data.
    Call<DataEvent> createPost(@Body DataEvent dataEvent);

    @POST("conexion")
        //on below line we are creating a method to post our data.
    Call<DataConexion> createPost(@Body DataConexion dataConexion);

}
