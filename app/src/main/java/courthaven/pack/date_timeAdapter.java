package courthaven.pack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class date_timeAdapter extends ArrayAdapter<String> {
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

        String currentTime = timesList.get(position);

        TextView timeText = (TextView) listItem.findViewById(R.id.time_text);
        timeText.setText(currentTime);

        return listItem;
    }
}
