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

public class homeFragment extends Fragment {

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        int userId = SignInFragment.UserId;
        courtAdapter adapter = new courtAdapter(mContext, new ArrayList<>());
        adapter.initializeFavoritedCourtIds(userId);

        DataBaseHelper db = new DataBaseHelper(mContext);
        ArrayList<Court> courts1 = db.getCourts();

        adapter.addAll(courts1);

        ListView listView = rootView.findViewById(R.id.courts_list);
        listView.setAdapter(adapter);


        ImageView filterButton = rootView.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> filterCourts());


        TextView dateText = rootView.findViewById(R.id.date_text);
        dateText.setOnClickListener(v -> showDateDialog());

        // Inflate the layout for this fragment
        return rootView;
    }

    private void filterCourts()
    {
        openFilterFragment();
    }

    private void openFilterFragment() {
        // Create an instance of EditProfileFragment
        FilterFragment filterFragment = new FilterFragment();

        // Replace the current fragment with EditProfileFragment
        replaceFragment(filterFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showDateDialog() {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                Toast.makeText(mContext, simpleDateFormat.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            }
        };
        new DatePickerDialog(mContext,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
