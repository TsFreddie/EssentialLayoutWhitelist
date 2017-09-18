package in.tsdo.elw.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import in.tsdo.elw.AsyncTasks.AppPackageLoader;

public class PackageIntentReceiver extends BroadcastReceiver {
    final AppPackageLoader loader;

    public PackageIntentReceiver(AppPackageLoader loader) {
        // TODO: use this.
        this.loader = loader;
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        loader.getContext().registerReceiver(this, filter);
    }

    @Override public void onReceive(Context context, Intent intent) {
        loader.onContentChanged();
    }
}
