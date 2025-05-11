package com.example.towatchlist.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.towatchlist.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private UserRepository repo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        repo = new UserRepository(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // "Giriş Yap" linkine tıklanınca login ekranına dön
        binding.tvLoginLink.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_register_to_login)
        );

        // Kayıt butonu tıklanınca
        binding.btnRegister.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String pwd      = binding.etPassword.getText().toString().trim();
            String pwd2     = binding.etPasswordConfirm.getText().toString().trim();

            // 1) Boş alan kontrolü
            if (TextUtils.isEmpty(username) ||
                    TextUtils.isEmpty(pwd) ||
                    TextUtils.isEmpty(pwd2)) {
                Toast.makeText(getContext(),
                        "Lütfen tüm alanları doldurun",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 2) Şifre eşleşmesi
            if (!pwd.equals(pwd2)) {
                Toast.makeText(getContext(),
                        "Şifreler eşleşmiyor",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 3) Asenkron kayıt işlemi
            repo.registerAsync(username, pwd, new AuthCallback() {
                @Override
                public void onComplete(User user, boolean isRegister) {
                    if (user != null && isRegister) {
                        Toast.makeText(getContext(),
                                "Kayıt başarılı! Giriş ekranına yönlendiriliyorsunuz",
                                Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view)
                                .navigate(R.id.action_register_to_login);
                    } else {
                        Toast.makeText(getContext(),
                                "Bu kullanıcı adı zaten alınmış",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
