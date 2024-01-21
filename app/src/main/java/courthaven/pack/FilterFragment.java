package courthaven.pack;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class FilterFragment extends Fragment {

    private ArrayList<String> selectedSports = new ArrayList<>();
    private int maxDistance = 1000; // Default max distance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        CheckBox checkBoxBasketball = rootView.findViewById(R.id.checkBox_basketball);
        CheckBox checkBoxTennis = rootView.findViewById(R.id.checkBox_tennis);
        CheckBox checkBoxFootball = rootView.findViewById(R.id.checkBox_football);

        EditText editTextMaxDistance = rootView.findViewById(R.id.editText_max_distance);
        Button buttonSaveChanges = rootView.findViewById(R.id.button_save_changes);

        buttonSaveChanges.setOnClickListener(v -> {
            // Clear the existing selection
            selectedSports.clear();

            // Check which sports are selected
            if (checkBoxBasketball.isChecked()) {
                selectedSports.add("basketball");
            }
            if (checkBoxTennis.isChecked()) {
                selectedSports.add("tennis");
            }
            if (checkBoxFootball.isChecked()) {
                selectedSports.add("football");
            }
            Log.d("Filtering", "Selected Sports: " + selectedSports.toString());

            // Get the maximum distance
            String maxDistanceStr = editTextMaxDistance.getText().toString();
            if (!maxDistanceStr.isEmpty()) {
                maxDistance = Integer.parseInt(maxDistanceStr);
            }

            openFilteredCourtsFragment(selectedSports, maxDistance);
        });

        return rootView;
    }

    public ArrayList<String> getSelectedSports() {
        return selectedSports;
    }

    private void openFilteredCourtsFragment(ArrayList<String> selectedSports, int maxDistance) {
        // Create an instance of FilteredCourtsFragment and pass the selectedSports array and maxDistance
        FilteredCourtsFragment filteredCourtsFragment = new FilteredCourtsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("selectedSports", selectedSports);
        args.putInt("maxDistance", maxDistance);
        filteredCourtsFragment.setArguments(args);

        // Replace the current fragment with FilteredCourtsFragment
        replaceFragment(filteredCourtsFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
