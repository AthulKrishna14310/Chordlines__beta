package com.integrals.chordlinesapp.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

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


    public void download(String Link,String extention) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                        Snackbar.with(activity,null)
                                .type(Type.SUCCESS)
                                .message("Downloading your file...")
                                .duration(Duration.SHORT)
                                .fillParent(true)
                                .textAlign(Align.LEFT)
                                .show();

                        String url = Link;
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setDescription("Chordlines ");
                        request.setTitle("Downloading your file....");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "chorldlines_"+System.currentTimeMillis()+extention);
                        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {

                        Snackbar.with(activity,null)
                                .type(Type.ERROR)
                                .message("Permission denied..")
                                .duration(Duration.SHORT)
                                .fillParent(true)
                                .textAlign(Align.LEFT)
                                .show();

                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();


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
                databaseReference1.child("DownloadLinkLow").setValue("");
                databaseReference1.child("DownloadLinkMedium").setValue("");
                databaseReference1.child("DownloadLinkHigh").setValue("");
                databaseReference1.child("DownloadLinkAudio").setValue("");

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
        final String[] DownloadLinkHigh = new String[1];
        final String[] DownloadLinkMedium = new String[1];
        final String[] DownloadLinkLow = new String[1];
        final String[] DownloadLinkAudio = new String[1];
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
                        if(snapshot.hasChild("DownloadLinkHigh")){
                             DownloadLinkHigh[0] =snapshot.child("DownloadLinkHigh").getValue().toString();

                            }
                        if(snapshot.hasChild("DownloadLinkLow")){
                            DownloadLinkLow[0] =snapshot.child("DownloadLinkLow").getValue().toString();

                        }
                        if(snapshot.hasChild("DownloadLinkMedium")){
                            DownloadLinkMedium[0] =snapshot.child("DownloadLinkMedium").getValue().toString();

                        }
                        if(snapshot.hasChild("DownloadLinkAudio")){
                            DownloadLinkAudio[0] =snapshot.child("DownloadLinkAudio").getValue().toString();

                        }

                    }

                    albumModels.add(new AlbumModel(CoverPic[0],YouTubeLink[0],PublishedON[0]
                            ,AlbumName[0],Details[0],DownloadLinkLow[0],DownloadLinkMedium[0],DownloadLinkHigh[0],DownloadLinkAudio[0]));

                }
                AlbumAdapter albumAdapter=new AlbumAdapter(context,albumModels,thisClass);
                recyclerView.setAdapter(albumAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void showDialogueForDownload(List<AlbumModel> albumModels, int position) {
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(activity)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setTitle("Download ")
                .setSingleChoiceItems(new String[]{"1920x1080 (High Quality )",
                        "640x360 (Medium Quality)", "256x144 (Low Quality)",
                        "Audio (MP3,128Kbps)"}, 4, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   switch (i){
                       case 0:
                           dialogInterface.dismiss();
                           Snackbar.with(activity,null)
                                 .type(Type.SUCCESS)
                                 .message("High Quality..")
                                 .duration(Duration.SHORT)
                                 .fillParent(true)
                                 .textAlign(Align.LEFT)
                                 .show();
                           download(albumModels.get(position).getDownloadLinkHigh().toString(),".mp4");
                         break;

                       case 1:
                           dialogInterface.dismiss();
                           Snackbar.with(activity,null)
                                   .type(Type.SUCCESS)
                                   .message("Medium Quality..")
                                   .duration(Duration.SHORT)
                                   .fillParent(true)
                                   .textAlign(Align.LEFT)
                                   .show();
                           download(albumModels.get(position).getDownloadLinkMedium().toString(),".mp4");

                           break;
                       case 2:
                           dialogInterface.dismiss();
                           Snackbar.with(activity,null)
                                   .type(Type.SUCCESS)
                                   .message("Low Quality..")
                                   .duration(Duration.SHORT)
                                   .fillParent(true)
                                   .textAlign(Align.LEFT)
                                   .show();
                           download(albumModels.get(position).getDownloadLinkLow().toString(),".mp4");
                           break;
                       case 3:
                           dialogInterface.dismiss();
                           Snackbar.with(activity,null)
                                   .type(Type.SUCCESS)
                                   .message("Audio Quality..")
                                   .duration(Duration.SHORT)
                                   .fillParent(true)
                                   .textAlign(Align.LEFT)
                                   .show();
                           download(albumModels.get(position).getDownloadLinkAudio().toString(),".mp3");
                           break;
                           }

                    }
                })
                .setDialogBackgroundColor(activity.getResources().getColor(R.color.cfdialogueColor))
                .setIcon(R.mipmap.ic_launcher_round)
                .addButton("  OK  ", -1, Color.BLUE, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
                    dialog.dismiss();
                });

        builder.show();





    }
}