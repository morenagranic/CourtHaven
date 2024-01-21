package courthaven.pack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;



public class SignInFragment extends Fragment {
    public static int UserId = 0;
    private AuthenticationViewModel viewModel;
    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // If not signed in, inflate the sign-in layout
        View view = inflater.inflate(R.layout.sign_in_layout, container, false);

        // Initialize UI components
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        Button signInButton = view.findViewById(R.id.signInButton);

        // Set up sign-in button click listener
        signInButton.setOnClickListener(v -> attemptSignIn(editTextEmail.getText().toString(), editTextPassword.getText().toString()));

        Button signUpButton = view.findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(v -> ((MainActivity) requireActivity()).replaceFragment(new signUpFragment()));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
    }

    void attemptSignIn(String email, String password) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
        if (dataBaseHelper.dataExists("USER", "email", email)) {
            if (dataBaseHelper.dataMatches("USER", "email", email, "password", password)) {
                Toast.makeText(requireContext(), "Sign-in successful", Toast.LENGTH_SHORT).show();
                viewModel.setSignInSuccessful(true);

                // Set the UserId to the actual user ID from the database
                UserId = dataBaseHelper.getUserId(email, password);

                ((MainActivity) requireActivity()).replaceFragment(new homeFragment());
            } else {
                Toast.makeText(requireContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                viewModel.setSignInSuccessful(false);
            }
        } else {
            Toast.makeText(requireContext(), "User with provided email does not exist", Toast.LENGTH_SHORT).show();
            viewModel.setSignInSuccessful(false);
        }
    }

    public boolean isSignInSuccessful() {
        return viewModel.isSignInSuccessful();
    }
}