package com.adjo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import DataBase.Operation;
import DataBase.OperationCtrl;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final int RESULT_EDIT_ACTIVITY = 0;
    private static final int RESULT_PARAM_ACTIVITY = 1;

    TextView tv_nom ;
    TextView tv_prenom;
    TextView tv_commerce;

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //FOR FRAGMENTS
    private Fragment fragmentStat;
    private Fragment fragmentHome;

    //FOR DATAS
    private static final int FRAGMENT_STAT = 0;
    private static final int FRAGMENT_HOME = 1;
    private OperationCtrl operationCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Edit.class);
                startActivityForResult(intent, RESULT_EDIT_ACTIVITY);
            }
        });

        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        showHomeFragment();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(getApplicationContext(), ParametresActivity.class);
            startActivityForResult(intent, RESULT_PARAM_ACTIVITY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_statistiques:
                this.showFragment(FRAGMENT_STAT);
                break;
            case R.id.activity_main_drawer_acceuil:
                this.showFragment(FRAGMENT_HOME);
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(int fragmentId) {
        switch (fragmentId) {
            case FRAGMENT_STAT:
                this.showStatFragment();
                break;
            case FRAGMENT_HOME:
                this.showHomeFragment();
                break;
        }
    }

    private void showHomeFragment() {
        if (this.fragmentHome == null) this.fragmentHome = HomeFragment.newInstance();
        this.startTransactionFragment(this.fragmentHome);
    }

    private void showStatFragment() {
        if (this.fragmentStat == null) this.fragmentStat = StatFragment.newInstance();
        this.startTransactionFragment(this.fragmentStat);
    }

    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_mail_frame_layout, fragment).commit();
        }
    }

    private void configureToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout () {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_mail_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_EDIT_ACTIVITY:
                this.fragmentStat = null;
                this.fragmentHome = null;
                showHomeFragment();
                break;
            case RESULT_PARAM_ACTIVITY:
                showUserSettings();
                break ;
        }
    }

    private void showUserSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("prefUsername", "NULL");
        String userSurname = sharedPreferences.getString("prefUsersurname", "NULL");
        String commerce = sharedPreferences.getString("prefCom_name", "NULL");
        String monnaie = sharedPreferences.getString("monnaie", "NULL");

        tv_nom = findViewById(R.id.activity_mail_nav_header_nom);
        tv_prenom = findViewById(R.id.activity_mail_nav_header_prenom);
        tv_commerce = findViewById(R.id.activity_mail_nav_header_commerce);


        tv_nom.setText(username);
        tv_prenom.setText(userSurname);
        tv_commerce.setText(commerce);
    }
}
