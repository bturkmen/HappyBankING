package ingbank.com.tr.happybanking.common.ui.grouped_list_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

import ingbank.com.tr.happybanking.R;

public class GroupedListView extends ListView {

    private boolean singleHeaderEnabled;
    private boolean pagingEnabled;
    private boolean showViewWhenEmpty;

    public GroupedListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GroupedListView);
        singleHeaderEnabled = a.getBoolean(R.styleable.GroupedListView_single_header_enabled, true);
        pagingEnabled = a.getBoolean(R.styleable.GroupedListView_paging_enabled, false);
        showViewWhenEmpty = a.getBoolean(R.styleable.GroupedListView_show_view_when_empty, false);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof GroupedListAdapter) {
            ((GroupedListAdapter) adapter).setSingleHeaderEnabled(singleHeaderEnabled);
            ((GroupedListAdapter) adapter).setPagingEnabled(pagingEnabled);
            ((GroupedListAdapter) adapter).setShowViewWhenEmpty(showViewWhenEmpty);
        }
        super.setAdapter(adapter);
    }
}
