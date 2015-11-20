package ingbank.com.tr.happybanking.common.ui.grouped_list_view.util;

/**
 * Created by Furkan Bayraktar
 * Created on 10/16/14.
 */
public class IndexPath {

    private int row;
    private int section;

    public IndexPath() {
        this.section = 0;
        this.row = 0;
    }

    public IndexPath(int row) {

        this.section = 0;

        if (row < 0) {
            this.row = 0;
        } else {
            this.row = row;
        }
    }

    public IndexPath(int row, int section) {

        if (row < 0) {
            this.row = 0;
        } else {
            this.row = row;
        }

        if (section < 0) {
            this.section = 0;
        } else {
            this.section = section;
        }
    }

    public int getRow() {
        return row;
    }

    public int getSection() {
        return section;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IndexPath) {
            return this.row == ((IndexPath) o).getRow() && this.section == ((IndexPath) o).getSection();
        } else {
            return super.equals(o);
        }
    }
}
