package in.tsdo.elw.AsyncTasks;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import in.tsdo.elw.AppAdapter;
import in.tsdo.elw.AppInfo;

public class AppLoadIconTask extends AsyncTask<Object, Void, Void> {

    private ImageView imageView;
    private AppInfo app;
    private AppAdapter.ViewHolder holder;

    @Override
    protected Void doInBackground(Object... params) {
        PackageManager pm = (PackageManager)params[0];
        app = (AppInfo)params[1];
        imageView = (ImageView)params[2];
        holder = (AppAdapter.ViewHolder)params[3];

        Drawable icon = pm.getApplicationIcon(app.getInfo());
        app.setIcon(icon);

        return null;
    }

    @Override
    protected void onPostExecute(Void arg) {
        // Icon loaded
        if (holder.IsPackage(app.getInfo().packageName))
            imageView.setImageDrawable(app.getIcon());
    }
}
