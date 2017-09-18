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
    private AppPackagePacker appPacker;
    private int type;
    private View progressBarView;
    private View noResultView;
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
        View emptyView = view.findViewById(R.id.empty);
        appList.setEmptyView(view.findViewById(R.id.empty));
        progressBarView = emptyView.findViewById(R.id.progress_bar);
        noResultView = emptyView.findViewById(R.id.no_result);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(state != null) {
            progressBarView.setVisibility(View.GONE);
            noResultView.setVisibility(View.VISIBLE);
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
