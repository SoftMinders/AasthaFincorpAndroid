package com.af.aasthafincorp.ui.Todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.af.aasthafincorp.R;
import com.af.aasthafincorp.databinding.BottomDialogBinding;
import com.af.aasthafincorp.databinding.FragmentTodoBinding;
import com.ak.ColoredDate;
import com.ak.EventObjects;
import com.ak.KalendarView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoFragment extends Fragment {
    private FragmentTodoBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        KalendarView mKalendarView = binding.kalendar;
        List<ColoredDate> datesColors = new ArrayList<>();
        datesColors.add(new ColoredDate(new Date(), getResources().getColor(R.color.main_prim)));
        mKalendarView.setColoredDates(datesColors);

        List<EventObjects> events = new ArrayList<>();
        events.add(new EventObjects("meeting",new Date()));
        mKalendarView.setEvents(events);

        mKalendarView.setDateSelector(new KalendarView.DateSelector() {
            @Override
            public void onDateClicked(Date selectedDate) {
                Log.d("DateSel",selectedDate.toString());
            }
        });
        mKalendarView.setMonthChanger(changedMonth -> Log.d("Changed","month changed "+changedMonth));
        Calendar tempCal = Calendar.getInstance();
        tempCal.set(Calendar.DATE,11);
        List<EventObjects> events2 = new ArrayList<>();
        events2.add(new EventObjects("meeting",tempCal.getTime()));

        tempCal.set(Calendar.DATE,15);
        List<ColoredDate> datesColors2 = new ArrayList<>();
        datesColors2.add(new ColoredDate(tempCal.getTime(), getResources().getColor(R.color.main_prim)));
        mKalendarView.addColoredDates(datesColors2);

        mKalendarView.addEvents(events2);

        Button btBottomDialog = binding.btBottomDialog;
        btBottomDialog.setOnClickListener(view -> showDialog());

        return root;
    }
    void showDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_dialog, null);

        Button demo = bottomSheet.findViewById(R.id.bt_bottom_dialog);
        KalendarView kalendarView = bottomSheet.findViewById(R.id.kalendar);
        demo.setOnClickListener(
                view -> {
                    kalendarView.setInitialSelectedDate(new Date());
                }
        );

        dialog.setContentView(bottomSheet);
        dialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
