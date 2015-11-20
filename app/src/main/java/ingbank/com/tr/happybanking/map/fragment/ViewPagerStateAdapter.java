package ingbank.com.tr.happybanking.map.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by aydinozkan on 31/07/15.
 */
public class ViewPagerStateAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private List<T> mItems;

    public ViewPagerStateAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return this.mItems.get(position);
    }

    @Override
    public int getCount() {
        return this.mItems == null ? 0 : this.mItems.size();
    }

    public List<T> getItems() {
        return mItems;
    }

    public void setItems(List<T> items) {
        this.mItems = items;
        this.notifyDataSetChanged();
    }

    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }

}
