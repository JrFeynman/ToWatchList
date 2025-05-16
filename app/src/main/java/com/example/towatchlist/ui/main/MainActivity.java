package com.example.towatchlist.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.towatchlist.R;
import com.example.towatchlist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ➊ FragmentContainerView içindeki NavHostFragment’i alın
        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host);

        // ➋ Ondan NavController’ı çekin
        NavController navController = navHost.getNavController();

        // ➌ BottomNavigationView ile NavController’ı bağlayın
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
    }
}
