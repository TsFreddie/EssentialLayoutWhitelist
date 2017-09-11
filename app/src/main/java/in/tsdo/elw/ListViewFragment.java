package in.tsdo.elw;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListViewFragment extends Fragment
{
    private ListView appList;
    private AppAdapter appAdapter;
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppPickerActivity activity = ((AppPickerActivity)getActivity());
        AppPackagePacker appPacker = activity.getAppPacker();
        if (appPacker == null) return;
        appAdapter = new AppAdapter(activity, appPacker, activity.getPackageManager(), type);
        appList.setAdapter(appAdapter);
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
        appAdapter.notifyDataSetChanged();
    }

    public void notifyNewPacker() {
        appAdapter.setPacker(((AppPickerActivity)getActivity()).getAppPacker());
        appAdapter.notifyDataSetChanged();
    }

}
