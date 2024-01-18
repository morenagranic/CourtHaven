package courthaven.pack;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class homeFragment extends Fragment {

    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        DataBaseHelper db = new DataBaseHelper(mContext);

        ArrayList<Court> courts = new ArrayList<>();
        courts.add(new Court(1, "name1", "address", "sport", 1));
        courts.add(new Court(1, "name2", "address", "sport", 1));
        courts.add(new Court(1, "name3", "address", "sport", 1));
        courts.add(new Court(1, "name4", "address", "sport", 1));
        courts.add(new Court(1, "name5", "address", "sport", 1));
        courts.add(new Court(1, "name1", "address", "sport", 1));
        courts.add(new Court(1, "name2", "address", "sport", 1));
        courts.add(new Court(1, "name3", "address", "sport", 1));
        courts.add(new Court(1, "name4", "address", "sport", 1));
        courts.add(new Court(1, "name5", "address", "sport", 1));

        ArrayList<Court> courts1 = db.getCourts();

        courtAdapter adapter = new courtAdapter(mContext, courts1);

        ListView listView = rootView.findViewById(R.id.courts_list);

        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }
 /*
    private ListView listView;
    private courtAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_home);
        super.onCreate(savedInstanceState);

        listView = (ListView) findViewById(R.id.courts_list);
        ArrayList<Court> courtsList = new ArrayList<>();
        courtsList.add(new Court(1, "Court 1", "Street 123", "tennis", 4));


        mAdapter = new courtAdapter(this, courtsList);
        listView.setAdapter(mAdapter);
    }*/

}