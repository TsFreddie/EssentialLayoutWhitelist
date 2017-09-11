package in.tsdo.essentialtools;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

public class AppPickerActivity extends AppCompatActivity {

    private ListView app_list;
    private AppAdapter app_adapter;
    Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_picker);
        app_list = (ListView)findViewById(R.id.app_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAppList();
        if(state != null) {
            app_list.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onPause() {
        state = app_list.onSaveInstanceState();
        writeSettingString();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_app_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_restore:
                restoreToDefault();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void updateAppList() {
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        AppPackagePacker appPacker = new AppPackagePacker(getSettingString());
        for (ApplicationInfo appInfo : packages)
        {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                // TODO: Add system app list
            }
            else if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                // TODO: Add system app list
            }
            else {
                appPacker.add(appInfo);
            }
        }
        appPacker.sort();
        app_adapter = new AppAdapter(this, appPacker, pm);
        app_list.setAdapter(app_adapter);
    }

    protected String getSettingString() {
        String settingString = Settings.Global.getString(getContentResolver(), "ESSENTIAL_LAYOUT_WHITELIST");
        if (settingString == null) {
            settingString = MainActivity.DEFAULT_WHITELIST;
        }
        return settingString;
    }

    protected void writeSettingString() {
        Settings.Global.putString(getContentResolver(), "ESSENTIAL_LAYOUT_WHITELIST", app_adapter.getPacker().generateSettingString());
    }

    protected void restoreToDefault() {
        Settings.Global.putString(getContentResolver(), "ESSENTIAL_LAYOUT_WHITELIST", MainActivity.DEFAULT_WHITELIST);
        app_adapter.setSettingString(MainActivity.DEFAULT_WHITELIST);
    }
}
