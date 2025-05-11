package com.example.towatchlist.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.towatchlist.R;
import com.example.towatchlist.data.db.WatchlistMovie;
import java.util.List;
import java.util.Locale;

public class WatchlistAdapter
        extends RecyclerView.Adapter<WatchlistAdapter.Holder> {

    /**
     * onClick           → navigasyon (detail’e git)
     * onRemove          → silme butonuna tıklanınca
     * onWatchedToggle   → “Watched” checkbox değiştiğinde tetiklenir
     */
    public interface OnItemClick {
        void onClick(int movieId);
        void onRemove(WatchlistMovie movie);
        void onWatchedToggle(WatchlistMovie movie, boolean isWatched);
    }

    private final List<WatchlistMovie> list;
    private final OnItemClick listener;

    public WatchlistAdapter(List<WatchlistMovie> list, OnItemClick listener) {
        this.list     = list;
        this.listener = listener;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Burada item_movie.xml değil, item_watchlist.xml kullanıyoruz
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_watchlist, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        WatchlistMovie m = list.get(pos);

        // Başlık
        h.title.setText(m.getTitle());

        // Year (yyyy-MM-dd → yyyy)
        String date = m.getReleaseDate();
        String year = (date != null && date.length() >= 4)
                ? date.substring(0, 4)
                : "";
        h.year.setText("Year: " + year);

        // Rating
        h.rating.setText(String.format(Locale.getDefault(),
                "Rating: %.1f/10", m.getVoteAverage()));

        // Poster yükle
        Glide.with(h.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w200" + m.getPosterPath())
                .into(h.poster);

        // “Watched” checkbox durumu
        h.cbWatched.setOnCheckedChangeListener(null);
        h.cbWatched.setChecked(m.isWatched());
        h.cbWatched.setOnCheckedChangeListener((buttonView, isChecked) ->
                listener.onWatchedToggle(m, isChecked)
        );

        // Tek tık → detay sayfasına git
        h.itemView.setOnClickListener(v ->
                listener.onClick(m.getId())
        );

        // Silme butonu → onRemove
        h.btnRemove.setOnClickListener(v ->
                listener.onRemove(m)
        );
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView  title, year, rating;
        CheckBox  cbWatched;
        ImageButton btnRemove;

        Holder(@NonNull View itemView) {
            super(itemView);
            poster    = itemView.findViewById(R.id.ivPoster);
            title     = itemView.findViewById(R.id.tvTitle);
            year      = itemView.findViewById(R.id.tvYear);
            rating    = itemView.findViewById(R.id.tvRating);
            cbWatched = itemView.findViewById(R.id.cbWatched);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
