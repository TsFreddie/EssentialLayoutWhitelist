package in.tsdo.elw;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.os.AsyncTaskCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import in.tsdo.elw.AsyncTasks.AppLoadIconTask;
import in.tsdo.elw.AsyncTasks.AppWaitIconTask;

public class AppAdapter extends BaseAdapter {
    AppPackagePacker packagePacker;
    ViewHolder holder;
    Activity context;
    int type;

    public AppAdapter(Activity context, AppPackagePacker packagePacker, int type){
        super();
        this.context = context;
        this.packagePacker = packagePacker;
        this.type = type;
    }

    @Override
    public int getCount() {
        if (packagePacker == null) return 0;
        return packagePacker.size();
    }

    @Override
    public Object getItem(int position) {
        if (packagePacker == null) return null;
        return packagePacker.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (packagePacker == null) return 0;
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.app_item, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView)convertView.findViewById(R.id.appNameText);
            holder.pkg = (TextView)convertView.findViewById(R.id.packageNameText);
            holder.icon = (ImageView)convertView.findViewById(R.id.iconImage);
            holder.check = (CheckBox)convertView.findViewById(R.id.appCheckBox);
            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Integer tag = (Integer) buttonView.getTag();
                    if (tag == null) return;
                    int position = tag;
                    packagePacker.setChecked(type, position, isChecked);
                }
            });

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        AppInfo appInfo = packagePacker.get(position);

        holder.pkgStr = appInfo.getInfo().packageName;
        holder.pkg.setText(appInfo.getInfo().packageName);
        holder.check.setTag(position);
        holder.check.setChecked(packagePacker.isChecked(type, position));
        holder.label.setText(appInfo.getAppName());

        if (!appInfo.hasTask()) {
            holder.icon.setImageDrawable(null);

            PackageManager pm = context.getPackageManager();
            AppLoadIconTask task = new AppLoadIconTask();
            appInfo.setTask(task);
            AsyncTaskCompat.executeParallel(task, pm, appInfo, holder.icon, holder);
        }
        else {
            if (appInfo.getIcon() == null) {
                // TODO: fix busy waiting.
                AppWaitIconTask task = new AppWaitIconTask();
                AsyncTaskCompat.executeParallel(task, appInfo, holder.icon, holder);
                holder.icon.setImageDrawable(null);
            }
            else {
                holder.icon.setImageDrawable(appInfo.getIcon());
            }
        }
        return convertView;
    }

    public AppPackagePacker getPacker() {
        return packagePacker;
    }

    public void setPacker(AppPackagePacker packagePacker) {
        this.packagePacker = packagePacker;
    }

    public class ViewHolder {
        TextView label;
        TextView pkg;
        ImageView icon;
        CheckBox check;
        String pkgStr;

        public boolean IsPackage(String pkg) {
            if (pkgStr == null) return false;
            return pkgStr.equals(pkg);
        }
    }
}
