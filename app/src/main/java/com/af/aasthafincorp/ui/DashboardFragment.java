package com.af.aasthafincorp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.af.aasthafincorp.Interface.RestServices;
import com.af.aasthafincorp.Utility.ApiClient;
import com.af.aasthafincorp.Utility.SaveSharedPreferences;
import com.af.aasthafincorp.Utility.UtilityClass;
import com.af.aasthafincorp.databinding.FragmentHomeBinding;
import com.google.gson.JsonObject;
import static com.af.aasthafincorp.Utility.ConstantClass.Token;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends Fragment {
    private FragmentHomeBinding binding;
    UtilityClass utilityClass;
    TextView totalMeeting,totalCasualMeetings,totalLeads,totalAccounts;
    LinearLayout meetingsLink,CasualMeetingsLink,LeadsLink,accountsLink;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        totalMeeting = binding.totalMeeting;
        totalCasualMeetings = binding.totalCasualMeetings;
        totalLeads = binding.totalLeads;
        totalAccounts = binding.totalAccounts;

        meetingsLink = binding.meetingsLink;
        CasualMeetingsLink = binding.CasualMeetingsLink;
        LeadsLink = binding.LeadsLink;
        accountsLink = binding.accountsLink;


        utilityClass = new UtilityClass(getContext());
        utilityClass.processDialogStart();

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SaveSharedPreferences.getUser_id(getContext()));
        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getDashboardData(Token,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                JSONObject Data = new JSONObject();
                Log.d("Dashboard", "getDashboardData->responseData:" + response);
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        totalMeeting.setText(responseObject.optString("meetings"));
                        totalCasualMeetings.setText(responseObject.optString("casual_meetings"));
                        totalLeads.setText(responseObject.optString("leads"));
                        totalAccounts.setText(responseObject.optString("accoutns"));
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                utilityClass.processDialogStart();
                Log.d("Test", "getLogin->error->" + t.toString());
                utilityClass.processDialogStop();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}