package courthaven.pack;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;



import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class favoritesFragment extends Fragment {

    static String selectedDate = homeFragment.getSelectedDate();
    private Context mContext;
    private int maxDistance = 1000; // Default max distance
    public static String getSelectedDate() {
        return selectedDate;
    }

    public static void setSelectedDate(String selectedDate) {
        FilteredCourtsFragment.selectedDate = selectedDate;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        //Log.d("FilteredCourtsFragment", "onAttach: mContext = " + mContext);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);


        int userId = SignInFragment.UserId;
        courtAdapter adapter = new courtAdapter(mContext, new ArrayList<>());
        adapter.initializeFavoritedCourtIds(userId);

        DataBaseHelper db = new DataBaseHelper(mContext);
        ArrayList<Court> courts1 = db.getCourtsFiltered(db.getUserFavorites(userId));

        adapter.addAll(courts1);

        ListView listView = rootView.findViewById(R.id.courts_list);
        listView.setAdapter(adapter);


        TextView dateText = rootView.findViewById(R.id.date_text);
        dateText.setText(formatDate(selectedDate));

        dateText.setOnClickListener(v -> {
            setSelectedDate(chooseDate(dateText));
        });

        return rootView;
    }


    private String chooseDate(TextView dateText) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                selectedDate = simpleDateFormat.format(calendar.getTime());
                dateText.setText(formatDate(selectedDate));
            }
        };
        new DatePickerDialog(mContext,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
        return selectedDate;
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
}