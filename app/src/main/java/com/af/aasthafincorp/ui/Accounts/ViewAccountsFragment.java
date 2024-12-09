package com.af.aasthafincorp.ui.Accounts;

import static com.af.aasthafincorp.Utility.ConstantClass.Token;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.af.aasthafincorp.databinding.FragmentAccountsViewBinding;
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

public class ViewAccountsFragment extends Fragment {
    private FragmentAccountsViewBinding binding;
    UtilityClass utilityClass;
    ListView allAccounts;
    ArrayList<HashMap<String,String>> Accounts = new ArrayList<>();
    SearchView AccountsSearch;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountsViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        utilityClass = new UtilityClass(getContext());
        utilityClass.processDialogStart();

        allAccounts = binding.allAccounts;
        AccountsSearch = binding.AccountsSearch;

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SaveSharedPreferences.getUser_id(getContext()));

        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getAllAccounts(Token,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                JSONObject Data = new JSONObject();
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("Accounts", "getAccountsData->responseData:" + responseObject);
                        if(responseObject.optString("status").equalsIgnoreCase("success")){

                            if(responseObject.has("accounts")) {
                                JSONArray jsonArray = responseObject.getJSONArray("accounts");
                                for(int i=0; i < jsonArray.length(); i++){
                                    HashMap<String, String> task = new HashMap<>();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    task.put("id",jsonObject.getString("id"));
                                    task.put("name",jsonObject.getString("account_name"));
                                    task.put("address",jsonObject.getString("address1"));
                                    task.put("area",jsonObject.getString("area"));
                                    task.put("city",jsonObject.getString("city"));
                                    task.put("mobile",jsonObject.getString("mobile"));
                                    task.put("date",jsonObject.getString("fdate") + " " + jsonObject.getString("ftime"));
                                    task.put("descr",jsonObject.getString("descr"));
                                    task.put("product",jsonObject.getString("product_name"));
                                    task.put("reference_name",jsonObject.getString("reference_name"));
                                    task.put("status",jsonObject.getString("status"));
                                    Accounts.add(task);
                                }
                                ListAdapter adapter = new SimpleAdapter(
                                        getContext(),Accounts, R.layout.accounts_list_layout,
                                        new String[]{"name","date","id","address","area","city","mobile","descr","product","reference_name","status"},
                                        new int[]{R.id.meeting_name,R.id.meeting_date});
                                allAccounts.setAdapter(adapter);
                                allAccounts.setTextFilterEnabled(true);
                                allAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        DetailPopup(allAccounts.getItemAtPosition(position).toString());//Loading Popup
                                    }
                                });
                                AccountsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        if(TextUtils.isEmpty(query)){
                                            allAccounts.clearTextFilter();
                                        }else{
                                            allAccounts.setFilterText(query);
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if(TextUtils.isEmpty(newText)){
                                            allAccounts.clearTextFilter();
                                        }else{
                                            allAccounts.setFilterText(newText);
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
                String reference[] = result[4].split("\\s*=\\s*");//Extracting Customer id
                String address[] = result[5].split("\\s*=\\s*");//Extracting Customer id
                String city[] = result[6].split("\\s*=\\s*");//Extracting Customer id
                String name[] = result[7].split("\\s*=\\s*");//Extracting Customer id
                String mobile[] = result[8].split("\\s*=\\s*");//Extracting Customer id
                String id[] = result[9].split("\\s*=\\s*");//Extracting Customer id
                String status[] = result[10].split("\\s*=\\s*");//Extracting Customer id
                Log.d("Accounts", "jsonArray->Position:" + position);

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
                dialog.setContentView(R.layout.accounts_detail_popup);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounden_shape_2);

                TextView meeting_name = dialog.findViewById(R.id.lead_name);
                TextView meeting_date = dialog.findViewById(R.id.lead_date);
                TextView meeting_status = dialog.findViewById(R.id.lead_status);
                TextView meeting_product = dialog.findViewById(R.id.lead_product);
                TextView meeting_desc = dialog.findViewById(R.id.lead_desc);
                TextView meeting_mobile = dialog.findViewById(R.id.lead_mobile);
                TextView reference_name = dialog.findViewById(R.id.lead_reference);


                meeting_name.setText("Name: "+name[1]);
                meeting_date.setText("Date & Time: "+date[1]);
                meeting_status.setText("Status: "+status[1].replace("}",""));
                meeting_product.setText("Product: "+product[1]);
                meeting_mobile.setText("Mobile: "+mobile[1]);
                meeting_desc.setText("Description: "+description[1]);
                reference_name.setText("Reference: "+reference[1]);

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
