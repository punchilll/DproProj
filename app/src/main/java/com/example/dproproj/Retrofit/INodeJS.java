package com.example.dproproj.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("username") String username,
                                 @Field("password") String password,
                                 @Field("l_name") String l_name,
                                 @Field("f_name") String f_name,
                                 @Field("email") String email,
                                 @Field("tel") String tel);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("username") String username,
                                 @Field("password") String password);

    @POST("service_charge")
    @FormUrlEncoded
    Observable<String> serviceCharge(@Field("size") String size,
                                     @Field("service_charge") String service_charge,
                                     @Field("note") String note,
                                     @Field("pic_evidence") String pic_evidence);

    @POST("history")
    @FormUrlEncoded
    Observable<String> history(@Field("created_at") String created_at);
}
