package com.application.juststocks;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar = findViewById(R.id.progress_circular);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                progressBar.setVisibility(View.VISIBLE);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);

            }

        });

        webView.loadUrl("https://juststocks.in/");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        if(id == R.id.register){
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/free-membership-registration/");
        } else if(id == R.id.login){
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/lms-login");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/");

        } else if (id == R.id.nav_charts) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/charts/");

        } else if (id == R.id.nav_series) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/series/");

        } else if (id == R.id.nav_groups) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/groups-2/");

        } else if (id == R.id.nav_chatroom) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/chatroom/");

        } else if (id == R.id.nav_activity) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/activity-2/");

        } else if (id == R.id.nav_instructors) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/members-2/");

        } else if (id == R.id.nav_courses) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/courses/");

        }  else if (id == R.id.nav_open_demat_account) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://onlinedemataccount.tradebulls.in/DematAccount/Demat_SubBroker?SubBroker=8jdf2EQsNGDRwkL6MoEAhg%3D%3D");

        }  else if (id == R.id.nav_about_us) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/about-us/");

        }  else if (id == R.id.nav_contact_us) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/contact-us/");
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
