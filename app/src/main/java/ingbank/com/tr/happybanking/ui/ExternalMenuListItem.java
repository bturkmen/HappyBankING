package ingbank.com.tr.happybanking.ui;

import android.graphics.drawable.Drawable;

public class ExternalMenuListItem {
    private String id;
    private String title;
    private Drawable image;

    public ExternalMenuListItem(String id, String title, Drawable image) {
        super();
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
