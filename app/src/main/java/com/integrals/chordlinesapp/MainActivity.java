package com.integrals.chordlinesapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LikeButton likeButton;
    private FirebaseActions firebaseActions;
    private DatabaseReference databaseReferenceCreateAlbum;
    private DatabaseReference databaseReferenceLikes;
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
        databaseReferenceLikes=FirebaseDatabase.getInstance().getReference();
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
        }


    @Override
    protected void onResume() {
        super.onResume();


        databaseReferenceLikes.child("Likes").addValueEventListener(new ValueEventListener() {
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
                firebaseActions.addLike(databaseReferenceLikes);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                firebaseActions.removeLike(databaseReferenceLikes);
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

             firebaseActions.sentAppInviteLink();

        } else if (id == R.id.nav_contact) {


             CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                     .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                     .setTitle("Chordlines Music Production")

                     .setMessage("Contact us : \n\n"+
                             " +918921407256 \n\n" +
                             " +919539483289   " +
                             "\n\n for all music services. \n\n Paytm and Google Pay accepted\n")
                     .setDialogBackgroundColor(getResources().getColor(R.color.cfdialogueColor))
                     .setIcon(R.drawable.logo_chordlines__)
                     .addButton("  OK  ", -1, Color.BLUE, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
                         dialog.dismiss();
                     });


             builder.show();


        } else if(id == R.id.nav_subscribe){
             YoutubeActions youtubeActions= new YoutubeActions(getApplicationContext());
             youtubeActions.subscribeYoutubeChannel("UCvo6q3_ZBqUuJZ-g_eak1-Q");


         }

        return true;
    }

    public void addLikes(View view) {
    Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

    }
}
