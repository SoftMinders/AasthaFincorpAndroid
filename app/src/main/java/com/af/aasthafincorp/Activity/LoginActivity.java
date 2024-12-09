package com.af.aasthafincorp.Activity;

//import static com.af.aasthafincorp.Utility.ConstantClass.Token;
//import static com.af.aasthafincorp.Utility.ConstantClass.token;

import static com.af.aasthafincorp.Utility.ConstantClass.Token;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.af.aasthafincorp.Interface.RestServices;
import com.af.aasthafincorp.MainActivity;
import com.af.aasthafincorp.R;
import com.af.aasthafincorp.Utility.ApiClient;
import com.af.aasthafincorp.Utility.ConstantClass;
import com.af.aasthafincorp.Utility.SaveSharedPreferences;
import com.af.aasthafincorp.Utility.UtilityClass;
import com.google.android.datatransport.runtime.dagger.multibindings.ElementsIntoSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.google.firebase.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText edt_empid, ed_password;
    ImageView show_pass_btn;
    Button btnLogin;
    UtilityClass utilityClass;
    String eid = "", pwd = "", unid = "", ui = "";
    Retrofit retrofit;
    RestServices restServices;
    String TAG = "LoginActivity";
    String devIdString = "Test";
    String UserLoginId, UserLoginPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        utilityClass = new UtilityClass(LoginActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            unid = bundle.getString("unid");
        }
        UserLoginId = SaveSharedPreferences.getloginId(LoginActivity.this);
        UserLoginPwd = SaveSharedPreferences.getloginPwd(LoginActivity.this);
        intilaization();
    }

    private void intilaization() {
        edt_empid = findViewById(R.id.edt_empid);
        ed_password = findViewById(R.id.ed_password);
        show_pass_btn = findViewById(R.id.show_pass_btn);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "getUserValidate->requestObject->");
                CallVerification();
            }
        });
    }

    private void CallVerification() {
        eid = edt_empid.getText().toString();
        pwd = ed_password.getText().toString();
        SaveSharedPreferences.set_loginId(LoginActivity.this, eid);
        SaveSharedPreferences.set_loginPwd(LoginActivity.this, pwd);

        if (eid.trim().isEmpty()) {
            edt_empid.setError("Please Enter Your Employee ID");
        } else if (pwd.trim().isEmpty()) {
            ed_password.setError("Please Enter Your Password");
        } else {

            if(UserLoginId != "" && UserLoginPwd != ""){
                signin(UserLoginId,UserLoginPwd);
            }

        }
    }

    private void signin(String eid, String pwd) {
        utilityClass.processDialogStart();
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("username", eid);
            requestObject.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody un = RequestBody.create(MediaType.parse("text/plain"), requestObject.optString("username"));
        RequestBody pw = RequestBody.create(MediaType.parse("text/plain"), requestObject.optString("password"));
        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getLogin(Token, un, pw);
        Log.d("Test", "getUserValidate->requestObject->" + requestObject.toString());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                if (response.isSuccessful()) {
                    JSONObject responseObject = new JSONObject();
                    JSONObject Data = new JSONObject();
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("Test", "getLogin->responseObject:" + responseObject);
                        if (responseObject.optString("success").equalsIgnoreCase("1")) {
                            if (responseObject.has("user")) {
                                Data = responseObject.optJSONObject("user");
                            }
                            if (Data.length() > 0) {
                                SaveSharedPreferences.setUser_id(LoginActivity.this, Data.optString("user_id"));
                                SaveSharedPreferences.setE_id(LoginActivity.this, Data.optString("eid"));
                                SaveSharedPreferences.setUname(LoginActivity.this, Data.optString("name"));
                                SaveSharedPreferences.setBranch(LoginActivity.this, Data.optString("branch"));
                                SaveSharedPreferences.setEmail(LoginActivity.this, Data.optString("email"));
                                SaveSharedPreferences.setMobile(LoginActivity.this, Data.optString("mobile"));
                                SaveSharedPreferences.setDepart(LoginActivity.this, Data.optString("department"));
                                SaveSharedPreferences.setVerif(LoginActivity.this, Data.optString("verification"));
                                SaveSharedPreferences.setRedi_Url(LoginActivity.this, Data.optString("redirect_url"));
                                SaveSharedPreferences.setUserObject(LoginActivity.this, Data.toString());
                                ui = Data.optString("user_id");
                                CallAddDeviceID();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            CallAddDeviceID();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                utilityClass.processDialogStart();
                Toast.makeText(LoginActivity.this, "Server Error! Try Again Later!", Toast.LENGTH_SHORT).show();
                Log.d("Test", "getLogin->error->" + t.toString());
                utilityClass.processDialogStop();
            }
        });
    }


    private void CallAddDeviceID() {

        utilityClass.processDialogStart();

        String did = SaveSharedPreferences.getUniqueID(LoginActivity.this);
        String uid = SaveSharedPreferences.getUser_id(LoginActivity.this);

        JSONObject requestObject = new JSONObject();

        try {
            requestObject.put("user_id", uid);
            requestObject.put("device_id", did);
        } catch (Exception e) {
            e.printStackTrace();
        }


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        utilityClass.processDialogStop(); // Stop the dialog here
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String devIdString = task.getResult();

                        // Log the token
                        Log.d(TAG, "Firebase Token: " + devIdString);
                       // Toast.makeText(LoginActivity.this, "Firebase token: " + devIdString, Toast.LENGTH_SHORT).show();

                        // Now create the request body with the FCM token
                        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), uid);
                        RequestBody devId = RequestBody.create(MediaType.parse("text/plain"), devIdString);

                        Log.d(TAG, "Device Id : " + devId);

                        //SaveSharedPreferences.setUniqueID(LoginActivity.this,devIdString);//Added nidhi

                        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);

                        Call<JsonObject> call = restServices.getDeviceId(Token, userid, devId);


                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                                if (response.isSuccessful()) {

                                    Toast.makeText(LoginActivity.this, "Response==" + response.toString(), Toast.LENGTH_SHORT).show();

                                    try {

                                        JSONObject responseObject = new JSONObject(response.body().toString());
                                        Log.d("Test", "getAddDeviceID->responseObject:" + responseObject);
                                      //  Toast.makeText(LoginActivity.this, responseObject.optString("message"), Toast.LENGTH_SHORT).show();

                                        if (responseObject.optString("success").equalsIgnoreCase("1")) {
                                            Toast.makeText(LoginActivity.this, responseObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                       // Toast.makeText(LoginActivity.this, "CallAdddeviceid:Failure" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.d("Test", "getAddDeviceID->error->" + t.toString());
                            }
                        });
                    }
                });

        utilityClass.processDialogStop(); // Stop the dialog in case of an early exit
    }
    public void ShowHidePass(View view) {

        if (view.getId() == R.id.show_pass_btn) {

            if (ed_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.hidden);

                //Show Password
                ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.eye);
                //Hide Password
                ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }


    }
}