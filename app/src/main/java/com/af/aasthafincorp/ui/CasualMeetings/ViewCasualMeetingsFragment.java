package com.af.aasthafincorp.ui.CasualMeetings;

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

public class ViewCasualMeetingsFragment extends Fragment {
    private FragmentCasualMeetingsViewBinding binding;
    UtilityClass utilityClass;
    ListView allCasualMeetins;
    ArrayList<HashMap<String,String>> CasualMeetins = new ArrayList<>();
    SearchView CasualMeetinsSearch;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView startTime, endTime;
    private ImageView playPauseButton;
    private Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCasualMeetingsViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        utilityClass = new UtilityClass(getContext());
        utilityClass.processDialogStart();

        allCasualMeetins = binding.allCasualMeetins;
        CasualMeetinsSearch = binding.CasualMeetingsSearch;

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SaveSharedPreferences.getUser_id(getContext()));

        RestServices restServices = ApiClient.getApiClient().create(RestServices.class);
        Call<JsonObject> call = restServices.getAllCasualMeetings(Token,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                utilityClass.processDialogStop();
                JSONObject responseObject = new JSONObject();
                JSONObject Data = new JSONObject();
                if(response.isSuccessful()){
                    try {
                        responseObject = new JSONObject(response.body().toString());
                        Log.d("Tasks", "getCasualMeetingsData->responseData:" + responseObject);
                        if(responseObject.optString("status").equalsIgnoreCase("success")){

                            if(responseObject.has("meetings")) {
                                JSONArray jsonArray = responseObject.getJSONArray("meetings");
                                for(int i=0; i < jsonArray.length(); i++){
                                    HashMap<String, String> task = new HashMap<>();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    task.put("id",jsonObject.getString("meeting_id"));
                                    task.put("name",jsonObject.getString("meeting_name"));
                                    task.put("address",jsonObject.getString("address1"));
                                    task.put("area",jsonObject.getString("area"));
                                    task.put("city",jsonObject.getString("city"));
                                    task.put("mobile",jsonObject.getString("mobile"));
                                    task.put("date",jsonObject.getString("fdate") + " " + jsonObject.getString("ftime"));
                                    task.put("descr",jsonObject.getString("descr"));
                                    task.put("product",jsonObject.getString("product_name"));
                                    task.put("status",jsonObject.getString("status"));
                                    task.put("audio",jsonObject.getString("audio"));
                                    CasualMeetins.add(task);
                                }
                                ListAdapter adapter = new SimpleAdapter(
                                        getContext(),CasualMeetins, R.layout.casual_meeting_list_layout,
                                        new String[]{"name","date","id","address","area","city","mobile","descr","product","status","audio"},
                                        new int[]{R.id.meeting_name,R.id.meeting_date});
                                allCasualMeetins.setAdapter(adapter);
                                allCasualMeetins.setTextFilterEnabled(true);
                                allCasualMeetins.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        DetailPopup(allCasualMeetins.getItemAtPosition(position).toString());//Loading Popup
                                    }
                                });
                                CasualMeetinsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        if(TextUtils.isEmpty(query)){
                                            allCasualMeetins.clearTextFilter();
                                        }else{
                                            allCasualMeetins.setFilterText(query);
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if(TextUtils.isEmpty(newText)){
                                            allCasualMeetins.clearTextFilter();
                                        }else{
                                            allCasualMeetins.setFilterText(newText);
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
                String audio[] = result[9].split("\\s*=\\s*");//Extracting Customer id
                String status[] = result[10].split("\\s*=\\s*");//Extracting Customer id
                Log.d("Casual Meetings", "jsonArray->Position:" + position);

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
                dialog.setContentView(R.layout.casual_meeting_detail_popup);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounden_shape_2);

                TextView meeting_name = dialog.findViewById(R.id.meeting_name);
                TextView meeting_date = dialog.findViewById(R.id.meeting_date);
                TextView meeting_status = dialog.findViewById(R.id.meeting_status);
                TextView meeting_product = dialog.findViewById(R.id.meeting_product);
                TextView meeting_desc = dialog.findViewById(R.id.meeting_desc);
                TextView meeting_mobile = dialog.findViewById(R.id.meeting_mobile);


                meeting_name.setText(name[1].length() > 1 ? name[1] : "");
                meeting_date.setText(date[1].length() > 1 ? date[1] : "");
                meeting_status.setText(status[1].replace("}",""));
                meeting_product.setText(product[1].length() > 1 ? product[1] : "");
                meeting_mobile.setText(mobile[1].length() > 1 ? mobile[1] : "");
                meeting_desc.setText(description[1].length() > 1 ? description[1] : "");


                startTime = dialog.findViewById(R.id.start_time);
                endTime = dialog.findViewById(R.id.end_time);
                seekBar = dialog.findViewById(R.id.progress_bar);
                playPauseButton = dialog.findViewById(R.id.play_pause_button);

                if(audio[1].length() > 1) {
                    // Initialize MediaPlayer with an external URL
                    mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(audio[1]));
                    mediaPlayer.setOnPreparedListener(mp -> {
                        // Set End Time
                        endTime.setText(formatTime(mp.getDuration()));
                        seekBar.setMax(mp.getDuration());
                    });

                    // Play/Pause Button
                    playPauseButton.setOnClickListener(v -> {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            playPauseButton.setImageResource(R.drawable.play);
                        } else {
                            mediaPlayer.start();
                            playPauseButton.setImageResource(R.drawable.pause);
                            updateSeekBar();
                        }
                    });
                    // SeekBar Listener
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) mediaPlayer.seekTo(progress);
                            startTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                }else{
                    //No Audio Found
                }

                dialog.show();
            }
            private void updateSeekBar() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                startTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                if (mediaPlayer.isPlaying()) {
                    handler.postDelayed(this::updateSeekBar, 1000);
                }
            }

            private String formatTime(int milliseconds) {
                int minutes = (milliseconds / 1000) / 60;
                int seconds = (milliseconds / 1000) % 60;
                return String.format("%02d:%02d", minutes, seconds);
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
