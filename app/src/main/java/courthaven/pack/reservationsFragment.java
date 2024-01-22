package courthaven.pack;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class reservationsFragment extends Fragment {

    Context mContext;
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);
        bookingAdapter adapter = new bookingAdapter(mContext, new ArrayList<>());

        int id_user = SignInFragment.UserId;

        DataBaseHelper db = new DataBaseHelper(mContext);
        ArrayList<Booking> bookings = db.getBookings(id_user);

        adapter.addAll(bookings);

        listView = view.findViewById(R.id.reservations_list);
        listView.setAdapter(adapter);

        View footer = getLayoutInflater().inflate(R.layout.footer, null);
        listView.addFooterView(footer);
        View header = getLayoutInflater().inflate(R.layout.header, null);
        listView.addHeaderView(header);


        return view;
    }
}
