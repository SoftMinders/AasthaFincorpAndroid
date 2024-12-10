package com.af.aasthafincorp.ui.Reference;

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
import com.af.aasthafincorp.databinding.FragmentReferencesViewBinding;
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

public class ViewReferencesFragment extends Fragment {
    private FragmentReferencesViewBinding binding;
    UtilityClass utilityClass;
    ListView allReference;
    ArrayList<HashMap<String,String>> Reference = new ArrayList<>();
    SearchView ReferenceSearch;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReferencesViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        utilityClass = new UtilityClass(getContext());
        utilityClass.processDialogStart();

        allReference = binding.allReference;
        ReferenceSearch = binding.ReferenceSearch;

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SaveSharedPreferences.getUser_id(getContext()));

        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getAllReference(Token,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                JSONObject Data = new JSONObject();
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("Reference", "getReferenceData->responseData:" + responseObject);
                        if(responseObject.optString("status").equalsIgnoreCase("success")){

                            if(responseObject.has("reference")) {
                                JSONArray jsonArray = responseObject.getJSONArray("reference");
                                for(int i=0; i < jsonArray.length(); i++){
                                    HashMap<String, String> task = new HashMap<>();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    task.put("id",jsonObject.getString("id"));
                                    task.put("name",jsonObject.getString("account_name"));
                                    task.put("code",jsonObject.getString("code"));
                                    task.put("reference_name",jsonObject.getString("reference_name"));
                                    task.put("reference_number",jsonObject.getString("reference_number"));
                                    Reference.add(task);
                                }
                                ListAdapter adapter = new SimpleAdapter(
                                        getContext(),Reference, R.layout.reference_list_layout,
                                        new String[]{"reference_name","reference_number","id","name","code"},
                                        new int[]{R.id.ref_name,R.id.ref_number});
                                allReference.setAdapter(adapter);
                                allReference.setTextFilterEnabled(true);
                                allReference.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        DetailPopup(allReference.getItemAtPosition(position).toString());//Loading Popup
                                    }
                                });
                                ReferenceSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        if(TextUtils.isEmpty(query)){
                                            allReference.clearTextFilter();
                                        }else{
                                            allReference.setFilterText(query);
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if(TextUtils.isEmpty(newText)){
                                            allReference.clearTextFilter();
                                        }else{
                                            allReference.setFilterText(newText);
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
                Log.d("Reference", "jsonArray->Position:" + position);

                String referencename[] = result[0].split("\\s*=\\s*");//Extracting Customer id
                String code[] = result[1].split("\\s*=\\s*");//Extracting Customer id
                String name[] = result[2].split("\\s*=\\s*");//Extracting Customer id
                String id[] = result[3].split("\\s*=\\s*");//Extracting Customer id
                String referencenumber[] = result[4].split("\\s*=\\s*");//Extracting Customer id


                Dialog dialog = new Dialog(getContext(),R.style.DialogStyle);
                dialog.setContentView(R.layout.reference_detail_popup);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounden_shape_2);

                TextView account_name = dialog.findViewById(R.id.account_name);
                TextView reference_name = dialog.findViewById(R.id.reference_name);
                TextView reference_number = dialog.findViewById(R.id.reference_number);
                TextView ref_code = dialog.findViewById(R.id.ref_code);


                account_name.setText(name[1]);
                reference_name.setText(referencename[1]);
                reference_number.setText(referencenumber[1].replace("}",""));
                ref_code.setText(code[1]);

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
