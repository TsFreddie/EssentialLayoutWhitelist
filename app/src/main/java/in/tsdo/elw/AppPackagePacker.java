package in.tsdo.elw;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class AppPackagePacker {
    final static int FILL = 0;
    final static int HIDE = 1;

    private String fillString;
    private String hideString;
    private HashMap<String, AppInfo> appHashMap;
    private HashMap<String, AppInfo> newAppHashMap;
    private ArrayList<AppInfo> filteredAppList;
    private PackageManager pm;

    private boolean showSystem;

    public AppPackagePacker(String fillString, String hideString, PackageManager pm, boolean showSystem) {
        this.fillString = fillString;
        this.hideString = hideString;
        this.appHashMap = null;
        this.newAppHashMap = null;
        this.filteredAppList = new ArrayList<>();
        this.showSystem = showSystem;
        this.pm = pm;
    }

    public void add(ApplicationInfo appInfo, boolean isSystem) {
        String key = appInfo.packageName;
        if (appHashMap != null && appHashMap.containsKey(key)) {
            newAppHashMap.put(key, appHashMap.get(key));
        }
        else {
            boolean[] checked = {false, false};
            if (checkString(fillString, appInfo.packageName))
                checked[FILL] = true;
            if (checkString(hideString, appInfo.packageName))
                checked[HIDE] = true;

            AppInfo app = new AppInfo(appInfo, checked, isSystem);
            newAppHashMap.put(key, app);
        }
    }

    public void reset() {
        newAppHashMap = new HashMap<>();
    }

    public void apply() {
        appHashMap = newAppHashMap;
        setFilteredAppList();
    }

    public boolean isApplied() {
        return appHashMap != null;
    }

    public AppInfo get(int index) {
        return filteredAppList.get(index);
    }

    public void setChecked(int type, int index, boolean checked) {
        filteredAppList.get(index).setChecked(type, checked);
    }

    public boolean isChecked(int type, int index) {
        return filteredAppList.get(index).isChecked(type);
    }

    public boolean isSystem(int index) {
        return filteredAppList.get(index).isSystem();
    }

    public int size() {
        return filteredAppList.size();
    }

    public void sort() {
        Collections.sort(filteredAppList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return o1.getInfo().packageName.compareTo(o2.getInfo().packageName);
            }
        });
    }

    public String getFillString() {
        this.fillString = "";
        Iterator<String> iter = appHashMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            AppInfo info = appHashMap.get(key);
            if (info.isChecked(FILL)) {
                this.fillString += info.getInfo().packageName+",";
            }
        }
        return this.fillString;
    }

    public String getHideString() {
        this.hideString = "immersive.navigation=";
        Iterator<String> iter = appHashMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            AppInfo info = appHashMap.get(key);
            if (info.isChecked(HIDE)) {
                this.hideString += info.getInfo().packageName+",";
            }
        }
        return this.hideString;
    }

    public void setFillString(String fillString) {
        this.fillString = fillString;
        Iterator<String> iter = appHashMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            AppInfo info = appHashMap.get(key);
            boolean checked = false;
            if (checkString(this.fillString, info.getInfo().packageName))
                checked = true;
            info.setChecked(FILL, checked);
        }
    }

    public void setCheckedAll(int type, boolean checked) {
        for (AppInfo info : filteredAppList) {
            info.setChecked(type, checked);
        }
    }

    public void invertSelection(int type) {
        for (AppInfo info : filteredAppList) {
            info.setChecked(type, !info.isChecked(type));
        }
    }

    public void setShowSystem(boolean showSystem) {
        this.showSystem = showSystem;
        setFilteredAppList();
    }

    private void setFilteredAppList() {
        int count = appHashMap.size();
        filteredAppList = new ArrayList<>(count);

        for (String key : appHashMap.keySet()) {
            AppInfo app = appHashMap.get(key);
            if (showSystem || !app.isSystem())
                filteredAppList.add(app);
        }
        sort();
    }


    public void filter(String filter) {
        if (filter == null) {
            setFilteredAppList();
            return;
        }
        filter = filter.toLowerCase();
        int count = appHashMap.size();
        filteredAppList = new ArrayList<>(count);

        for (String key : appHashMap.keySet()) {
            AppInfo app = appHashMap.get(key);
            if ((showSystem || !app.isSystem()) &&
                    (((app.getAppName() != null) && app.getAppName().toLowerCase().contains(filter))
                        || app.getInfo().packageName.toLowerCase().contains(filter)))
                filteredAppList.add(app);
        }
        sort();
    }

    private boolean checkString(String str, String pkg){
        // TODO: regex

        String matchStringStart = String.format("%s,", pkg);
        String matchString1 = String.format(",%s,", pkg);
        String matchString2 = String.format("=%s,", pkg);

        return (str.startsWith(matchStringStart) || str.contains(matchString1) || str.contains(matchString2));
    }
}
