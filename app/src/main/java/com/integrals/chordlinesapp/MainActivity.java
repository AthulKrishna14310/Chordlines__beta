package com.integrals.chordlinesapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.integrals.chordlinesapp.Helper.FirebaseActions;
import com.integrals.chordlinesapp.Helper.YoutubeActions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import io.github.tonnyl.light.Light;

import static com.chootdev.csnackbar.Snackbar.show;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LikeButton likeButton;
    private FirebaseActions firebaseActions;
    private DatabaseReference databaseReferenceCreateAlbum;
    private DatabaseReference databaseReferenceMajor;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReferenceAlbumList;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.activity=this;
        firebaseActions=new FirebaseActions(getApplicationContext(),activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_open);
        toolbar.setSubtitle("Please wait..");
        setSupportActionBar(toolbar);




        recyclerView=(RecyclerView)findViewById(R.id.albumListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);

        likeButton=(LikeButton)findViewById(R.id.like_button);
        databaseReferenceCreateAlbum=FirebaseDatabase.getInstance().getReference().child("Posts");
        databaseReferenceMajor=FirebaseDatabase.getInstance().getReference();
        databaseReferenceAlbumList=FirebaseDatabase.getInstance().getReference().child("Posts");


        SharedPreferences sharedPreferences=getSharedPreferences("Liked.pref",MODE_PRIVATE);
        Boolean Index=sharedPreferences.getBoolean("Index::",false);
        if(Index==false){
        likeButton.setLiked(false);
        }else {

            likeButton.setLiked(true);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseActions.loadRecyclerView(databaseReferenceAlbumList,recyclerView);
        Light.info(recyclerView, "For our music services", Light.LENGTH_LONG)

                .setActionTextColor(Color.WHITE)
                .setAction("CONTACT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showContactDialogue();
                    }
                }).show();

    }


    @Override
    protected void onResume() {
        super.onResume();


        databaseReferenceMajor.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toolbar.setSubtitle(dataSnapshot.getValue().toString().trim()+" likes__");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                firebaseActions.addLike(databaseReferenceMajor);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                firebaseActions.removeLike(databaseReferenceMajor);
            }
        });







    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            firebaseActions.createAlbum(databaseReferenceCreateAlbum);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
         if (id == R.id.nav_invite) {
             Snackbar.with(activity,null)
                     .type(Type.CUSTOM)
                     .message("Loading App invite  link...")
                     .duration(Duration.SHORT)
                     .fillParent(true)
                     .textAlign(Align.LEFT)
                     .show();
             firebaseActions.sentAppInviteLink(databaseReferenceMajor);

        } else if (id == R.id.nav_contact) {
             showContactDialogue();
          } else if(id == R.id.nav_subscribe){
             YoutubeActions youtubeActions= new YoutubeActions(getApplicationContext(),activity);
             youtubeActions.subscribeYoutubeChannel("UCvo6q3_ZBqUuJZ-g_eak1-Q");


         }

        return true;
    }

    public void addLikes(View view) {
    Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

    }

    private void showContactDialogue(){
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setContentImageDrawable(R.drawable.ic_buisness_card__)
                .setDialogBackgroundColor(getResources().getColor(R.color.cfdialoguecolorContact))
                .setMessage("Devoloper-Email : athul.krishna14310@gmail.com")
                .addButton(" SHARE CONTACT ", -1, Color.GRAY, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
                    dialog.dismiss();
                    Snackbar.with(activity,null)
                            .type(Type.CUSTOM)
                            .message("Loading Buisness card..")
                            .duration(Duration.SHORT)
                            .fillParent(true)
                            .textAlign(Align.LEFT)
                            .show();
                    runOnUiThread(() -> firebaseActions.shareImage());
                })
                .addButton("  OK  ", -1, Color.GRAY, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
                    dialog.dismiss();
                });


        builder.show();

    }

}
