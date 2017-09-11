package in.tsdo.elw;

import android.content.pm.ApplicationInfo;

public class AppInfo {
    private ApplicationInfo info;
    private boolean isSystem;
    private boolean[] checked;
    public AppInfo(ApplicationInfo info, boolean[] checked, boolean isSystem) {
        this.info = info;
        this.checked = checked;
        this.isSystem = isSystem;
    }

    public void setChecked(int type, boolean checked) {
        this.checked[type] = checked;
    }

    public boolean isChecked(int type){
        return this.checked[type];
    }

    public boolean isSystem(){
        return isSystem;
    }

    public ApplicationInfo getInfo() {
        return this.info;
    }
}
