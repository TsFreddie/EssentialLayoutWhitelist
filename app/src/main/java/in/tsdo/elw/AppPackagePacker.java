package in.tsdo.elw;

import android.content.pm.ApplicationInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AppPackagePacker {
    final static int FILL = 0;
    final static int HIDE = 1;

    private String fillString;
    private String systemFillString;
    private String hideString;
    private String systemHideString;
    private ArrayList<AppInfo> appArrayList;
    public AppPackagePacker(String fillString, String hideString) {
        this.fillString = fillString;
        this.hideString = hideString;
        this.appArrayList = new ArrayList<>();
    }

    public void add(ApplicationInfo appInfo, boolean isSystem) {
        boolean[] checked = {false, false};
        if (fillString.contains(appInfo.packageName+","))
            checked[FILL] = true;
        if (hideString.contains(appInfo.packageName+","))
            checked[HIDE] = true;

        appArrayList.add(new AppInfo(appInfo, checked, isSystem));
    }

    public ApplicationInfo get(int index) {
        return appArrayList.get(index).getInfo();
    }

    public void setChecked(int type, int index, boolean checked) {
        appArrayList.get(index).setChecked(type, checked);
    }

    public boolean isChecked(int type, int index) {
        return appArrayList.get(index).isChecked(type);
    }

    public boolean isSystem(int index) {
        return appArrayList.get(index).isSystem();
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

    public String getFillString() {
        this.fillString = systemFillString;
        for (AppInfo info : appArrayList) {
            if (info.isChecked(FILL)) {
                this.fillString += info.getInfo().packageName+",";
            }
        }
        return this.fillString;
    }

    public String getHideString() {
        this.hideString = "immersive.navigation="+systemHideString;
        for (AppInfo info : appArrayList) {
            if (info.isChecked(HIDE)) {
                this.hideString += info.getInfo().packageName+",";
            }
        }
        return this.hideString;
    }

    public void setFillString(String fillString) {
        this.fillString = fillString;
        for (AppInfo info : appArrayList) {
            boolean checked = false;
            if (fillString.contains(info.getInfo().packageName+","))
                checked = true;
            info.setChecked(FILL, checked);
        }
    }

    public void setHideString(String hideString) {
        this.hideString = hideString;
        for (AppInfo info : appArrayList) {
            boolean checked = false;
            if (hideString.contains(info.getInfo().packageName+","))
                checked = true;
            info.setChecked(HIDE, checked);
        }
    }

    public void setCheckedAll(int type, boolean checked) {
        for (AppInfo info : appArrayList) {
            info.setChecked(type, checked);
        }
    }

    public void invertSelection(int type) {
        for (AppInfo info : appArrayList) {
            info.setChecked(type, !info.isChecked(type));
        }
    }

    public void setSystemFillString(String systemFillString) {
        this.systemFillString = systemFillString;
    }

    public void setSystemHideString(String systemHideString){
        this.systemHideString = systemHideString;
    }
}
