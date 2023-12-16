package courthaven.pack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import courthaven.pack.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new homeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home){
                replaceFragment(new homeFragment());
            }
            else if (item.getItemId() == R.id.reservations){
                replaceFragment(new reservationsFragment());
            }
            else if (item.getItemId() == R.id.favorites){
                replaceFragment(new favoritesFragment());
            }
            else if (item.getItemId() == R.id.events){
                replaceFragment(new eventsFragment());
            }
            else if (item.getItemId() == R.id.discounts){
                replaceFragment(new discountsFragment());
            }
            return true;
        });
    }
    
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}