package courthaven.pack;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FilteredCourtsFragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filtered_courts, container, false);
        //Log.d("FilteredCourtsFragment", "onCreateView: mContext = " + mContext);

        // Retrieve selectedSports and maxDistance from arguments
        Bundle args = getArguments();
        ArrayList<String> selectedSports = (args != null) ? args.getStringArrayList("selectedSports") : new ArrayList<>();
        maxDistance = (args != null) ? args.getInt("maxDistance") : 1000;

        // Get courts data from the database based on selected sports and max distance
        DataBaseHelper db = new DataBaseHelper(mContext);
        String[] selectedSportsArray = selectedSports.toArray(new String[0]);

        ArrayList<Court> filteredCourts = db.getFilteredCourts(selectedSportsArray, maxDistance);

        int userId = SignInFragment.UserId;
        courtAdapter adapter = new courtAdapter(mContext, new ArrayList<>());
        adapter.initializeFavoritedCourtIds(userId);

        adapter.addAll(filteredCourts);


        // Attach the adapter to the ListView
        ListView listView = rootView.findViewById(R.id.courts_list);
        listView.setAdapter(adapter);

        // Find the filter button by ID
        ImageView cancel_icon1 = rootView.findViewById(R.id.cancel_icon1);

        // Set OnClickListener for the filter button
        cancel_icon1.setOnClickListener(v -> openHomeFragment());


        TextView dateText = rootView.findViewById(R.id.date_text);
        dateText.setText(formatDate(selectedDate));

        dateText.setOnClickListener(v -> {
            setSelectedDate(chooseDate(dateText));
        });
        return rootView;
    }

    private ArrayList<Integer> convertStringToIntegerList(String commaSeparatedString) {
        ArrayList<Integer> integerList = new ArrayList<>();
        String[] parts = commaSeparatedString.split(",");
        for (String part : parts) {
            try {
                int id = Integer.parseInt(part.trim());
                integerList.add(id);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Handle the case where the string is not a valid integer
            }
        }
        return integerList;
    }

    private void openHomeFragment() {
        // Create an instance of EditProfileFragment
        homeFragment homeFragment = new homeFragment();

        // Replace the current fragment with EditProfileFragment
        replaceFragment(homeFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
