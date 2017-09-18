package in.tsdo.elw;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

public class ListViewFragment extends Fragment
{
    private ListView appList;
    private AppAdapter appAdapter;
    private AppPackagePacker appPacker;
    private int type;
    Parcelable state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        appList = (ListView)view.findViewById(R.id.app_list);
        type = getArguments().getInt("FRAGMENT_TYPE");

        appAdapter = new AppAdapter(getActivity(), appPacker, type);
        appList.setAdapter(appAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(state != null) {
            appList.onRestoreInstanceState(state);
        }
    }

    @Override
    public void onPause() {
        state = appList.onSaveInstanceState();
        super.onPause();
    }

    public void notifyDataSetChanged() {
        if (appAdapter == null) {
            return;
        }
        appAdapter.notifyDataSetChanged();
    }

    public void setPacker(AppPackagePacker appPacker) {
        this.appPacker = appPacker;
        if (appAdapter == null) {
            return;
        }
        appAdapter.setPacker(appPacker);
    }
}
