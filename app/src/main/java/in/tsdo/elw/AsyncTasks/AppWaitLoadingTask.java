package in.tsdo.elw.AsyncTasks;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import in.tsdo.elw.AppAdapter;
import in.tsdo.elw.AppInfo;

public class AppWaitLoadingTask extends AsyncTask<Object, Void, Void> {

    TextView textView;
    ImageView imageView;
    AppInfo app;
    AppAdapter.ViewHolder holder;

    @Override
    protected Void doInBackground(Object... params) {
        // TODO: fix this
        app = (AppInfo)params[0];
        textView = (TextView)params[1];
        imageView = (ImageView)params[2];
        holder = (AppAdapter.ViewHolder)params[3];

        while(app.getAppName() == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        publishProgress();
        while(app.getAppName() == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... arg) {
        if (holder.IsPackage(app.getInfo().packageName))
            textView.setText(app.getAppName());
    }

    @Override
    protected void onPostExecute(Void arg) {
        if (holder.IsPackage(app.getInfo().packageName))
            imageView.setImageDrawable(app.getIcon());
    }
}
