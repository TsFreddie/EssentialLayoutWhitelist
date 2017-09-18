package in.tsdo.elw.AsyncTasks;

import android.os.AsyncTask;
import android.widget.ImageView;

import in.tsdo.elw.AppAdapter;
import in.tsdo.elw.AppInfo;

public class AppWaitIconTask extends AsyncTask<Object, Void, Void> {

    private ImageView imageView;
    private AppInfo app;
    private AppAdapter.ViewHolder holder;

    @Override
    protected Void doInBackground(Object... params) {
        // TODO: fix this
        app = (AppInfo)params[0];
        imageView = (ImageView)params[1];
        holder = (AppAdapter.ViewHolder)params[2];

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
    protected void onPostExecute(Void arg) {
        if (holder.IsPackage(app.getInfo().packageName))
            imageView.setImageDrawable(app.getIcon());
    }
}
