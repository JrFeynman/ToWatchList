package com.example.towatchlist.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.towatchlist.databinding.FragmentHomeBinding;
import com.example.towatchlist.data.model.Movie;
import com.example.towatchlist.ui.adapters.MovieAdapter;
import com.example.towatchlist.viewmodel.HomeViewModel;
import java.util.List;
import com.example.towatchlist.R;
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        int defaultGenreId = 28; // Örnek: Action türü
        viewModel.fetchMovies(defaultGenreId);
        viewModel.getMovies().observe(getViewLifecycleOwner(), movies -> setupRecyclerView(movies));
    }

    private void setupRecyclerView(List<Movie> movies) {
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMovies.setAdapter(new MovieAdapter(movies, m -> {
            Bundle args = new Bundle();
            args.putInt("movieId", m.getId());
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_homeFragment_to_detailFragment, args);
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}