package com.af.aasthafincorp.Interface;

import static com.af.aasthafincorp.Utility.ConstantClass.Token;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestServices {

    //1. Login
    @Multipart
    @POST("login")
    Call<JsonObject> getLogin(@Header("token") String Token,
                              @Part("username") RequestBody username,
                              @Part("password") RequestBody password);



    //2. Add device Id
    @Multipart
    @POST("add_device_id")
    Call<JsonObject> getDeviceId(@Header("token") String Token,
                                 @Part("user_id") RequestBody user_id,
                                 @Part("device_id") RequestBody device_id);

    //3. User Tracking
    @Multipart
    @POST("tracking")
    Call<JsonObject> getUSerTracking(@Header("token") String Token,
                                     @Part("user_id") RequestBody user_id,
                                     @Part("lat") RequestBody lat,
                                     @Part("lng") RequestBody lng,
                                     @Part("datetime") RequestBody datetime,
                                     @Part("address") RequestBody address);

    //2. Add device Id
    @Multipart
    @POST("get-dashboard-data")
    Call<JsonObject> getDashboardData(@Header("token") String Token,
                                 @Part("user_id") RequestBody user_id);
    //2. Add device Id
    @Multipart
    @POST("get-all-tasks")
    Call<JsonObject> getAllTasks(@Header("token") String Token,
                                 @Part("user_id") RequestBody user_id);
    //2. Add device Id
    @Multipart
    @POST("get-all-meetings")
    Call<JsonObject> getAllMeetings(@Header("token") String Token,
                                 @Part("user_id") RequestBody user_id);
    //2. Add device Id
    @Multipart
    @POST("get-all-casual-meetings")
    Call<JsonObject> getAllCasualMeetings(@Header("token") String Token,
                                 @Part("user_id") RequestBody user_id);
    //2. Add device Id
    @Multipart
    @POST("get-all-leads")
    Call<JsonObject> getAllLeads(@Header("token") String Token,
                                 @Part("user_id") RequestBody user_id);
    //2. Add device Id
    @Multipart
    @POST("get-all-accounts")
    Call<JsonObject> getAllAccounts(@Header("token") String Token,
                                 @Part("user_id") RequestBody user_id);
    //2. Add device Id
    @Multipart
    @POST("get-all-sales")
    Call<JsonObject> getAllSales(@Header("token") String Token,
                                 @Part("user_id") RequestBody user_id);


    @Multipart
    @POST("add-casual-meeting")
    Call<JsonObject> addCasualMeeting(@Header("token") String Token,
                              @Part("name") RequestBody name,
                              @Part("address1") RequestBody address1,
                                      @Part("area") RequestBody area,
                                      @Part("city") RequestBody city,
                                      @Part("mobile") RequestBody mobile,
                                      @Part("fdate") RequestBody fdate,
                                      @Part("ftime") RequestBody ftime,
                                      @Part("product_id") RequestBody product_id,
                                      @Part("descr") RequestBody descr,
                                      @Part("sale_id") RequestBody sale_id,
                                      @Part("audio") RequestBody audio,
                                      @Part("recordingTime") RequestBody recordingTime,
                                      @Part("converttolead") RequestBody converttolead,
                                      @Part("firm_name") RequestBody firm_name,
                                      @Part("source") RequestBody source,
                                      @Part("src_name") RequestBody src_name,
                                      @Part("src_number") RequestBody src_number


    );
//    @Multipart
//    @POST("upload-recording")
//    Call<JsonObject> uploadRecording(@Header("token") String Token,
//                                     @Part("voice_file") RequestBody voice_file);
@Multipart
@POST("upload-recording")
Call<JsonObject> uploadRecording(@Header("token") String token,
                                 @Part MultipartBody.Part voice_file);

    @POST("get-product-ids") // Replace with your endpoint
    Call<ResponseBody> getProductIds(
            @Header("token") String token
    );



}
