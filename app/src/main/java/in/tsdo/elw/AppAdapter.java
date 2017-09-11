package in.tsdo.elw;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends BaseAdapter {
    AppPackagePacker packagePacker;
    Activity context;
    PackageManager packageManager;
    int type;

    public AppAdapter(Activity context, AppPackagePacker packagePacker, PackageManager packageManager, int type){
        super();
        this.context = context;
        this.packagePacker = packagePacker;
        this.packageManager = packageManager;
        this.type = type;
    }

    @Override
    public int getCount() {
        return packagePacker.size();
    }

    @Override
    public Object getItem(int position) {
        return packagePacker.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.app_item, parent, false);
        }
        ApplicationInfo appInfo = packagePacker.get(position);
        ((ImageView)convertView.findViewById(R.id.iconImage)).setImageDrawable(appInfo.loadIcon(packageManager));
        if (packagePacker.isSystem(position)) {
            ((TextView)convertView.findViewById(R.id.appNameText)).setText(String.format("(S)%s", packageManager.getApplicationLabel(appInfo)));
        }else {
            ((TextView)convertView.findViewById(R.id.appNameText)).setText(packageManager.getApplicationLabel(appInfo));
        }

        ((TextView)convertView.findViewById(R.id.packageNameText)).setText(appInfo.packageName);
        final CheckBox appCheckBox = (CheckBox)convertView.findViewById(R.id.appCheckBox);
        appCheckBox.setTag(position);
        appCheckBox.setChecked(packagePacker.isChecked(type, position));
        appCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (Integer)buttonView.getTag();
                packagePacker.setChecked(type, position, isChecked);
            }
        });
        return convertView;
    }

    public AppPackagePacker getPacker() {
        return packagePacker;
    }

    public void setPacker(AppPackagePacker packagePacker) {
        this.packagePacker = packagePacker;
    }
}
