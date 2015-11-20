package ingbank.com.tr.happybanking.common.ui.grouped_list_view.model;

import java.util.UUID;

/**
 * Created by Furkan Bayraktar
 * Created on 10/1/14.
 */
public abstract class SectionData {

    private String mId;

    public SectionData() {
        this.mId = UUID.randomUUID().toString();
    }

    public String getIdInSection() {
        return mId;
    }
}
