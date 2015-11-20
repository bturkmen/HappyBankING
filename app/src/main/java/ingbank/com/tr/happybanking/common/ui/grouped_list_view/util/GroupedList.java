package ingbank.com.tr.happybanking.common.ui.grouped_list_view.util;



import java.util.List;

import ingbank.com.tr.happybanking.common.ui.grouped_list_view.model.Section;
import ingbank.com.tr.happybanking.common.ui.grouped_list_view.model.SectionData;

/**
 * Created by Furkan Bayraktar
 * Created on 10/8/14.
 */
public interface GroupedList<E extends SectionData> extends List<Section<E>> {

    public int lastSectionSize();

}
