package courthaven.pack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class courtAdapter extends ArrayAdapter<Court> {

    private Context mContext;
    private ArrayList<Court> courtsList = new ArrayList<>();


    public courtAdapter(@NonNull Context context,  @NonNull ArrayList<Court> list) {
        super(context, 0, list);
        mContext = context;
        courtsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.home_list_item,parent,false);

        Court currentCourt = courtsList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.imageView_court);
        image.setImageResource(currentCourt.getImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.court_name);
        name.setText(currentCourt.getName());

        TextView address = (TextView) listItem.findViewById(R.id.court_address);
        address.setText(currentCourt.getAddress());

        TextView sport = (TextView) listItem.findViewById(R.id.court_sport);
        sport.setText(currentCourt.getSport());

        return listItem;
    }
}

