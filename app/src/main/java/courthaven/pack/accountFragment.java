package courthaven.pack;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import courthaven.pack.SignInFragment;

import androidx.fragment.app.FragmentTransaction;

public class accountFragment extends Fragment {

    private TextView usernameTextView;
    private TextView emailTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Find the edit_icon ImageView
        ImageView editIcon = view.findViewById(R.id.edit_icon);

        // Find TextViews for username and email
        usernameTextView = view.findViewById(R.id.username);
        emailTextView = view.findViewById(R.id.email);

        // Set a click listener for the edit_icon
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileFragment();
            }
        });

        // Fetch and display user information
        fetchAndDisplayUserInfo();

        return view;
    }

    private void openEditProfileFragment() {
        // Create an instance of EditProfileFragment
        EditProfileFragment editProfileFragment = new EditProfileFragment();

        // Replace the current fragment with EditProfileFragment
        replaceFragment(editProfileFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void fetchAndDisplayUserInfo() {
        // Replace 'userId' with the actual user ID of the currently logged-in user
        int userId = SignInFragment.UserId;

        // Fetch user information from the database
        DataBaseHelper dbHelper = new DataBaseHelper(requireContext());
        User user = dbHelper.getUserById(userId);

        // Display user information in the TextViews
        usernameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
    }
}