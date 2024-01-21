package courthaven.pack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

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

        // Initialize favorited court IDs for the logged-in user
        int userId = SignInFragment.UserId;

        // Create an instance of courtAdapter
        courtAdapter adapter = new courtAdapter(mContext, new ArrayList<>());

        // Initialize favorited court IDs using the public method
        adapter.initializeFavoritedCourtIds(userId);

        // Get courts data from the database
        DataBaseHelper db = new DataBaseHelper(mContext);
        ArrayList<Court> courts1 = db.getCourts();

        // Set up the adapter with the loaded favorited court IDs
        adapter.addAll(courts1);


        // Attach the adapter to the ListView
        ListView listView = rootView.findViewById(R.id.courts_list);
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }
}
