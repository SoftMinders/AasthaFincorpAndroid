package com.af.aasthafincorp.ui.Contacts;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.af.aasthafincorp.Interface.RestServices;
import com.af.aasthafincorp.R;
import com.af.aasthafincorp.Utility.ApiClient;
import com.af.aasthafincorp.Utility.SaveSharedPreferences;
import com.af.aasthafincorp.Utility.UtilityClass;
import com.af.aasthafincorp.databinding.FragmentContactNotreceivingBinding;
import com.af.aasthafincorp.databinding.FragmentContactSwitchoffBinding;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwitchOffContactFragment extends Fragment{
    private FragmentContactSwitchoffBinding binding;
    UtilityClass utilityClass;
    ListView allpendingContact;
    ArrayList<HashMap<String,String>> Sheets = new ArrayList<>();
    SearchView pendingContactSearch;
    Spinner pendingContactSheets;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentContactSwitchoffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        utilityClass = new UtilityClass(getContext());
        utilityClass.processDialogStart();

        allpendingContact = binding.allpendingContact;
        pendingContactSearch = binding.pendingContactSearch;
        pendingContactSheets = binding.pendingContactSheets;

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SaveSharedPreferences.getUser_id(getContext()));

        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getAllSheets(Token,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                JSONObject Data = new JSONObject();
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("sheets", "getSheetsData->responseData:" + responseObject);
                        if(responseObject.optString("status").equalsIgnoreCase("success")){

                            if(responseObject.has("sheets")) {
                                JSONArray jsonArray = responseObject.getJSONArray("sheets");
                                List<SpinnerItem> sheet = new ArrayList<>();
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    sheet.add(new SpinnerItem(jsonObject.getString("sheet_id"), jsonObject.getString("name")));//Setting custom adapter for spinner to insert ID
                                }
                                SpinnerAdapter adapter = new SpinnerAdapter(getContext(), sheet);
                                pendingContactSheets.setAdapter(adapter);
                                pendingContactSheets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                                        loadContacts(selectedItem.getId(),"switch_off",SaveSharedPreferences.getUser_id(getContext()));
                                        //Toast.makeText(getContext(), "ID: " + selectedItem.getId(), Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
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

                String amount[] = result[1].split("\\s*=\\s*");//Extracting Customer id
                String code[] = result[3].split("\\s*=\\s*");//Extracting Customer id
                String created_at[] = result[5].split("\\s*=\\s*");//Extracting Customer id
                String name[] = result[4].split("\\s*=\\s*");//Extracting Customer id
                String product[] = result[2].split("\\s*=\\s*");//Extracting Customer id
                String reference[] = result[0].split("\\s*=\\s*");//Extracting Customer id
                Log.d("Sales", "jsonArray->Position:" + position);

                Dialog dialog = new Dialog(getContext(),R.style.DialogStyle);
                dialog.setContentView(R.layout.sales_detail_popup);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounden_shape_2);

                TextView investor_name = dialog.findViewById(R.id.investor_name);
                TextView sale_reference = dialog.findViewById(R.id.sale_reference);
                TextView sale_code = dialog.findViewById(R.id.sale_code);
                TextView sale_amount = dialog.findViewById(R.id.sale_amount);
                TextView sale_product = dialog.findViewById(R.id.sale_product);
                TextView sale_date = dialog.findViewById(R.id.sale_date);


                investor_name.setText(name[1].length() > 1 ? name[1] : "");
                sale_date.setText(created_at[1].length() > 1 ? created_at[1] : "");
                sale_reference.setText(reference[1].length() > 1 ? reference[1] : "");
                sale_code.setText(code[1].length() > 1 ? code[1] : "");
                sale_amount.setText(amount[1].length() > 1 ? amount[1] : "");
                sale_product.setText(product[1].length() > 1 ? product[1] : "");


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
    public void loadContacts(String sheetid, String stype, String userid){
        utilityClass.processDialogStart();
        //Clearing Data
        Sheets.clear();

        RequestBody sheet_id = RequestBody.create(MediaType.parse("text/plain"), sheetid);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), stype);
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), userid);

        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getContactByType(Token,sheet_id,type,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                Log.d("Contacts", "getContactsData->responseData:" + response);
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("Contacts", "getContactsData->responseData:" + responseObject);
                        if(responseObject.optString("status").equalsIgnoreCase("success")){
                            //Getting All Products Based on Class
                            JSONArray contacts = responseObject.getJSONArray("contacts");
                            for(int i=0; i < contacts.length(); i++){
                                HashMap<String, String> task = new HashMap<>();
                                JSONObject jsonObject = contacts.getJSONObject(i);
                                task.put("id",jsonObject.getString("id"));
                                task.put("name",jsonObject.getString("name"));
                                task.put("number",jsonObject.getString("number"));
                                task.put("address",jsonObject.getString("address"));
                                task.put("sheet_id",jsonObject.getString("sheet_id"));
                                Sheets.add(task);
                            }
                            ListAdapter adapter = new SimpleAdapter(
                                        getContext(),Sheets, R.layout.contacts_list_layout,
                                        new String[]{"name","number","address","sheet_id","id"},
                                        new int[]{R.id.contact_name,R.id.contact_number});
                            allpendingContact.setAdapter(adapter);
                            allpendingContact.setTextFilterEnabled(true);
                            allpendingContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        DetailPopup(allpendingContact.getItemAtPosition(position).toString());//Loading Popup
                                    }
                                });
                            pendingContactSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        if(TextUtils.isEmpty(query)){
                                            allpendingContact.clearTextFilter();
                                        }else{
                                            allpendingContact.setFilterText(query);
                                        }
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if(TextUtils.isEmpty(newText)){
                                            allpendingContact.clearTextFilter();
                                        }else{
                                            allpendingContact.setFilterText(newText);
                                        }
                                        return false;
                                    }
                                });

                        } else {
                            Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            public void DetailPopup(String position) {
                String jsonString = position.replace("=", "\":\"").replace(", ", "\", \"");
                jsonString = "{\"" + jsonString.substring(1, jsonString.length() - 1) + "\"}";
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Log.d("Contacts", "jsonArray->Position:" + position);
                    Log.d("Contacts", "jsonArray->Position2:" + jsonString);

                    Dialog dialog = new Dialog(getContext(),R.style.DialogStyle);
                    dialog.setContentView(R.layout.contact_detail_popup);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounden_shape_2);

                    TextView contact_name = dialog.findViewById(R.id.contact_name);
                    TextView contact_number = dialog.findViewById(R.id.contact_number);
                    TextView contact_address = dialog.findViewById(R.id.contact_address);


                    contact_name.setText(jsonObject.getString("name"));
                    contact_number.setText(jsonObject.getString("number"));
                    contact_address.setText(jsonObject.getString("address"));


                    dialog.show();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                utilityClass.processDialogStart();
                Log.d("Test", "getLogin->error->" + t.toString());
                utilityClass.processDialogStop();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
