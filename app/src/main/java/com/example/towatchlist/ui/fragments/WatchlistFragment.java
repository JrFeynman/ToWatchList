package com.example.towatchlist.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.towatchlist.R;
import com.example.towatchlist.databinding.FragmentWatchlistBinding;
import com.example.towatchlist.data.db.WatchlistMovie;
import com.example.towatchlist.ui.adapters.WatchlistAdapter;
import com.example.towatchlist.viewmodel.WatchlistViewModel;
import java.util.List;

public class WatchlistFragment extends Fragment {
    private FragmentWatchlistBinding binding;
    private WatchlistViewModel viewModel;
    private int userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false);
        // SharedPreferences’ten userId oku
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Çıkış Yap butonunu dinle
        // ↓ Burayı güncelledik ↓
        binding.btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = requireActivity()
                    .getSharedPreferences("prefs", Context.MODE_PRIVATE);
            prefs.edit()
                    .remove("isRemembered")
                    .remove("userId")
                    .apply();
            // Global action ile login’e dön
            Navigation.findNavController(v)
                    .navigate(R.id.action_global_loginFragment);
        });

        // ViewModel’i al
        AndroidViewModelFactory factory =
                AndroidViewModelFactory.getInstance(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory)
                .get(WatchlistViewModel.class);

        // Kullanıcının watchlist’ini yükle
        viewModel.fetchForUser(userId);

        // Listeyi observe edip RecyclerView’a ata
        viewModel.getList().observe(getViewLifecycleOwner(), this::setupRecyclerView);
    }

    private void setupRecyclerView(List<WatchlistMovie> list) {
        binding.rvWatchlist.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
        binding.rvWatchlist.setAdapter(new WatchlistAdapter(
                list,
                new WatchlistAdapter.OnItemClick() {
                    @Override
                    public void onClick(int movieId) {
                        Bundle args = new Bundle();
                        args.putInt("movieId", movieId);
                        Navigation.findNavController(binding.getRoot())
                                .navigate(R.id.action_watchlistFragment_to_detailFragment, args);
                    }

                    @Override
                    public void onRemove(WatchlistMovie movie) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Çıkarılsın mı?")
                                .setMessage(movie.getTitle() + " watchlist'ten çıkarılsın mı?")
                                .setPositiveButton("Evet", (dlg, i) -> {
                                    viewModel.removeFromWatchlist(movie);
                                    viewModel.fetchForUser(userId);
                                })
                                .setNegativeButton("Hayır", null)
                                .show();
                    }

                    @Override
                    public void onWatchedToggle(WatchlistMovie movie, boolean isWatched) {
                        viewModel.markWatched(movie.getId(), userId, isWatched);
                    }
                }
        ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
