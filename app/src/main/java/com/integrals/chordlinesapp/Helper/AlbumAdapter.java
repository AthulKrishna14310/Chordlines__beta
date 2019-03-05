package com.integrals.chordlinesapp.Helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.integrals.chordlinesapp.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
private Context context;
private List<AlbumModel> albumModels;
private FirebaseActions firebaseActions;
private Activity activity;
    public AlbumAdapter(Context context, List<AlbumModel> albumModels,FirebaseActions firebaseActions,
                        Activity activity) {
        this.context = context;
        this.firebaseActions=firebaseActions;
        this.albumModels = albumModels;
        this.activity=activity;
        ///
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.album_card, parent, false);
        return new AlbumViewHolder(view);

        }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        RequestOptions requestOptions=new RequestOptions().centerCrop();
        Glide.with(context)
                .load(albumModels.get(position).getCoverPic())
                .apply(requestOptions)
                .into(holder.CoverPic);
        holder.albumTitle.setText(albumModels.get(position).getAlbumName());



        holder.share_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.with(activity,null)
                        .type(Type.CUSTOM)
                        .message("Loading Youtube link...")
                        .duration(Duration.SHORT)
                        .fillParent(true)
                        .textAlign(Align.LEFT)
                        .show();

                firebaseActions.shareAlbumLink(albumModels.get(position).getYouTubeLink().toString());
                 }
        });
        holder.download_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseActions.showDialogueForDownload(albumModels,position);
            }
        });

        holder.album_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseActions.showDetails(albumModels.get(position).getDetails().toString().trim());
            }
        });
        holder.CoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoutubeActions youtubeActions=new YoutubeActions(context,activity);
                youtubeActions.watchYoutubeVideo(albumModels.get(position).getYouTubeLink().toString().trim());
            }
        });
        holder.play_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoutubeActions youtubeActions=new YoutubeActions(context,activity);
                youtubeActions.watchYoutubeVideo(albumModels.get(position).getYouTubeLink().toString().trim());
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumModels.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        public ImageView CoverPic;
        public TextView  albumTitle;
        public Button    download_album;
        public Button    share_link;
        public Button    album_details;
        public Button    play_album;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            CoverPic=(ImageView)itemView.findViewById(R.id.cover_pic);
            album_details=(Button)itemView.findViewById(R.id.album_details);
            albumTitle=(TextView) itemView.findViewById(R.id.albumTitle);
            share_link=(Button)itemView.findViewById(R.id.share_link);
            download_album=(Button)itemView.findViewById(R.id.download_album);
            play_album=(Button)itemView.findViewById(R.id.play_album_button);
        }
    }
}
