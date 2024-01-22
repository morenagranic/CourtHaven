package courthaven.pack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class date_timeAdapter extends ArrayAdapter<String> {
    Court court = new Court();
    String selectedDate = new String();

    public String getSelectedDate() { return selectedDate; }
    public void setSelectedDate(String selectedDate) { this.selectedDate = selectedDate; }

    public Court getCourt() { return court; }
    public void setCourt(Court court) { this.court = court; }
    private Context mContext;
    private ArrayList<String> timesList = new ArrayList<>();

    public date_timeAdapter(@NonNull Context context, @NonNull ArrayList<String> list) {
        super(context, 0, list);
        mContext = context;
        timesList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.time_list_item, parent, false);
        }

        DataBaseHelper db = new DataBaseHelper(mContext);


        String currentTime = timesList.get(position);

        TextView timeText = (TextView) listItem.findViewById(R.id.time_text);
        timeText.setText(currentTime);



        if (db.isBooked(court.getId(), db.getDateTimeId(selectedDate, currentTime))) {
            timeText.setTextColor(ContextCompat.getColor(mContext, R.color.light_red));
            timeText.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lighter_gray));
            timeText.setOnClickListener( v -> {
                Toast.makeText(mContext, "Unavailable", Toast.LENGTH_SHORT).show();
            });

        }

        return listItem;
    }

}
