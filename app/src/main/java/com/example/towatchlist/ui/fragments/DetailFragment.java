package com.example.towatchlist.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.towatchlist.data.db.WatchlistMovie;
import com.example.towatchlist.data.repository.MovieRepository;
import com.example.towatchlist.data.model.Cast;
import com.example.towatchlist.data.model.Crew;
import com.example.towatchlist.data.model.MovieDetail;
import com.example.towatchlist.databinding.FragmentDetailBinding;
import com.example.towatchlist.viewmodel.DetailViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding;
    private DetailViewModel viewModel;
    private MovieRepository movieRepo;
    private int movieId;
    private int userId;
    private MovieDetail currentDetail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        movieRepo = new MovieRepository(requireContext());

        // Argümandan movieId al
        if (getArguments() != null) {
            movieId = getArguments().getInt("movieId");
        }
        // SharedPreferences’ten userId al
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this)
                .get(DetailViewModel.class);

        // Detayı çek
        viewModel.fetchDetail(movieId);

        viewModel.getDetail().observe(getViewLifecycleOwner(), detail -> {
            if (detail == null) return;
            currentDetail = detail;

            // Poster
            Glide.with(binding.ivDetailPoster.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + detail.getPosterPath())
                    .into(binding.ivDetailPoster);

            // Başlık
            binding.tvDetailTitle.setText(detail.getTitle());

            // Rating
            binding.tvDetailRating.setText(
                    String.format(Locale.getDefault(),
                            "Rating: %.1f/10", detail.getVoteAverage())
            );

            // Year
            String rd = detail.getReleaseDate();
            String year = (rd != null && rd.length() >= 4) ? rd.substring(0, 4) : "";
            binding.tvDetailYear.setText("Year: " + year);

            // Açıklama
            binding.tvDetailDescription.setText(detail.getOverview());

            // Yönetmen
            List<String> directors = new ArrayList<>();
            for (Crew c : detail.getCredits().getCrew()) {
                if ("Director".equals(c.getJob())) directors.add(c.getName());
            }
            binding.tvDetailDirector.setText(
                    "Director: " + TextUtils.join(", ", directors)
            );

            // Yazarlar
            List<String> writers = new ArrayList<>();
            for (Crew c : detail.getCredits().getCrew()) {
                String job = c.getJob();
                if ("Writer".equals(job) || "Screenplay".equals(job)) {
                    writers.add(c.getName());
                }
            }
            binding.tvDetailWriters.setText(
                    "Writers: " + TextUtils.join(", ", writers)
            );

            // Cast
            List<String> castNames = new ArrayList<>();
            int count = Math.min(detail.getCredits().getCast().size(), 5);
            for (int i = 0; i < count; i++) {
                Cast cast = detail.getCredits().getCast().get(i);
                castNames.add(cast.getName());
            }
            binding.tvDetailCast.setText(
                    "Cast: " + TextUtils.join(", ", castNames)
            );

            // **Burada eklediğimiz kısım:**
            binding.btnAddToWatchlist.setOnClickListener(v -> {
                if (userId == -1) {
                    Toast.makeText(getContext(),
                            "Önce giriş yapmalısın", Toast.LENGTH_SHORT).show();
                    return;
                }
                WatchlistMovie wm = new WatchlistMovie(
                        detail.getId(),
                        detail.getTitle(),
                        detail.getPosterPath(),
                        detail.getReleaseDate(),
                        detail.getVoteAverage(),
                        userId
                );
                movieRepo.addToWatchlist(wm);
                Toast.makeText(getContext(),
                        "Filmi watchlist’e ekledin!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
