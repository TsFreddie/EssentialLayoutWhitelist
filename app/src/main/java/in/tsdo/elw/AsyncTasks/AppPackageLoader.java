package in.tsdo.elw.AsyncTasks;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;

import java.util.HashMap;
import java.util.List;

import in.tsdo.elw.AppInfo;
import in.tsdo.elw.AppPackagePacker;
import in.tsdo.elw.BroadcastReceivers.PackageIntentReceiver;

public class AppPackageLoader extends AsyncTaskLoader<HashMap<String, AppInfo>> {

    public static class InterestingConfigChanges {
        final Configuration mLastConfiguration = new Configuration();
        int mLastDensity;

        boolean applyNewConfig(Resources res) {
            int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
            boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
            if (densityChanged || (configChanges&(ActivityInfo.CONFIG_LOCALE
                    |ActivityInfo.CONFIG_UI_MODE|ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
                mLastDensity = res.getDisplayMetrics().densityDpi;
                return true;
            }
            return false;
        }
    }

    final InterestingConfigChanges lastConfig = new InterestingConfigChanges();
    private final PackageManager pm;
    private final AppPackagePacker packer;
    private HashMap<String, AppInfo> appHashMap;
    private PackageIntentReceiver packageObserver;


    public AppPackageLoader(Context context, AppPackagePacker packer) {
        super(context);
        this.packer = packer;
        this.pm = context.getPackageManager();
    }

    @Override
    public HashMap<String, AppInfo> loadInBackground() {
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        appHashMap = new HashMap<>();
        for (ApplicationInfo appInfo : packages) {
            boolean isSystem = false;
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ||
                    (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                isSystem = true;
            }

            String key = appInfo.packageName;
            if (packer.contains(key)) {
                appHashMap.put(key, packer.get(key));
            }
            else {
                boolean[] checked = {false, false};
                if (AppPackagePacker.checkString(packer.getFillString(), appInfo.packageName))
                    checked[AppPackagePacker.FILL] = true;
                if (AppPackagePacker.checkString(packer.getHideString(), appInfo.packageName))
                    checked[AppPackagePacker.HIDE] = true;

                AppInfo app = new AppInfo(appInfo, checked, isSystem);
                app.setAppName(pm.getApplicationLabel(appInfo).toString());
                appHashMap.put(key, app);
            }
        }

        return appHashMap;
    }

    @Override
    protected void onStartLoading() {
        if (packageObserver == null) {
            packageObserver = new PackageIntentReceiver(this);
        }

        boolean configChange = lastConfig.applyNewConfig(getContext().getResources());

        if (appHashMap == null || takeContentChanged() || !packer.isApplied() || configChange) {
            forceLoad();
        } else if (appHashMap != null) {
            deliverResult(appHashMap);
        }
    }

    @Override
    public void deliverResult(HashMap<String, AppInfo> appMap) {
        if (isReset()) {
            return;
        }
        super.deliverResult(appMap);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (packageObserver != null) {
            getContext().unregisterReceiver(packageObserver);
            packageObserver = null;
        }
    }

}