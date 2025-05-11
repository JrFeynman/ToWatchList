package com.example.towatchlist.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.towatchlist.R;
import com.example.towatchlist.data.db.User;
import com.example.towatchlist.data.repository.AuthCallback;
import com.example.towatchlist.data.repository.UserRepository;
import com.example.towatchlist.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private UserRepository userRepo;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // ViewBinding ile inflate
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        userRepo = new UserRepository(requireContext());
        prefs    = requireActivity()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (prefs.getBoolean("isRemembered", false)) {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_login_to_home);
        }
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            // ← Asenkron login çağrısı
            userRepo.loginAsync(username, password, new AuthCallback() {
                @Override
                public void onComplete(User user, boolean isRegister) {
                    if (user != null) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putInt("userId", user.getId());

                        if (binding.cbRemember.isChecked()) {
                            edit.putBoolean("isRemembered", true);
                        } else {
                            edit.putBoolean("isRemembered", false);
                        }
                        edit.apply();

                        Navigation.findNavController(binding.getRoot())
                                .navigate(R.id.action_login_to_home);
                    } else {
                        Toast.makeText(getContext(),
                                "Kullanıcı adı veya şifre hatalı",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        // Kayıt ekranına geçiş linki
        binding.tvRegisterLink.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_login_to_register)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
