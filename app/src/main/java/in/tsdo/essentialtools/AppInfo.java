package in.tsdo.essentialtools;

import android.content.pm.ApplicationInfo;

public class AppInfo {
    private ApplicationInfo info;
    private boolean checked;
    public AppInfo(ApplicationInfo info, boolean checked) {
        this.info = info;
        this.checked = checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked(){
        return this.checked;
    }

    public ApplicationInfo getInfo() {
        return this.info;
    }
}
