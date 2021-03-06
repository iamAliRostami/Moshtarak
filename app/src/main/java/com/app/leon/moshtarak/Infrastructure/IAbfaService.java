package com.app.leon.moshtarak.Infrastructure;

import com.app.leon.moshtarak.Models.DbTables.Cardex;
import com.app.leon.moshtarak.Models.DbTables.Counter;
import com.app.leon.moshtarak.Models.DbTables.FollowUpDto;
import com.app.leon.moshtarak.Models.DbTables.LastBillInfo;
import com.app.leon.moshtarak.Models.DbTables.LastBillInfoV2;
import com.app.leon.moshtarak.Models.DbTables.Login;
import com.app.leon.moshtarak.Models.DbTables.MemberInfo;
import com.app.leon.moshtarak.Models.DbTables.RegisterAsDto;
import com.app.leon.moshtarak.Models.DbTables.RegisterNewDto;
import com.app.leon.moshtarak.Models.DbTables.Request;
import com.app.leon.moshtarak.Models.DbTables.Service;
import com.app.leon.moshtarak.Models.DbTables.Session;
import com.app.leon.moshtarak.Models.DbTables.Suggestion;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;

import java.util.ArrayList;
import java.util.List;

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

    @POST("/MoshtarakinApi/V2/HamrahAbfaManager/Register")
    Call<Login> register(
            @Body Login login
    );

    @GET("/MoshtarakinApi/V2/Member/IsValid/{api}/{billId}")
    Call<Login> register(
            @Path("api") String api,
            @Path("billId") String billId
    );

    @GET("/MoshtarakinApi/Member/GetInfo/{billId}")
    Call<MemberInfo> getInfo(
            @Path("billId") String billId
    );

    @GET("/MoshtarakinApi/V2/Member/GetInfo/{api}/{billId}")
    Call<MemberInfo> getInfo(
            @Path("billId") String billId,
            @Path("api") String apiKey
    );

    @GET("/MoshtarakinApi/V2/Bill/GetKardex/{api}/{billId}")
    Call<ArrayList<Cardex>> getKardex(
            @Path("billId") String billId,
            @Path("api") String apiKey
    );

    @GET("/MoshtarakinApi/Bill/GetKardex/{billId}")
    Call<ArrayList<Cardex>> getKardex(
            @Path("billId") String billId
    );

    @GET("/MoshtarakinApi/Bill/GetBill/{billId}")
    Call<LastBillInfo> getLastBillInfo(
            @Path("billId") String billId
    );

    @GET("/MoshtarakinApi/V2/Bill/Get/{api}/{billId}")
    Call<LastBillInfoV2> getLastBillInfo(
            @Path("billId") String billId,
            @Path("api") String apiKey

    );

    @GET("/MoshtarakinApi/Bill/GetThisBill?")
    Call<LastBillInfoV2> getThisBillInfo(
            @Query("id") String id,
            @Query("zoneId") String zoneId
    );

    @GET("/MoshtarakinApi/Bill/GetThisPayInfo?")
    Call<LastBillInfoV2> getThisPayInfo(
            @Query("id") String id,
            @Query("zoneId") String zoneId
    );

    //    @POST("/MoshtarakinApi/Bill/GenerateBill")
    @POST("/MoshtarakinApi/V2/Bill/GenerateBill/{apiKey}")
    Call<LastBillInfo> sendNumber(
            @Path("apiKey") String apiKey,
            @Body Counter counter
    );

    @PUT("/MoshtarakinApi/RequestManager/RegisterNew")
    Call<SimpleMessage> registerNew(@Body RegisterNewDto registerNewDto);

    @PUT("/MoshtarakinApi/RequestManager/RegisterAS")
    Call<SimpleMessage> registerAS(@Body RegisterAsDto registerAsDto);

    @GET("/MoshtarakinApi/RequestManager/GetDictionary?serviceGroupId=2")
    Call<ArrayList<Service>> getDictionary();


    @GET("/MoshtarakinApi/RequestManager/GetTrackings/{trackNumber}")
    Call<ArrayList<FollowUpDto>> followingUp(
            @Path("trackNumber") String trackNumber
    );

    @POST("/MoshtarakinApi/SuggestionManager/SetSuggestion")
    Call<SimpleMessage> sendSuggestion(//type , message, OS Version, Phone model
                                       @Body Suggestion suggestionInput
    );

    @GET("/MoshtarakinApi/V2/RequestManager/GetAll/{api}/{billId}")
    Call<List<Request>> getAllRequests(
            @Path("billId") String billId,
            @Path("api") String apiKey
    );

    @GET("/MoshtarakinApi/Member/GetAllRequests?")
    Call<List<Request>> getAllRequests(
            @Query("billId") String billId
    );

    @GET("/MoshtarakinApi/HamrahAbfaManager/GetSesssions?")
    Call<ArrayList<Session>> getSessions(
            @Query("billId") String billId
    );

    @POST("/MoshtarakinApi/V2/Payment/GetTokenAb/{apiKey}")
    Call<SimpleMessage> getToken(
            @Path("apiKey") String apiKey
    );
}
