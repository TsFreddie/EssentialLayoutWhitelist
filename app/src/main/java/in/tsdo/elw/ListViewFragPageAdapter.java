package in.tsdo.elw;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

class ListViewFragPageAdapter extends FragmentStatePagerAdapter {
    private final List<ListViewFragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ListViewFragPageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(ListViewFragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    public void notifyFragmentDataSetChanged(int position) {
        mFragmentList.get(position).notifyDataSetChanged();
    }

    public void notifyNewPacker(AppPackagePacker appPacker) {
        for (ListViewFragment fragment : mFragmentList) {
            fragment.setPacker(appPacker);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}