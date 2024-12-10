package com.af.aasthafincorp.ui.sales;

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
import android.widget.Button;
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
import com.af.aasthafincorp.databinding.FragmentSalesViewBinding;
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

public class ViewSalesFragment extends Fragment {
    private FragmentSalesViewBinding binding;
    UtilityClass utilityClass;
    ListView allSales;
    ArrayList<HashMap<String,String>> Sales = new ArrayList<>();
    SearchView SalesSearch;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSalesViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        utilityClass = new UtilityClass(getContext());
        utilityClass.processDialogStart();

        allSales = binding.allSales;
        SalesSearch = binding.SalesSearch;

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SaveSharedPreferences.getUser_id(getContext()));

        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getAllSales(Token,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                JSONObject Data = new JSONObject();
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("Sales", "getSalesData->responseData:" + responseObject);
                        if(responseObject.optString("status").equalsIgnoreCase("success")){

                            if(responseObject.has("sales")) {
                                JSONArray jsonArray = responseObject.getJSONArray("sales");
                                for(int i=0; i < jsonArray.length(); i++){
                                    HashMap<String, String> task = new HashMap<>();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    task.put("id",jsonObject.getString("id"));
                                    task.put("name",jsonObject.getString("name"));
                                    task.put("code",jsonObject.getString("code"));
                                    task.put("reference_name",jsonObject.getString("reference_name"));
                                    task.put("amount",jsonObject.getString("amount"));
                                    task.put("product",jsonObject.getString("product"));
                                    task.put("created_at",jsonObject.getString("created_at"));
                                    Sales.add(task);
                                }
                                ListAdapter adapter = new SimpleAdapter(
                                        getContext(),Sales, R.layout.sales_list_layout,
                                        new String[]{"name","amount","code","reference_name","product","created_at"},
                                        new int[]{R.id.meeting_name,R.id.meeting_date});
                                allSales.setAdapter(adapter);
                                allSales.setTextFilterEnabled(true);
                                allSales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        DetailPopup(allSales.getItemAtPosition(position).toString());//Loading Popup
                                    }
                                });
                                SalesSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        if(TextUtils.isEmpty(query)){
                                            allSales.clearTextFilter();
                                        }else{
                                            allSales.setFilterText(query);
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if(TextUtils.isEmpty(newText)){
                                            allSales.clearTextFilter();
                                        }else{
                                            allSales.setFilterText(newText);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
