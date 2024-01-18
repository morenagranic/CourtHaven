package courthaven.pack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import courthaven.pack.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initially, set the currentFragment to SignInFragment
        currentFragment = new SignInFragment();
        replaceFragment(currentFragment);


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (isSignInSuccessful()) {
                handleNavigation(item.getItemId());
                return true;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof signUpFragment) {
            // If the current fragment is SignUpFragment, replace it with SignInFragment
            currentFragment = new SignInFragment();
            replaceFragment(currentFragment);
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private boolean isSignInSuccessful() {
        if (currentFragment instanceof SignInFragment) {
            return ((SignInFragment) currentFragment).isSignInSuccessful();
        } else if (currentFragment instanceof signUpFragment) {
            return ((signUpFragment) currentFragment).isSignUpSuccessful();
        }
        return false;
    }

    private void handleNavigation(int itemId) {
        if (itemId == R.id.home) {
            replaceFragment(new homeFragment());
        } else if (itemId == R.id.reservations) {
            replaceFragment(new reservationsFragment());
        } else if (itemId == R.id.favorites) {
            replaceFragment(new favoritesFragment());
        } else if (itemId == R.id.events) {
            replaceFragment(new eventsFragment());
        } else if (itemId == R.id.account) {
            replaceFragment(new accountFragment());
        }
    }


}