package courthaven.pack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import courthaven.pack.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {


    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = courthaven.pack.databinding.ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new homeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                replaceFragment(new homeFragment());
            }else if (item.getItemId() == R.id.reservations){
                replaceFragment(new reservationsFragment());
            }else if (item.getItemId() == R.id.favorites){
                replaceFragment(new favoritesFragment());
            }else if (item.getItemId() == R.id.events){
                replaceFragment(new eventsFragment());
            }else if (item.getItemId() == R.id.account){
                replaceFragment(new accountFragment());
            }
            return true;
        });
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}