package com.elishanto.schoololoconnect.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.elishanto.schoololoconnect.R;
import com.elishanto.schoololoconnect.adapter.Homework;
import com.elishanto.schoololoconnect.adapter.Subject;
import com.elishanto.schoololoconnect.fragment.HomeworkFragment;
import com.elishanto.schoololoconnect.fragment.MarksFragment;
import com.elishanto.schoololoconnect.fragment.SubjectFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    static String login;
    static String password;
    static String email;
    SharedPreferences preferences;
    static List<Subject> subjects;
    static List<Homework> homeworks;
    static Drawer result;
    static FragmentManager fragmentManager;
    static InterstitialAd interstitialAd;
    static int counter;
    static boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        first = true;
        counter = 0;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        login = preferences.getString("login", "");
        password = preferences.getString("password", "");
        subjects = new ArrayList<>();
        homeworks = new ArrayList<>();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        fragmentManager = getSupportFragmentManager();


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(
                        new AccountHeaderBuilder()
                                .withSelectionListEnabledForSingleProfile(false)
                                .withActivity(this)
                                .withHeaderBackground(R.drawable.account_bg)
                                .addProfiles(new ProfileDrawerItem().withName(login).withEmail(email))
                                .build()
                )
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Оценки")
                                .withIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_assignment_grey_500_24dp))
                                .withSelectedIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_assignment_blue_500_24dp))
                )
                .addStickyDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Настройки")
                                .withSelectable(false)
                )
                .build();
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-8050884723094020/2084358009");

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                if (!(fragmentManager.findFragmentById(R.id.container) instanceof HomeworkFragment))
                    replace(HomeworkFragment.newInstance());
            }
        });
        requestNewInterstitial();

        result.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (position) {
                    case 1:
                        if (!(fragmentManager.findFragmentById(R.id.container) instanceof MarksFragment))
                            replace(MarksFragment.newInstance());
                        break;
                    case 2:
                        if (!(fragmentManager.findFragmentById(R.id.container) instanceof HomeworkFragment))
                            replace(HomeworkFragment.newInstance());
                        break;
                    case -1:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                }
                result.closeDrawer();
                return true;
            }
        });

        replace(MarksFragment.newInstance());
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    public static void replace(Fragment fragment) {
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.replace(R.id.container, fragment);
        tx.commitAllowingStateLoss();
    }

    public static boolean isFirst() {
        return first;
    }

    public static void setFirst(boolean first) {
        MainActivity.first = first;
    }

    public static void incCounter() {
        counter++;
    }

    public static int getCounter() {
        return counter;
    }

    public static InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public static Drawer getDrawer() {
        return result;
    }

    public static String getLogin() {
        return login;
    }

    public static String getPassword() {
        return password;
    }

    public static List<Subject> getSubjects() {
        return subjects;
    }

    public static List<Homework> getHomeworks() {
        return homeworks;
    }

    public static void setEmail(String eemail) {
        email = eemail;
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.findFragmentById(R.id.container) instanceof SubjectFragment)
            replace(MarksFragment.newInstance());
    }
}
