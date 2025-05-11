package com.example.towatchlist.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.towatchlist.databinding.FragmentSearchBinding;
import com.example.towatchlist.ui.adapters.MovieAdapter;
import com.example.towatchlist.viewmodel.SearchViewModel;
import com.example.towatchlist.R;
public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding.btnSearch.setOnClickListener(v -> {
            String query = binding.etSearch.getText().toString().trim();
            viewModel.search(query);
            viewModel.getResults().observe(getViewLifecycleOwner(), movies -> {
                binding.rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rvSearch.setAdapter(new MovieAdapter(movies, m -> {
                    Bundle args = new Bundle();
                    args.putInt("movieId", m.getId());
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_searchFragment_to_detailFragment, args);
                }));
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}