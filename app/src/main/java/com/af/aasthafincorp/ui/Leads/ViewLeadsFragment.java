package com.af.aasthafincorp.ui.Leads;

import static com.af.aasthafincorp.Utility.ConstantClass.Token;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.af.aasthafincorp.Interface.RestServices;
import com.af.aasthafincorp.R;
import com.af.aasthafincorp.Utility.ApiClient;
import com.af.aasthafincorp.Utility.SaveSharedPreferences;
import com.af.aasthafincorp.Utility.UtilityClass;
import com.af.aasthafincorp.databinding.FragmentCasualMeetingsViewBinding;
import com.af.aasthafincorp.databinding.FragmentLeadsViewBinding;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewLeadsFragment extends Fragment {
    private FragmentLeadsViewBinding binding;
    UtilityClass utilityClass;
    ListView allLeads;
    ArrayList<HashMap<String,String>> Leads = new ArrayList<>();
    SearchView LeadsSearch;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLeadsViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        utilityClass = new UtilityClass(getContext());
        utilityClass.processDialogStart();

        allLeads = binding.allLeads;
        LeadsSearch = binding.LeadsSearch;

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SaveSharedPreferences.getUser_id(getContext()));

        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getAllLeads(Token,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                JSONObject Data = new JSONObject();
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("Leads", "getLeadsData->responseData:" + responseObject);
                        if(responseObject.optString("status").equalsIgnoreCase("success")){

                            if(responseObject.has("leads")) {
                                JSONArray jsonArray = responseObject.getJSONArray("leads");
                                for(int i=0; i < jsonArray.length(); i++){
                                    HashMap<String, String> task = new HashMap<>();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    task.put("id",jsonObject.getString("lead_id"));
                                    task.put("name",jsonObject.getString("name"));
                                    task.put("address",jsonObject.getString("address1"));
                                    task.put("area",jsonObject.getString("area"));
                                    task.put("city",jsonObject.getString("city"));
                                    task.put("mobile",jsonObject.getString("mobile"));
                                    task.put("date",jsonObject.getString("fdate") + " " + jsonObject.getString("ftime"));
                                    task.put("descr",jsonObject.getString("descr"));
                                    task.put("product",jsonObject.getString("product_name"));
                                    task.put("status",jsonObject.getString("status"));
                                    Leads.add(task);
                                }
                                ListAdapter adapter = new SimpleAdapter(
                                        getContext(),Leads, R.layout.leads_list_layout,
                                        new String[]{"name","date","id","address","area","city","mobile","descr","product","status"},
                                        new int[]{R.id.meeting_name,R.id.meeting_date});
                                allLeads.setAdapter(adapter);
                                allLeads.setTextFilterEnabled(true);
                                allLeads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        DetailPopup(allLeads.getItemAtPosition(position).toString());//Loading Popup
                                    }
                                });
                                LeadsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        if(TextUtils.isEmpty(query)){
                                            allLeads.clearTextFilter();
                                        }else{
                                            allLeads.setFilterText(query);
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if(TextUtils.isEmpty(newText)){
                                            allLeads.clearTextFilter();
                                        }else{
                                            allLeads.setFilterText(newText);
                                        }

                                        return false;
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            public void DetailPopup(String position) {
                String result[] = position.split("\\s*,\\s*");

                String area[] = result[0].split("\\s*=\\s*");//Extracting Customer id
                String date[] = result[1].split("\\s*=\\s*");//Extracting Customer id
                String description[] = result[2].split("\\s*=\\s*");//Extracting Customer id
                String product[] = result[3].split("\\s*=\\s*");//Extracting Customer id
                String address[] = result[4].split("\\s*=\\s*");//Extracting Customer id
                String city[] = result[5].split("\\s*=\\s*");//Extracting Customer id
                String name[] = result[6].split("\\s*=\\s*");//Extracting Customer id
                String mobile[] = result[7].split("\\s*=\\s*");//Extracting Customer id
                String id[] = result[8].split("\\s*=\\s*");//Extracting Customer id
                String status[] = result[9].split("\\s*=\\s*");//Extracting Customer id
                Log.d("Leads", "jsonArray->Position:" + position);

                String addresss = "";
                if(!address[1].equalsIgnoreCase("")){
                    addresss += address[1]+", ";
                }
                if(!area[1].equalsIgnoreCase("")){
                    addresss += area[1]+", ";
                }
                if(!city[1].equalsIgnoreCase("")){
                    addresss += city[1];
                }

                Dialog dialog = new Dialog(getContext(),R.style.DialogStyle);
                dialog.setContentView(R.layout.leads_detail_popup);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounden_shape_2);

                TextView meeting_name = dialog.findViewById(R.id.lead_name);
                TextView meeting_date = dialog.findViewById(R.id.lead_date);
                TextView meeting_status = dialog.findViewById(R.id.lead_status);
                TextView meeting_product = dialog.findViewById(R.id.lead_product);
                TextView meeting_desc = dialog.findViewById(R.id.lead_desc);
                TextView meeting_mobile = dialog.findViewById(R.id.lead_mobile);


                meeting_name.setText(name[1]);
                meeting_date.setText(date[1]);
                meeting_status.setText(status[1].replace("}",""));
                meeting_product.setText(product[1]);
                meeting_mobile.setText(mobile[1]);
                meeting_desc.setText(description[1]);

                dialog.show();
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
