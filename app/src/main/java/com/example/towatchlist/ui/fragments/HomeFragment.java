package com.example.towatchlist.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.towatchlist.R;
import com.example.towatchlist.data.model.Movie;
import com.example.towatchlist.databinding.FragmentHomeBinding;
import com.example.towatchlist.ui.adapters.MovieAdapter;
import com.example.towatchlist.viewmodel.HomeViewModel;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private MovieAdapter adapter;

    // TMDB tür ID’leri
    private static final int GENRE_ACTION  = 28;
    private static final int GENRE_COMEDY  = 35;
    private static final int GENRE_DRAMA   = 18;
    private static final int GENRE_HORROR  = 27;
    private static final int GENRE_ROMANCE = 10749;
    private static final int GENRE_SCIFI   = 878;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) ViewModel’i al
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory
                        .getInstance(requireActivity().getApplication())
        ).get(HomeViewModel.class);

        // 2) Adapter’ı oluşturup RecyclerView’a ata
        adapter = new MovieAdapter(new ArrayList<>(), m -> {
            Bundle args = new Bundle();
            args.putInt("movieId", m.getId());
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_homeFragment_to_detailFragment, args);
        });
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMovies.setAdapter(adapter);

        // 3) Tek LiveData’yı observe et
        viewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                adapter.setMovies(movies);
            }
        });

        // 4) Tür butonlarına tıklanınca ViewModel’e ilet
        binding.btnGenreAction.setOnClickListener(v -> viewModel.setGenre(GENRE_ACTION));
        binding.btnGenreComedy.setOnClickListener(v -> viewModel.setGenre(GENRE_COMEDY));
        binding.btnGenreDrama.setOnClickListener(v -> viewModel.setGenre(GENRE_DRAMA));
        binding.btnGenreHorror.setOnClickListener(v -> viewModel.setGenre(GENRE_HORROR));
        binding.btnGenreRomance.setOnClickListener(v -> viewModel.setGenre(GENRE_ROMANCE));
        binding.btnGenreSciFi.setOnClickListener(v -> viewModel.setGenre(GENRE_SCIFI));

        // 5) Uygulama açılır açılmaz default türü yükle
        viewModel.setGenre(GENRE_ACTION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
