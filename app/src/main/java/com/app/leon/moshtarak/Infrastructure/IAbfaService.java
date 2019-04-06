package com.app.leon.moshtarak.Infrastructure;

import com.app.leon.moshtarak.Models.DbTables.Kardex;
import com.app.leon.moshtarak.Models.DbTables.LastBillInfo;
import com.app.leon.moshtarak.Models.DbTables.MemberInfo;
import com.app.leon.moshtarak.Models.DbTables.RegisterAsDto;
import com.app.leon.moshtarak.Models.DbTables.RegisterNewDto;
import com.app.leon.moshtarak.Models.DbTables.Service;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAbfaService {
    @GET("/MoshtarakinApi/Member/CanMatch/")
    Call<SimpleMessage> canMatch(
            @Query("billId") String billId,
            @Query("eshterak") String eshterak
    );

    @GET("/MoshtarakinApi/Member/CanMatch/")
    Call<SimpleMessage> register(
            @Query("billId") String billId,
            @Query("eshterak") String eshterak,
            @Query("buildSerial") String buildSerial,
            @Query("appVersion") String appVersion,
            @Query("osVersion") String osVersion,
            @Query("mobile") String mobile,
            @Query("phoneModel") String phoneModel

    );
    @GET("/MoshtarakinApi/Member/GetInfo/{billId}")
    Call<MemberInfo> getInfo(
            @Path("billId") String billId
    );

    @GET("/MoshtarakinApi/Bill/GetKardex/{billId}")
    Call<ArrayList<Kardex>> getKardex(
            @Path("billId") String billId
    );

    @GET("/MoshtarakinApi/Bill/GetBill/{billId}")
    Call<LastBillInfo> getLastBillInfo(
            @Path("billId") String billId
    );

    @POST("/MoshtarakinApi/Bill/GenerateBill")
    Call<LastBillInfo> sendNumber(
            @Query("billId") String billId,
            @Query("counterclaim") String counterclaim,
            @Query("notificationMobile") String notificationMobile
    );

    @PUT("/MoshtarakinApi/RequestManager/RegisterNew")
    Call<SimpleMessage> registerNew(@Body RegisterNewDto registerNewDto);

    @PUT("/MoshtarakinApi/RequestManager/RegisterAS")
    Call<SimpleMessage> registerAS(@Body RegisterAsDto registerAsDto);

    @GET("/MoshtarakinApi/RequestManager/GetDictionary?serviceGroupId=2")
    Call<ArrayList<Service>> getDictionary();
}
