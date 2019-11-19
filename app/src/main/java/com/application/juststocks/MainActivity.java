package com.application.juststocks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
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

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private ProgressBar progressBar;
    private Stack<String> stack;
    private static boolean firstPress = true;
    private HashMap<String, Integer> hashMap = new HashMap<>();
    private NavigationView navigationView;
    private String lastOpen;
    private String loginStatus;
    private Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable d = getResources().getDrawable(R.drawable.ic_group_add_black_24dp);
        toolbar.setOverflowIcon(d);

        progressBar = findViewById(R.id.progress_circular);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);

            }

        });

        webView.loadUrl("https://juststocks.in/");
        stack = new Stack<String>();
        stack.push("https://juststocks.in/");
        hashMap.put("https://juststocks.in/", 0);
        navigationView.getMenu().getItem(0).setChecked(true);

        periodicChecks();

    }

    private void periodicChecks() {
        t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      checkForLogin();
                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                  }

                              },
        //Set how long before to start calling the TimerTask (in milliseconds)
                0,
        //Set the amount of time between each execution (in milliseconds)
                1000*10);
    }

    private void checkForLogin() {
        //Creating an object of our api interface
        ApiService api = RetroClient.getApiService();
        Call<Login> call = api.getMyJSON();
        /**
         * Enqueue Callback will be call when get response...
         */
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                //Dismiss Dialog

                if (response.isSuccessful()) {
                    /**
                     * Got Successfully
                     */
                    loginStatus = response.body().getLogin();
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            try {
                if(firstPress) {
                    stack.pop();
                    firstPress = false;
                }
                String url = stack.pop();
                lastOpen = url;
                navigationView.getMenu().getItem(hashMap.get(url)).setChecked(true);
                if (url != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    webView.loadUrl(url);
                }
                else {
                    finish();
                }
            } catch (EmptyStackException e){
                finish();
                Log.e("Empty stack", e.toString());
            }

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

        if (id == R.id.register) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/free-membership-registration/");
        } else if (id == R.id.login) {
            if(item.getTitle().equals("Log In")) {
                progressBar.setVisibility(View.VISIBLE);
                webView.loadUrl("https://juststocks.in/lms-login");
            } else if(item.getTitle().equals("Log Out")){
                progressBar.setVisibility(View.VISIBLE);
                webView.loadUrl("https://juststocks.in/lms-logout"); //Change here
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(lastOpen!=null){
            stack.push(lastOpen);
        }

        if (id == R.id.nav_home) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/");
            hashMap.put("https://juststocks.in/", 0);
            stack.push("https://juststocks.in/");
            firstPress = true;

        } else if (id == R.id.nav_charts) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/charts/");
            stack.push("https://juststocks.in/charts/");
            hashMap.put("https://juststocks.in/charts/", 1);
            firstPress = true;


        } else if (id == R.id.nav_series) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/series/");
            stack.push("https://juststocks.in/series/");
            hashMap.put("https://juststocks.in/series/", 2);
            firstPress = true;

        } else if (id == R.id.nav_groups) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/groups-2/");
            stack.push("https://juststocks.in/groups-2/");
            hashMap.put("https://juststocks.in/groups-2/", 3);
            firstPress = true;

        } else if (id == R.id.nav_chatroom) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/chatroom/");
            stack.push("https://juststocks.in/chatroom/");
            hashMap.put("https://juststocks.in/chatroom/", 4);
            firstPress = true;

        } else if (id == R.id.nav_activity) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/activity-2/");
            stack.push("https://juststocks.in/activity-2/");
            hashMap.put("https://juststocks.in/activity-2/", 5);
            firstPress = true;

        } else if (id == R.id.nav_instructors) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/members-2/");
            stack.push("https://juststocks.in/members-2/");
            hashMap.put("https://juststocks.in/members-2/", 6);
            firstPress = true;

        } else if (id == R.id.nav_courses) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/courses/");
            stack.push("https://juststocks.in/courses/");
            hashMap.put("https://juststocks.in/courses/", 7);
            firstPress = true;

        } else if (id == R.id.nav_open_demat_account) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://onlinedemataccount.tradebulls.in/DematAccount/Demat_SubBroker?SubBroker=8jdf2EQsNGDRwkL6MoEAhg%3D%3D");
            stack.push("https://onlinedemataccount.tradebulls.in/DematAccount/Demat_SubBroker?SubBroker=8jdf2EQsNGDRwkL6MoEAhg%3D%3D");
            hashMap.put("https://onlinedemataccount.tradebulls.in/DematAccount/Demat_SubBroker?SubBroker=8jdf2EQsNGDRwkL6MoEAhg%3D%3D", 8);
            firstPress = true;

        } else if (id == R.id.nav_about_us) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/about-us/");
            stack.push("https://juststocks.in/about-us/");
            hashMap.put("https://juststocks.in/about-us/", 9);
            firstPress = true;

        } else if (id == R.id.nav_contact_us) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl("https://juststocks.in/contact-us/");
            stack.push("https://juststocks.in/contact-us/");
            hashMap.put("https://juststocks.in/contact-us/", 10);
            firstPress = true;
        } else if(id == R.id.nav_share){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Just Stocks Application");
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.application.juststocks");
            startActivity(Intent.createChooser(i, "Share URL"));
        } else if(id == R.id.nav_exit){
           finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(loginStatus!=null) {
            if (loginStatus.equals("true")) {
                menu.findItem(R.id.login).setTitle("Log Out");
                menu.findItem(R.id.register).setVisible(false);
            }
            else{
                menu.findItem(R.id.login).setTitle("Log In");
                menu.findItem(R.id.register).setVisible(true);
            }

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        t.cancel();
        super.onPause();
    }
}
