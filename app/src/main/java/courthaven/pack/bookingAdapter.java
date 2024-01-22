package courthaven.pack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class bookingAdapter extends ArrayAdapter<Booking> {
    private Context mContext;
    private ArrayList<Booking> bookingList = new ArrayList<>();

    public bookingAdapter(@NonNull Context context, @NonNull ArrayList<Booking> list) {
        super(context, 0, list);
        mContext = context;
        bookingList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.upcoming_list_item, parent, false);
        }

        int[] imageResources = {R.drawable.tennis_court1, R.drawable.basketball_court2, R.drawable.football_court3, R.drawable.basketball_court4,
                R.drawable.tennis_court5, R.drawable.basketball_court6, R.drawable.tennis_court7, R.drawable.basketball_court8,
                R.drawable.football_court9, R.drawable.basketball_court10, R.drawable.tennis_court11, R.drawable.basketball_court12};


        Booking currentBooking = bookingList.get(position);

        ImageView imageView = listItem.findViewById(R.id.imageView_court);
        imageView.setBackgroundResource(imageResources[currentBooking.getCourt().getId() - 1]);

        TextView courtName = listItem.findViewById(R.id.court1);
        courtName.setText(currentBooking.getCourt().getName());

        TextView courtAddress = listItem.findViewById(R.id.address1);
        courtAddress.setText(currentBooking.getCourt().getAddress());

        TextView dateTime = listItem.findViewById(R.id.date_time1);
        dateTime.setText(currentBooking.getTimePeriod());

        TextView total = listItem.findViewById(R.id.total1);
        total.setText("(Total = " + String.format("%.2f", currentBooking.getCost()) + "EUR)");


        TextView codeText = listItem.findViewById(R.id.code1);
        codeText.setText(String.valueOf(currentBooking.getCode()));


        return listItem;
    }

}
