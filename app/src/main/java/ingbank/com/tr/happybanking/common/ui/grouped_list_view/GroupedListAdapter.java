package ingbank.com.tr.happybanking.common.ui.grouped_list_view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import ingbank.com.tr.happybanking.common.ui.grouped_list_view.model.Section;
import ingbank.com.tr.happybanking.common.ui.grouped_list_view.model.SectionData;
import ingbank.com.tr.happybanking.common.ui.grouped_list_view.util.IndexPath;
import ingbank.com.tr.happybanking.common.ui.grouped_list_view.util.SectionList;

/**
 * Created by Furkan Bayraktar
 * Created on 9/23/14.
 */
public abstract class GroupedListAdapter<T extends SectionData> extends BaseAdapter {

    private Context context;
    private SectionList<T> sections;
    private ArrayList<Integer> headerPositions;
    private HashMap<String, ViewWrapper> wrappedViews;

    private boolean startupPaging;
    private boolean pagingEnabled;
    private OnPagingOccurredListener onPagingOccurredListener;

    private boolean headerViewVisibility;

    private boolean showViewWhenEmpty;

    private IndexPath selectedIndexPath;

    public GroupedListAdapter(Context context, SectionList<T> sections) {
        this.context = context;
        this.sections = sections;

        pagingEnabled = false;
        startupPaging = false;
        headerViewVisibility = true;
        showViewWhenEmpty = false;

        this.headerPositions = new ArrayList<Integer>();

        if (sections == null) {
            sections = new SectionList<T>();
        }

        if (sections.size() > 0) {
            headerPositions.add(0);
            for (int i = 0; i < sections.size() - 1; i++) {
                int lastPosition = headerPositions.get(headerPositions.size() - 1);
                headerPositions.add(lastPosition + sections.get(i).size() + 1);
            }
        }

        wrappedViews = new HashMap<String, ViewWrapper>();
    }

    @Override
    public void notifyDataSetChanged() {
        this.headerPositions = new ArrayList<Integer>();

        if (getSectionList() == null) {
            sections = new SectionList<T>();
        }

        if (getSectionList().size() > 0) {
            headerPositions.add(0);
            for (int i = 0; i < getSectionList().size() - 1; i++) {
                int lastPosition = headerPositions.get(headerPositions.size() - 1);
                headerPositions.add(lastPosition + getSectionList().get(i).size() + 1);
            }
        }

        if (!headerViewVisibility && getSectionList().size() > 1) {
            headerViewVisibility = true;
        }

        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (!headerViewVisibility && getSectionList().size() == 1) {
            return getSectionList().get(0).size();
        } else if (headerPositions.size() > 0) {
            return headerPositions.get(headerPositions.size() - 1) + getSectionList().lastSectionSize() + 1;
        } else {
            if (showViewWhenEmpty) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public Object getItem(int position) {
        if (!headerViewVisibility && getSectionList().size() == 1) {
            return getObjectAtIndexPath(new IndexPath(position));
        } else if (headerPositions.contains(position)) {
            int section = headerPositions.indexOf(position);
            return getSectionAtIndex(section).getHeader();
        } else {
            IndexPath indexPath = positionToIndexPath(position);
            return getObjectAtIndexPath(indexPath);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == 0 && getSectionList().size() == 0 && showViewWhenEmpty) {

            ViewType type;
            if (convertView != null) {
                Object tag = convertView.getTag();
                String id;
                if (tag != null && tag instanceof String) {
                    id = (String) tag;
                    type = getTypeForViewId(id);
                } else {
                    type = ViewType.UNKNOWN;
                    id = "";
                }

                if (type != ViewType.EMPTY_ROW) {
                    convertView = null;
                    removeViewForId(id);
                }
            }

            if (convertView == null) {
                convertView = getViewForEmptyList(null, parent);
                String id = UUID.randomUUID().toString();
                convertView.setTag(id);
                putViewWithId(convertView, ViewType.EMPTY_ROW, id);
            } else {
                convertView = getViewForEmptyList(convertView, parent);
            }

        } else {
            if (isPagingEnabled() && position > getCount() - 2 && getOnPagingOccurredListener() != null) {
                getOnPagingOccurredListener().onPagingOccurred();
            }

            if (!headerViewVisibility && getSectionList().size() == 1) {
                IndexPath indexPath = new IndexPath(position);

                T data = getObjectAtIndexPath(indexPath);

                if (convertView != null && getTypeForViewId(data.getIdInSection()) != ViewType.ROW) {
                    convertView = null;
                    removeViewForId(data.getIdInSection());
                }

                if (convertView != null) {
                    Class type = getRowHolderType();
                    if (!convertView.getTag().getClass().equals(type)) {
                        convertView = null;
                        removeViewForId(data.getIdInSection());
                    }
                }

                if (convertView == null) {
                    convertView = getRowView(indexPath, data, null, parent);
                    putViewWithId(convertView, ViewType.ROW, data.getIdInSection());
                } else {
                    convertView = getRowView(indexPath, data, convertView, parent);
                }

                if (indexPath.equals(selectedIndexPath)) {
                    convertView = getDraggedView(convertView);
                }

            } else if (headerPositions.contains(position)) {
                int sectionIndex = headerPositions.indexOf(position);
                Section<T> section = getSectionAtIndex(sectionIndex);

                if (convertView != null && getTypeForViewId(section.getId()) != ViewType.HEADER) {
                    convertView = null;
                    removeViewForId(section.getId());
                }

                if (convertView != null) {
                    Class type = getHeaderHolderType();
                    if (!convertView.getTag().getClass().equals(type)) {
                        convertView = null;
                        removeViewForId(section.getId());
                    }
                }

                if (convertView == null) {
                    convertView = getHeaderView(sectionIndex, section, null, parent);
                    putViewWithId(convertView, ViewType.HEADER, section.getId());
                } else {
                    convertView = getHeaderView(sectionIndex, section, convertView, parent);
                }

            } else {
                IndexPath indexPath = positionToIndexPath(position);

                T data = getObjectAtIndexPath(indexPath);

                if (convertView != null && getTypeForViewId(data.getIdInSection()) != ViewType.ROW) {
                    convertView = null;
                    removeViewForId(data.getIdInSection());
                }

                if (convertView != null) {
                    Class type = getRowHolderType();
                    if (!convertView.getTag().getClass().equals(type)) {
                        convertView = null;
                        removeViewForId(data.getIdInSection());
                    }
                }

                if (convertView == null) {
                    convertView = getRowView(indexPath, data, null, parent);
                    putViewWithId(convertView, ViewType.ROW, data.getIdInSection());
                } else {
                    convertView = getRowView(indexPath, data, convertView, parent);
                }

                if (indexPath.equals(selectedIndexPath)) {
                    convertView = getDraggedView(convertView);
                }
            }
        }
        return convertView;
    }

    protected View getDraggedView(View draggedView) {
        draggedView.setVisibility(View.INVISIBLE);
        return draggedView;
    }

    public abstract View getRowView(IndexPath index, T data, View convertView, ViewGroup parent);

    public abstract View getHeaderView(int sectionIndex, Section<T> section, View convertView, ViewGroup parent);

    public abstract View getViewForEmptyList(View convertView, ViewGroup parent);

    public abstract Class getRowHolderType();

    public abstract Class getHeaderHolderType();

    public OnPagingOccurredListener getOnPagingOccurredListener() {
        if (startupPaging) {
            return onPagingOccurredListener;
        } else {
            return null;
        }
    }

    public void setOnPagingOccurredListener(OnPagingOccurredListener onPagingOccurredListener) {
        this.onPagingOccurredListener = onPagingOccurredListener;
    }

    public void pagingCallStarted() {
        if (startupPaging) {
            this.pagingEnabled = false;
        }
    }

    public void pagingCallFinished(boolean continuePaging) {
        if (startupPaging) {
            this.pagingEnabled = continuePaging;
        }
    }

    public boolean isPagingEnabled() {
        return pagingEnabled;
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        this.startupPaging = pagingEnabled;
        this.pagingEnabled = pagingEnabled;
    }

    public SectionList<T> getSectionList() {
        return sections;
    }

    public Section<T> getSectionAtIndex(int index) {
        return getSectionList().get(index);
    }

    public T getObjectAtIndexPath(IndexPath indexPath) {
        return getSectionList().get(indexPath.getSection()).get(indexPath.getRow());
    }

    public Context getContext() {
        return context;
    }

    public int getSectionNumber(int position) {
        return headerPositions.indexOf(position);
    }

    public IndexPath positionToIndexPath(int position) {
        if (headerPositions.size() == 1) {
            return new IndexPath(position - 1);
        }

        for (int i = 0; i < headerPositions.size() - 1; i++) {
            int floor = headerPositions.get(i);
            if (floor < position && position < headerPositions.get(i + 1)) {
                return new IndexPath(position - 1 - floor, i);
            }
        }

        int lastIndex = headerPositions.size() - 1;
        int floor = headerPositions.get(lastIndex);

        if (position > floor) {
            return new IndexPath(position - 1 - floor, lastIndex);
        }

        return new IndexPath();
    }

    private ViewType getTypeForViewId(String id) {
        ViewWrapper wrapper = wrappedViews.get(id);
        if (wrapper == null) {
            return ViewType.UNKNOWN;
        }
        return wrapper.type;
    }

    private void removeViewForId(String id) {
        wrappedViews.remove(id);
    }

    private void putViewWithId(View view, ViewType type, String id) {
        wrappedViews.put(id, new ViewWrapper(type, view));
    }

    public void onPick(IndexPath indexPath) {
        selectedIndexPath = indexPath;
    }

    public void onDrop(IndexPath from, IndexPath to) {
        if (from.getSection() > sections.size()) {
            return;
        }

        T tValue = getObjectAtIndexPath(from);
        getSectionList().get(from.getSection()).remove(tValue);
        getSectionList().get(to.getSection()).add(to.getRow(), tValue);

        selectedIndexPath = null;

        notifyDataSetChanged();
    }

    public ViewType getViewType(int position) {
        if (position == 0 && getSectionList().size() == 0 && showViewWhenEmpty) {
            return ViewType.EMPTY_ROW;
        } else {
            if (!headerViewVisibility && getSectionList().size() == 1) {
                return ViewType.ROW;
            } else if (headerPositions.contains(position)) {
                return ViewType.HEADER;
            } else {
                return ViewType.ROW;
            }
        }
    }

    protected void setSingleHeaderEnabled(boolean enabled) {
        if (getSectionList().size() <= 1) {
            headerViewVisibility = enabled;
            notifyDataSetChanged();
        }
    }

    protected void setShowViewWhenEmpty(boolean enabled) {
        this.showViewWhenEmpty = enabled;
    }

    public enum ViewType {
        EMPTY_ROW,
        HEADER,
        ROW,
        UNKNOWN
    }

    public interface OnPagingOccurredListener {
        public void onPagingOccurred();
    }

    protected class ViewWrapper {
        View convertView;
        ViewType type;

        ViewWrapper(ViewType type, View convertView) {
            this.type = type;
            this.convertView = convertView;
        }
    }
}
