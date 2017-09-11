package in.tsdo.elw;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class AppPickerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppPackagePacker appPacker;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean showSystem;
    MenuItem showSystemMenu;
    Parcelable state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_picker);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("ESSENTIAL_TOOLS_PREF", MODE_PRIVATE);
        showSystem = pref.getBoolean("SHOW_SYSTEM", false);
        createPacker();
        if (state != null) {
            viewPager.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onPause() {
        writeSettingString();
        state = viewPager.onSaveInstanceState();
        SharedPreferences.Editor edit = getSharedPreferences("ESSENTIAL_TOOLS_PREF", MODE_PRIVATE).edit();
        edit.putBoolean("SHOW_SYSTEM", showSystem);
        edit.apply();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_app_picker, menu);
        showSystemMenu = menu.findItem(R.id.menu_show_system);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentPage = viewPager.getCurrentItem();
        switch (item.getItemId()) {
            case R.id.menu_restore:
                if (showSystem) {
                    appPacker.setFillString(MainActivity.DEFAULT_WHITELIST);
                } else {
                    appPacker.setFillString(MainActivity.DEFAULT_WHITELIST);
                    appPacker.setSystemFillString(MainActivity.DEFAULT_SYSTEM_WHITELIST);
                }
                appPacker.setCheckedAll(AppPackagePacker.HIDE, false);
                appPacker.setSystemHideString("");
                ((ViewPagerAdapter)viewPager.getAdapter()).notifyDataSetChanged(currentPage);
                return true;
            case R.id.menu_deselect_all:

                appPacker.setCheckedAll(currentPage, false);
                ((ViewPagerAdapter)viewPager.getAdapter()).notifyDataSetChanged(currentPage);
                return true;
            case R.id.menu_select_all:
                appPacker.setCheckedAll(currentPage, true);
                ((ViewPagerAdapter)viewPager.getAdapter()).notifyDataSetChanged(currentPage);
                return true;
            case R.id.menu_invert:
                appPacker.invertSelection(currentPage);
                ((ViewPagerAdapter)viewPager.getAdapter()).notifyDataSetChanged(currentPage);
                return true;
            case R.id.menu_show_system:
                writeSettingString();
                showSystem = !showSystem;
                createPacker();
                ((ViewPagerAdapter)viewPager.getAdapter()).notifyNewPacker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (showSystem) {
            showSystemMenu.setTitle(R.string.menu_hide_system);
        }
        else {
            showSystemMenu.setTitle(R.string.menu_show_system);
        }
        return true;
    }

    protected void createPacker() {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        String fillString = getFillString();
        String hideString = getHideString();
        appPacker = new AppPackagePacker(getFillString(), getHideString());
        String systemFillString = "";
        String systemHideString = "";
        for (ApplicationInfo appInfo : packages)
        {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ||
                    (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                if (showSystem) {
                    appPacker.add(appInfo, true);
                }
                else{
                    if (fillString.contains(appInfo.packageName+",")) {
                        systemFillString += appInfo.packageName+",";
                    }
                    if (hideString.contains(appInfo.packageName+",")) {
                        systemHideString += appInfo.packageName+",";
                    }
                }
            }
            else {
                appPacker.add(appInfo, false);
            }
        }
        appPacker.setSystemFillString(systemFillString);
        appPacker.setSystemHideString(systemHideString);
        appPacker.sort();
    }

    protected String getFillString() {
        String settingString = Settings.Global.getString(getContentResolver(), "ESSENTIAL_LAYOUT_WHITELIST");
        if (settingString == null) {
            settingString = MainActivity.DEFAULT_WHITELIST;
        }
        if (!settingString.endsWith(",")) {
            settingString += ",";
        }
        return settingString;
    }

    protected String getHideString() {
        String settingString = Settings.Global.getString(getContentResolver(), "policy_control");
        if (settingString == null) {
            settingString = "immersive.navigation=";
        }
        if (!settingString.endsWith(",")) {
            settingString += ",";
        }
        return settingString;
    }

    protected AppPackagePacker getAppPacker() {
        return appPacker;
    }

    protected void writeSettingString() {
        Settings.Global.putString(getContentResolver(), "ESSENTIAL_LAYOUT_WHITELIST", appPacker.getFillString());
        Settings.Global.putString(getContentResolver(), "policy_control", appPacker.getHideString());
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add fragment here
        adapter.addFragment(newFragment(AppPackagePacker.FILL), getString(R.string.tab_fill_bar));
        adapter.addFragment(newFragment(AppPackagePacker.HIDE), getString(R.string.tab_hide_nav));
        viewPager.setAdapter(adapter);
    }

    private ListViewFragment newFragment(int type) {
        Bundle hideArg = new Bundle();
        hideArg.putInt("FRAGMENT_TYPE", type);
        ListViewFragment fragment = new ListViewFragment();
        fragment.setArguments(hideArg);
        return fragment;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<ListViewFragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(ListViewFragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void notifyDataSetChanged(int position) {
            for (ListViewFragment fragment : mFragmentList) {
                fragment.notifyDataSetChanged();
            }
        }

        public void notifyNewPacker() {
            for (ListViewFragment fragment : mFragmentList) {
                fragment.notifyNewPacker();
            }
        }
    }
}
