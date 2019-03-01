package com.integrals.chordlinesapp.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.integrals.chordlinesapp.R;

import java.util.ArrayList;
import java.util.List;


public class FirebaseActions {
    private Context context;
    private List<AlbumModel> albumModels;
    private FirebaseActions thisClass;
    private Activity activity;

    public FirebaseActions(Context context,Activity activity) {
        this.thisClass=this;
        this.context = context;
        this.activity=activity;

    }

    public void sentAppInviteLink() {
    }

    public void addLike(DatabaseReference databaseReference) {
        final Boolean[] Liked = {false};
        databaseReference.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!Liked[0]) {
                    String L = dataSnapshot.getValue().toString();
                    int LI = Integer.parseInt(L);
                    dataSnapshot.getRef().setValue(LI + 1);
                    Liked[0] = true;


                    SharedPreferences sharedPreferences=context.getSharedPreferences("Liked.pref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("Index::",true);
                    editor.commit();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void removeLike(DatabaseReference databaseReference) {
        final Boolean[] unLiked = {false};
        databaseReference.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!unLiked[0]) {
                    String L = dataSnapshot.getValue().toString();
                    int LI = Integer.parseInt(L);
                    dataSnapshot.getRef().setValue(LI - 1);
                    unLiked[0] = true;

                    SharedPreferences sharedPreferences=context.getSharedPreferences("Liked.pref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("Index::",false);
                    editor.commit();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void downloadVedio(String LInk) {

    }

    public void showDetails(String s) {
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(activity)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setTitle("Chordlines Music Production")
                .setMessage(s+"\n")
                .setDialogBackgroundColor(activity.getResources().getColor(R.color.cfdialogueColor))
                .setIcon(R.mipmap.ic_launcher_round)
                .addButton("  OK  ", -1, Color.BLUE, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
                    dialog.dismiss();
                });

            builder.show();
    }


    public void createAlbum(DatabaseReference databaseReference) {

        String PushKey = databaseReference.push().getKey();
        DatabaseReference databaseReference1 = databaseReference.child(PushKey);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference1.child("CoverPic").setValue("");
                databaseReference1.child("YoutubeLink").setValue("");
                databaseReference1.child("PublishedON").setValue("");
                databaseReference1.child("AlbumName").setValue("");
                databaseReference1.child("Details").setValue("");
                databaseReference1.child("DownloadLink").setValue("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error .. Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void shareAlbumLink(String s) {
        Intent SharingIntent = new Intent(Intent.ACTION_SEND);
        SharingIntent.setType("text/plain");
        SharingIntent.putExtra(Intent.EXTRA_TEXT, "Chordlines Music Youtube Link" +"\n\n" + s);
        SharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(SharingIntent);

        }

    public void loadRecyclerView(DatabaseReference databaseReference, RecyclerView recyclerView) {
        albumModels=new ArrayList<>();
        final String[] CoverPic = new String[1];
        final String[] AlbumName = new String[1];
        final String[] Details = new String[1];
        final String[] YouTubeLink = new String[1];
        final String[] DownloadLink = new String[1];
        final String[] PublishedON=new String[1];
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                albumModels.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.hasChildren()) {
                        if(snapshot.hasChild("CoverPic")){
                         CoverPic[0] =snapshot.child("CoverPic").getValue().toString();
                        }
                        if(snapshot.hasChild("YoutubeLink")){

                             YouTubeLink[0] =snapshot.child("YoutubeLink").getValue().toString();

                        }
                        if(snapshot.hasChild("PublishedON")){
                             PublishedON[0]=snapshot.child("PublishedON").getValue().toString();
                        }
                        if(snapshot.hasChild("AlbumName")){
                             AlbumName[0] =snapshot.child("AlbumName").getValue().toString();

                        }
                        if(snapshot.hasChild("Details")){
                             Details[0] =snapshot.child("Details").getValue().toString();

                        }
                        if(snapshot.hasChild("DownloadLink")){
                             DownloadLink[0] =snapshot.child("DownloadLink").getValue().toString();

                            }

                    }

                    albumModels.add(new AlbumModel(CoverPic[0],YouTubeLink[0],PublishedON[0]
                            ,AlbumName[0],Details[0],DownloadLink[0]));

                }
                AlbumAdapter albumAdapter=new AlbumAdapter(context,albumModels,thisClass);
                recyclerView.setAdapter(albumAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




}