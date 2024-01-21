package courthaven.pack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class EditProfileFragment extends Fragment {

    private EditText etChangeName;
    private EditText etChangeDescription;
    private Button saveChangesButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etChangeName = view.findViewById(R.id.etChangeName);
        etChangeDescription = view.findViewById(R.id.etChangeDescription);
        saveChangesButton = view.findViewById(R.id.saveChangesButton);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangesToDatabase();
            }
        });

        return view;
    }
    private void openHomeFragment() {
        homeFragment homeFragment = new homeFragment();

        // Replace the current fragment with EditProfileFragment
        replaceFragment(homeFragment);

    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void saveChangesToDatabase() {

            String newName = etChangeName.getText().toString();
            String newDescription = etChangeDescription.getText().toString();

            // Replace 'userId' with the actual user ID of the currently logged-in user
            int userId = SignInFragment.UserId;

            // Save changes to the database
            DataBaseHelper dbHelper = new DataBaseHelper(requireContext());
            dbHelper.updateUserName(newName, userId);
            dbHelper.updateUserDescription(newDescription, userId);

            // You might also want to update the UI or show a success message
            openHomeFragment();


    }

}
