package in.tsdo.essentialtools;

import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AppPackagePacker{
    private String settingString;
    private ArrayList<AppInfo> appArrayList;
    public AppPackagePacker(String settingString) {
        this.settingString = settingString;
        this.appArrayList = new ArrayList<>();
        Log.d("SETTING", settingString);
    }

    public void add(ApplicationInfo appInfo) {
        boolean checked = false;
        if (settingString.contains(appInfo.packageName))
            checked = true;
        if (checked) {
            Log.d("CHECKED", appInfo.packageName);
        }
        appArrayList.add(new AppInfo(appInfo, checked));
    }

    public ApplicationInfo get(int index) {
        return appArrayList.get(index).getInfo();
    }

    public void setChecked(int index, boolean checked) {
        appArrayList.get(index).setChecked(checked);
    }

    public boolean isChecked(int index) {
        return appArrayList.get(index).isChecked();
    }

    public int size() {
        return appArrayList.size();
    }

    public void sort() {
        Collections.sort(appArrayList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return o1.getInfo().packageName.compareTo(o2.getInfo().packageName);
            }
        });
    }

    public String generateSettingString() {
        this.settingString = MainActivity.SYSTEM_WHITELIST;
        for (AppInfo info : appArrayList) {
            if (info.isChecked()) {
                this.settingString = this.settingString +","+info.getInfo().packageName;
            }
        }
        return this.settingString;
    }

    public void setSettingString(String settingString) {
        this.settingString = settingString;
        for (AppInfo info : appArrayList) {
            boolean checked = false;
            if (settingString.contains(info.getInfo().packageName))
                checked = true;
            info.setChecked(checked);
        }
    }
}
