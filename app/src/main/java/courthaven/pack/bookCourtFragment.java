package courthaven.pack;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


public class bookCourtFragment extends Fragment {
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_court, container, false);

        date_timeAdapter adapter = new date_timeAdapter(mContext, new ArrayList<>());

        // Get courts data from the database
        DataBaseHelper db = new DataBaseHelper(mContext);
        ArrayList<String> times = db.getTimes("23-01-2024");
        adapter.addAll(times);

        // Attach the adapter to the ListView
        GridView listView = view.findViewById(R.id.times_grid);
        listView.setAdapter(adapter);

        ImageView cancelX = view.findViewById(R.id.cancel_icon1);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        cancelX.setOnClickListener(v -> ((MainActivity) requireActivity()).replaceFragment(new homeFragment()));

        cancelBtn.setOnClickListener(v -> ((MainActivity) requireActivity()).replaceFragment(new homeFragment()));


        return view;
    }

    void backToHomeFragment(View v) {
        Fragment fragment = new homeFragment();
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}