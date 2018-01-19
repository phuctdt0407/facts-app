package com.example.phuct.myapplication.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.phuct.myapplication.R;
import com.example.phuct.myapplication.adapter.ImageAdapter;
import com.example.phuct.myapplication.config.Const;
import com.example.phuct.myapplication.model.Image;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference databaseReference;
    private ArrayList<Image> listImage;
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle toggle;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_main);

        initView();
        setOption();
        if(!isNetworkAvailable()){
            Toast.makeText(this,"No internet connection",Toast.LENGTH_LONG).show();
        }
        else{
            loadData(Const.RANDOM);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void initView(){
        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        listImage = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void hideStatusBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public boolean isNetworkAvailable(){
        final ConnectivityManager connectivityManager = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void setOption(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setCheckable(true);
        switch (item.getItemId()) {
            case R.id.nav_animal:
                listImage.clear();
                loadData(Const.ANIMAL);
                break;
            case R.id.nav_space:
                listImage.clear();
                loadData(Const.SPACE);
                break;
            default:
                listImage.clear();
                loadData(Const.RANDOM);
                return onOptionsItemSelected(item);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadData(String str) {
        databaseReference.child(str).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                childAdded(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void childAdded(DataSnapshot dataSnapshot){
        Image newImage = dataSnapshot.getValue(Image.class);
        if (newImage != null) {
            listImage.add(new Image(newImage.getURL()));
            ImageAdapter imageAdapter = new ImageAdapter(listImage, getApplicationContext());
            recyclerView.setAdapter(imageAdapter);
        }
    }
}
