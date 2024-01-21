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

public class signUpFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.sign_up_layout, container, false);

        EditText editTextEmailSignUp = view.findViewById(R.id.editTextEmailSignUp);
        EditText editTextPasswordSignUp = view.findViewById(R.id.editTextPasswordSignUp);
        EditText editTextRepeatPasswordSignUp = view.findViewById(R.id.editTextRepeatPasswordSignUp);
        Button signUpButton = view.findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(v -> attemptSignUp(
                editTextEmailSignUp.getText().toString(),
                editTextPasswordSignUp.getText().toString(),
                editTextRepeatPasswordSignUp.getText().toString()));

        Button signInButton = view.findViewById(R.id.signInButton);

        signInButton.setOnClickListener(v -> ((MainActivity) requireActivity()).replaceFragment(new SignInFragment()));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
    }

    private void attemptSignUp(String email, String password, String repeatPassword) {
        if (isValidEmail(email) && isValidPassword(password, repeatPassword)) {
            viewModel.setSignUpSuccessful(true);

            User user = new User(0, "","", email, password,"", false); // Set an empty string for the name

            DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);

            if (dataBaseHelper.dataExists("USER", "email", email)) {
                Toast.makeText(requireContext(), "Email is already taken", Toast.LENGTH_SHORT).show();
                viewModel.setSignUpSuccessful(false);
            } else {
                boolean success = dataBaseHelper.addUser(user);

                if (success) {
                    Toast.makeText(mContext, "User added to database", Toast.LENGTH_SHORT).show();
                    viewModel.setSignUpSuccessful(true);
                } else {
                    Toast.makeText(mContext, "Error adding user to the database", Toast.LENGTH_SHORT).show();
                }
                navigateToSignInScreen();
            }

        } else {
            Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            viewModel.setSignUpSuccessful(false);
        }
    }

    private void navigateToSignInScreen() {
        if (requireActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) requireActivity();
            mainActivity.replaceFragment(new SignInFragment());
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    private boolean isValidPassword(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

    public boolean isSignUpSuccessful() {
        return viewModel.isSignUpSuccessful();
    }
}