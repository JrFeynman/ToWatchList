package com.example.towatchlist.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.towatchlist.R;
import com.example.towatchlist.data.model.Movie;
import java.util.List;
import java.util.Locale;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder> {
    public interface OnItemClick { void onClick(Movie m); }
    private final List<Movie> list;
    private final OnItemClick listener;

    public MovieAdapter(List<Movie> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Movie m = list.get(pos);
        // 1) Başlık
        h.title.setText(m.getTitle());

        // 2) Yıl (YYYY-MM-DD → YYYY)
        String date = m.getReleaseDate();
        String year = (date!=null && date.length()>=4) ? date.substring(0,4) : "";
        h.year.setText("Year: " + year);

        // 3) Puan
        h.rating.setText(
                String.format(Locale.getDefault(),
                        "Rating: %.1f/10", m.getVoteAverage())
        );

        // 4) Poster yükleme
        Glide.with(h.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w200" + m.getPosterPath())
                .into(h.poster);

        // 5) Tıklama
        h.itemView.setOnClickListener(v -> listener.onClick(m));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, year, rating;
        Holder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.ivPoster);
            title  = itemView.findViewById(R.id.tvTitle);
            year   = itemView.findViewById(R.id.tvYear);
            rating = itemView.findViewById(R.id.tvRating);
        }
    }
}
