package com.af.aasthafincorp.ui.Task;

import static com.af.aasthafincorp.Utility.ConstantClass.Token;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.af.aasthafincorp.Interface.RestServices;
import com.af.aasthafincorp.R;
import com.af.aasthafincorp.Utility.ApiClient;
import com.af.aasthafincorp.Utility.SaveSharedPreferences;
import com.af.aasthafincorp.Utility.UtilityClass;
import com.af.aasthafincorp.databinding.FragmentTaskViewBinding;
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

public class TasksFragment extends Fragment {
    private FragmentTaskViewBinding binding;
    UtilityClass utilityClass;
    ListView allTasks;
    ArrayAdapter<String> arr;
    ArrayList<HashMap<String,String>> tasks = new ArrayList<>();
    SearchView TasksSearch;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTaskViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        utilityClass = new UtilityClass(getContext());
        utilityClass.processDialogStart();

        allTasks = binding.allTasks;
        TasksSearch = binding.TasksSearch;

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SaveSharedPreferences.getUser_id(getContext()));

        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getAllTasks(Token,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                JSONObject Data = new JSONObject();
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("Tasks", "getTasksData->responseData:" + responseObject);
                        if(responseObject.optString("status").equalsIgnoreCase("success")){

                            if(responseObject.has("tasks")) {
                                JSONArray jsonArray = responseObject.getJSONArray("tasks");
                                for(int i=0; i < jsonArray.length(); i++){
                                    HashMap<String, String> task = new HashMap<>();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    task.put("id",jsonObject.getString("id"));
                                    task.put("name",jsonObject.getString("task"));
                                    task.put("date",jsonObject.getString("date") + " " + jsonObject.getString("time"));
                                    task.put("status",jsonObject.getString("status"));
                                    task.put("remarks",jsonObject.getString("remarks"));
                                    task.put("type",jsonObject.getString("type"));
                                    tasks.add(task);
                                }
                                ListAdapter adapter = new SimpleAdapter(
                                        getContext(),tasks, R.layout.task_list_layout,
                                        new String[]{"name","date","id","status","remarks","type"},
                                        new int[]{R.id.task_name,R.id.task_date});
                                allTasks.setAdapter(adapter);
                                allTasks.setTextFilterEnabled(true);
                                allTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        DetailPopup(allTasks.getItemAtPosition(position).toString());//Loading Popup
                                    }
                                });
                                TasksSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        if(TextUtils.isEmpty(query)){
                                            allTasks.clearTextFilter();
                                        }else{
                                            allTasks.setFilterText(query);
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if(TextUtils.isEmpty(newText)){
                                            allTasks.clearTextFilter();
                                        }else{
                                            allTasks.setFilterText(newText);
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

                String taskdate[] = result[0].split("\\s*=\\s*");//Extracting Customer id
                String taskname[] = result[1].split("\\s*=\\s*");//Extracting Customer id
                String taskid[] = result[2].split("\\s*=\\s*");//Extracting Customer id
                String taskremark[] = result[4].split("\\s*=\\s*");//Extracting Customer id
                String taskstatus[] = result[5].split("\\s*=\\s*");//Extracting Customer id
                String tasktype[] = result[3].split("\\s*=\\s*");//Extracting Customer id
                Log.d("Tasks", "jsonArray->Position:" + position);

                Dialog dialog = new Dialog(getContext(),R.style.DialogStyle);
                dialog.setContentView(R.layout.task_detail_popup);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounden_shape_2);

                TextView task_name = dialog.findViewById(R.id.task_name);
                TextView task_date = dialog.findViewById(R.id.task_date);
                TextView task_status = dialog.findViewById(R.id.task_status);
                TextView task_remarks = dialog.findViewById(R.id.task_remarks);

                if(tasktype[1].equalsIgnoreCase("Call")){
                    Drawable drawableLeft = ContextCompat.getDrawable(getContext(), R.drawable.phone);
                    task_name.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null,null,null);
                }

                task_date.setText(taskdate[1]);
                task_name.setText(taskname[1]);
                task_remarks.setText(taskremark[1]);
                task_status.setText(taskstatus[1].replace("}",""));

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
