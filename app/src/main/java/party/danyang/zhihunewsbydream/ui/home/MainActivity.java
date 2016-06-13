package party.danyang.zhihunewsbydream.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Switch;

import com.code19.library.SPUtils;
import com.gc.materialdesign.widgets.ColorSelector;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.BuildConfig;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.gc.materialdesign.widgets.ColorSelector.OnColorSelectedListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import party.danyang.zhihunewsbydream.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String PREF_COLOR_PRIMARY = "PREF_COLOR_PRIMARY";
    public static final String PREF_COLOR_NAV_HEADER = "PREF_COLOR_NAV_HEADER";
    public static final String PREF_NO_PIC = "PREF_NO_PIC";

    @Bind(R.id.toolbar_main)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout_main)
    DrawerLayout drawer;
    @Bind(R.id.nav_view_main)
    NavigationView navigationView;
    @Bind(R.id.smart_tabs_main)
    SmartTabLayout tabLayout;
    @Bind(R.id.view_pager_main)
    ViewPager viewPager;

    private Switch noPicSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setupToolbar();
        setupDrawer();
        setupTab();
        setupTheme();
    }

    private void setupTheme() {
        Integer color = (Integer) SPUtils.getSp(this, PREF_COLOR_PRIMARY, Integer.valueOf(getResources().getColor(R.color.colorPrimary)));
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        if (Build.VERSION.SDK_INT >= 19) {
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }
        toolbar.setBackgroundColor(color);
        tabLayout.setBackgroundColor(color);
        Integer nav_color = (Integer) SPUtils.getSp(this, PREF_COLOR_NAV_HEADER, Integer.valueOf(getResources().getColor(R.color.colorPrimary)));
        navigationView.getHeaderView(0).setBackgroundColor(nav_color);
    }

    private void setupTab() {
        FragmentPagerItems pagerItems = FragmentPagerItems.with(this)
                .add(R.string.news, NewsFragment.class)
                .add(R.string.hot, HotFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pagerItems);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCustomThemeColor(PREF_COLOR_NAV_HEADER);
            }
        });
        Menu menu = navigationView.getMenu();
        noPicSwitch = (Switch) MenuItemCompat.getActionView(menu.findItem(R.id.nav_no_pic));
        noPicSwitch.setChecked((Boolean) SPUtils.getSp(this, PREF_NO_PIC, Boolean.FALSE));
        noPicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNoPicModel();
            }
        });


    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_theme:
                chooseCustomThemeColor(PREF_COLOR_PRIMARY);
                break;
            case R.id.nav_no_pic:
                setNoPicModel();
                break;
            case R.id.nav_about:
                showAboutDialog();
                break;
            default:
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void chooseCustomThemeColor(final String key) {
        final ColorSelector colorSelector = new ColorSelector(this
                , (Integer) SPUtils.getSp(this, key, getResources().getColor(R.color.colorPrimary))
                , new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                SPUtils.setSP(MainActivity.this, null, key, color);
                setupTheme();
            }
        });
        colorSelector.setCanceledOnTouchOutside(true);
        colorSelector.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void showAboutDialog() {

        final MaterialDialog materialDialog = new MaterialDialog(this);

        Element LibElement = new Element();
        LibElement.setTitle(getString(R.string.libs));
        LibElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog(MainActivity.this)
                        .setTitle(R.string.libs)
                        .setMessage(R.string.libs_content)
                        .setCanceledOnTouchOutside(true).show();
            }
        });

        Element thinksElement = new Element();
        thinksElement.setTitle(getString(R.string.thinks));
        thinksElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog(MainActivity.this)
                        .setTitle(R.string.thinks)
                        .setMessage(R.string.thinks_content)
                        .setCanceledOnTouchOutside(true).show();
            }
        });


        View aboutPage = new AboutPage(this)
//                .setImage(R.drawable.ic_launcher)
                .setDescription(getString(R.string.app_name) + "\n" + getString(R.string.version) + "\n" + getString(R.string.description))
                .addEmail(getString(R.string.email))
                .addTwitter(getString(R.string.twitter))
                .addWebsite(getString(R.string.website))
                .addGitHub(getString(R.string.github))
                .addGroup(getString(R.string.others))
                .addItem(LibElement)
                .addItem(thinksElement)
                .create();
        materialDialog.setView(aboutPage).setCanceledOnTouchOutside(true).show();
    }

    private void setNoPicModel() {
        boolean checked = (Boolean) SPUtils.getSp(this, PREF_NO_PIC, Boolean.FALSE);
        SPUtils.setSP(this, null, PREF_NO_PIC, !checked);
        noPicSwitch.setChecked(!checked);
    }
}
