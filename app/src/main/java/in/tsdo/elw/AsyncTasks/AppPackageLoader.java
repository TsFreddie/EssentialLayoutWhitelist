package in.tsdo.elw.AsyncTasks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import in.tsdo.elw.AppPackagePacker;

public class AppPackageLoader extends AsyncTaskLoader<AppPackagePacker> {
    final PackageManager pm;
    private AppPackagePacker packer;


    public AppPackageLoader(Context context, AppPackagePacker packer) {
        super(context);
        this.pm = context.getPackageManager();
        this.packer = packer;
    }

    @Override
    public AppPackagePacker loadInBackground() {
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        packer.reset();
        for (ApplicationInfo appInfo : packages) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ||
                    (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                packer.add(appInfo, true);
            } else {
                packer.add(appInfo, false);
            }
        }

        return packer;
    }

    @Override
    public void deliverResult(AppPackagePacker packer) {
        if (isReset()) {
            return;
        }
        super.deliverResult(packer);
    }
}
