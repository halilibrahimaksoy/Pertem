package com.haksoy.pertem.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.haksoy.pertem.R;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.fragment.ContentDetail;
import com.haksoy.pertem.fragment.AnnounceFragment;
import com.haksoy.pertem.fragment.AnswerFragment;
import com.haksoy.pertem.fragment.ExplanationFragment;
import com.haksoy.pertem.fragment.MeasurementFragment;
import com.haksoy.pertem.fragment.MostQuestionFragment;
import com.haksoy.pertem.fragment.ProcurementFragment;
import com.haksoy.pertem.tools.Constant;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.GifImageView;
import com.haksoy.pertem.tools.INotifyAction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, INotifyAction {
    @BindView(R.id.txtMSB)
    LinearLayout txtMSB;
    @BindView(R.id.txtKKK)
    LinearLayout txtKKK;
    @BindView(R.id.txtHVKK)
    LinearLayout txtHVKK;
    @BindView(R.id.txtDKK)
    LinearLayout txtDKK;
    @BindView(R.id.txtMSU)
    LinearLayout txtMSU;
    @BindView(R.id.askerolAdd)
    GifImageView askerolAdd;


    INotifyAction mNotifyAction;
    private MenuItem searchItem;

    private boolean searchItemVisibility = true;
    private Unbinder unbinder;
    private String TAG = "MainActivity";
    private Fragment currentFragment;
    private AdView mBannerAd;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setMenuFooterView();

        mBannerAd = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        FirebaseClient.getAddStatusForOther(new INotifyAction() {
            @Override
            public void onNotified(Object key, Object value) {
                if ((boolean) value) {
                    askerolAdd.setVisibility(View.VISIBLE);
                    askerolAdd.setGifImageResource(R.drawable.askerol);
                    askerolAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.askerol_url)));
                            startActivity(browserIntent);
                        }
                    });
                } else
                    mBannerAd.loadAd(adRequest);
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        setCurrentFragment(Enums.Announce);
    }

    private void setMenuFooterView() {
        txtMSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(Constant.url_msb);
            }
        });
        txtKKK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(Constant.url_kkk);
            }
        });
        txtHVKK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(Constant.url_hvkk);
            }
        });
        txtDKK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(Constant.url_dkk);
            }
        });
        txtMSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(Constant.url_msu);
            }
        });
    }

    private void openWebPage(String url) {

        showInterstitialAd();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder().setToolbarColor(getResources().getColor(R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (controlAnswerFragment()) {
        } else {
            super.onBackPressed();
        }
        setMenuVisibilityForActiveFragment();
    }

    private boolean controlAnswerFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            for (Fragment fragment1 : fragment.getChildFragmentManager().getFragments()) {
                if (fragment1 instanceof AnswerFragment) {
                    fragment.getChildFragmentManager().popBackStack();
                    return true;
                }
                for (Fragment fragment2 : fragment1.getChildFragmentManager().getFragments()) {
                    if (fragment2 instanceof AnswerFragment) {
                        fragment1.getChildFragmentManager().popBackStack();
                        return true;
                    }
                }

            }

        }
        return false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(searchItemVisibility);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNotifyAction.onNotified(Enums.Search, newText);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_teminler) {
            setCurrentFragment(Enums.Procurement);
        } else if (id == R.id.nav_duyurular) {
            setCurrentFragment(Enums.Announce);
        } else if (id == R.id.nav_most_question) {
            setCurrentFragment(Enums.MostQuestions);
        } else if (id == R.id.nav_measurement) {
            setCurrentFragment(Enums.Measurement);
        } else if (id == R.id.nav_explanation) {
            setCurrentFragment(Enums.Explanation);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    private void setCurrentFragment(Enums target) {
        currentFragment = null;

        switch (target) {
            case Procurement: {
                currentFragment = new ProcurementFragment(this);
            }
            break;
            case Announce: {
                currentFragment = new AnnounceFragment(this);
            }
            break;
            case MostQuestions: {
                currentFragment = new MostQuestionFragment(this);
            }
            break;
            case Measurement: {
                currentFragment = new MeasurementFragment();
            }
            break;
            case Explanation: {
                currentFragment = new ExplanationFragment();
            }
            break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frmMainContainer, currentFragment, target.name()).commit();
        setTitle(target);
        setMenuVisibilityForActiveFragment();

        showInterstitialAd();
    }


    private void setMenuVisibilityForActiveFragment() {
        if (getSupportFragmentManager().getFragments().size() > 0)
            if (currentFragment instanceof AnnounceFragment)
                setMenuVisibility(true);
            else if (currentFragment instanceof ProcurementFragment)
                setMenuVisibility(true);
            else if (currentFragment instanceof MostQuestionFragment)
                setMenuVisibility(true);
            else
                setMenuVisibility(false);
    }

    private void setMenuVisibility(boolean visibility) {
        if (searchItem != null)
            searchItem.setVisible(visibility);

        searchItemVisibility = visibility;
    }

    private void setTitle(Enums target) {
        switch (target) {
            case Procurement: {
                getSupportActionBar().setTitle("Güncel Teminler");
            }
            break;
            case Announce: {
                getSupportActionBar().setTitle("Güncel Duyurular");
            }
            break;
            case MostQuestions: {
                getSupportActionBar().setTitle("Sıkça Sorulan Sorular");
            }
            break;
            case Measurement: {
                getSupportActionBar().setTitle("Boy Kilo Nomogramı");
            }
            break;
            case Explanation: {
                getSupportActionBar().setTitle("Personel Temini");
            }
            break;
        }
    }

    @Override
    public void onNotified(Object key, Object value) {
        if (key == Enums.AnnounceDetail || key == Enums.ProcurementDetail) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frmMainContainer, new ContentDetail((String) value), key.toString()).addToBackStack("detail").commit();
            setMenuVisibility(false);
            showInterstitialAd();
        } else if (key == Enums.SetNotifyAction) {
            this.mNotifyAction = (INotifyAction) value;
        }
    }

    public void showInterstitialAd() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            mInterstitialAd.loadAd(adRequest);
        }
    }


}
