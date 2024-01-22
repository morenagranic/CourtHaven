package courthaven.pack;

import static java.lang.Integer.valueOf;
import static java.lang.Math.round;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class bookCourtFragment extends Fragment {

    GridView gridView;
    private Context mContext;


    int userId = -1;
    Court court = new Court();
    String selectedDate = new String();
    private int selectedPosition = -1;
    private int selectedRadioButtonId = -1;


    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public int getUserId() {return userId;}

    public void setUserId(int userId) {this.userId = userId;}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    DataBaseHelper db = new DataBaseHelper(mContext);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_court, container, false);

        date_timeAdapter adapter = new date_timeAdapter(mContext, new ArrayList<>());
        adapter.setCourt(court);
        adapter.setSelectedDate(selectedDate);

        // Get courts data from the database
        DataBaseHelper db = new DataBaseHelper(mContext);
        ArrayList<String> times = db.getTimes(selectedDate);
        adapter.addAll(times);

        // Attach the adapter to the GridView
        gridView = view.findViewById(R.id.times_grid);
        gridView.setAdapter(adapter);

        ImageView image = view.findViewById(R.id.imageView_court);
        image.setBackgroundResource(court.getImageDrawable());

        ImageView cancelX = view.findViewById(R.id.cancel_icon1);
        cancelX.setOnClickListener(v -> ((MainActivity) requireActivity()).replaceFragment(new homeFragment()));

        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(v -> ((MainActivity) requireActivity()).replaceFragment(new homeFragment()));


        TextView date1 = view.findViewById((R.id.date_time_text1));
        date1.setText(formatDate(selectedDate));


        TextView courtName = view.findViewById((R.id.court1));
        courtName.setText(court.getName());

        TextView courtAddress = view.findViewById((R.id.address1));
        courtAddress.setText(court.getAddress());


        Button totalButton = view.findViewById((R.id.total_btn));
        totalButton.setText("Total = 0.00 EUR");



        final double[] cost = {0};
        final int[] duration = {0};
        final boolean[] available = {true};
        final String[] selectedTime = {"","","","",""};
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTime[0] = selectTime(position);
            }
        });

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        TextView date_time2 = view.findViewById((R.id.date_time_text2));
        date_time2.setText(""); //triba dodat dan i vrijeme

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle radio button selection here
                available[0] = true;
                duration[0] = handleRadioButtonSelection(checkedId, view);
                cost[0] = duration[0] * 10.5;

                for (int i = 1; i <= duration[0]; i++){
                    selectedTime[i] = incTime(selectedTime[0], i);
                    if (selectedTime[i].equals("") || db.isBooked(court.getId(), db.getDateTimeId(selectedDate, selectedTime[i-1]))){
                        available[0] = false;
                    }
                }

                if (available[0]) {
                    totalButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.purple));

                } else {
                    totalButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
                    Toast.makeText(mContext, "Select valid time and duration", Toast.LENGTH_SHORT).show();
                }


                totalButton.setText("Total = " + String.format("%.2f", cost[0]) + "EUR");
                date_time2.setText(formatDate(selectedDate) + " " + selectedTime[0] + " - " + selectedTime[duration[0]]); //triba dodat dan i vrijeme
            }
        });

        totalButton.setOnClickListener(v -> {
            if (available[0]) {
                int code = generateCode();
                int bookingId = db.addBooking(code, cost[0], duration[0], userId);
                Toast.makeText(mContext, "Court booked successfully", Toast.LENGTH_LONG).show();
                for (int i = 0; i < duration[0]; i++) {
                    int dateTimeId = db.getDateTimeId(selectedDate, selectedTime[i]);
                    db.addBookedDateTimeCourt(dateTimeId, court.getId(), bookingId);
                }
                ((MainActivity) requireActivity()).replaceFragment(new homeFragment());

            } else {
                Toast.makeText(mContext, "Select valid time and duration", Toast.LENGTH_SHORT).show();
            }


        });


        return view;
    }

    private String incTime(String time, int i) {
        String[] s = time.split(":");
        int hour = Integer.parseInt(s[0]);
        int minutes = Integer.parseInt(s[1]);

        if (minutes > 0){
            minutes = (i % 2 != 0) ? 0 : 30;
            hour = hour + (1 + i)/2;
        }else{
            minutes = (i % 2 != 0) ? 30 : 0;
            hour = hour + i/2;
        }
        s[0] = (hour < 10) ? "0" + hour : "" + hour;
        s[1] = (minutes > 0) ? "30" : "00";

        time = (hour < 24) ? s[0] + ":" + s[1] : "";
        return time;
    }


    private int handleRadioButtonSelection(int checkedId, View view) {
        // Unselect the previous item
        if (selectedRadioButtonId != -1) {
            RadioButton previousRadioButton = view.findViewById(selectedRadioButtonId);
            if (previousRadioButton != null) {
                previousRadioButton.setChecked(false);
            }
        }

        // Update the selected radio button ID
        selectedRadioButtonId = checkedId;

        if (checkedId == R.id.btn_30min) return 1;
        else if (checkedId == R.id.btn_1hr) return 2;
        else if (checkedId == R.id.btn_1hr30min) return 3;
        else if (checkedId == R.id.btn_2hr) return 4;

        return 0;
    }

    private String selectTime(int position) {
        // Unselect the previous item
        if (selectedPosition != -1) {
            View previousItem = gridView.getChildAt(selectedPosition);
            if (previousItem != null) {
                TextView previousTextView = previousItem.findViewById(R.id.time_text);
                if (previousTextView != null) {
                    previousTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    previousTextView.setTextColor(getResources().getColor(R.color.purple));
                }
            }
        }

        String selectedTime = "";

        // Select the new item
        View selectedItem = gridView.getChildAt(position);
        if (selectedItem != null) {
            TextView selectedTimeTextView = selectedItem.findViewById(R.id.time_text);
            if (selectedTimeTextView != null) {
                selectedTime = selectedTimeTextView.getText().toString();
                selectedTimeTextView.setBackgroundColor(getResources().getColor(R.color.purple));
                selectedTimeTextView.setTextColor(getResources().getColor(R.color.lighter_gray));
            }
        }

        // Update the selected position
        selectedPosition = position;
        return selectedTime;
    }

    private String formatDate(String date){
        DataBaseHelper db = new DataBaseHelper(mContext);
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        int numDay = Integer.parseInt(date.split("-")[0]);
        int index = Integer.parseInt(date.split("-")[1]) - 1;
        String day = db.getDateTimeDay(date);

        return day + ", " + numDay + " " + months[index];
    }

    public static int generateCode() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }
}