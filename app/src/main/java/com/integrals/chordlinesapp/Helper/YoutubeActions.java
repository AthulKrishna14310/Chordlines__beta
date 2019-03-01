package com.integrals.chordlinesapp.Helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class YoutubeActions {
    private Context context;

    public YoutubeActions(Context context) {
        this.context = context;
    }

    public  void subscribeYoutubeChannel(String id) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/channel/" + id));
        try {
           context.startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context,"Sorry... unable to open Youtube.",Toast.LENGTH_SHORT).show();
        }
    }

    public  void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
