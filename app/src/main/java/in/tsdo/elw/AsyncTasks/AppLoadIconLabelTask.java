package in.tsdo.elw.AsyncTasks;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Callable;

import in.tsdo.elw.AppAdapter;
import in.tsdo.elw.AppInfo;

public class AppLoadIconLabelTask extends AsyncTask<Object, Void, Void> {

    TextView textView;
    ImageView imageView;
    AppInfo app;
    AppAdapter.ViewHolder holder;

    @Override
    protected Void doInBackground(Object... params) {
        PackageManager pm = (PackageManager)params[0];
        app = (AppInfo)params[1];
        textView = (TextView)params[2];
        imageView = (ImageView)params[3];
        holder = (AppAdapter.ViewHolder)params[4];

        String appName = pm.getApplicationLabel(app.getInfo()).toString();
        app.setAppInfo(appName);
        publishProgress();
        Drawable icon = pm.getApplicationIcon(app.getInfo());
        app.setIcon(icon);

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... arg) {
        // Label loaded
        if (holder.IsPackage(app.getInfo().packageName))
            textView.setText(app.getAppName());
    }
    @Override
    protected void onPostExecute(Void arg) {
        // Icon loaded
        if (holder.IsPackage(app.getInfo().packageName))
            imageView.setImageDrawable(app.getIcon());
    }
}
