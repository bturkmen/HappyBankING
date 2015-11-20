package ingbank.com.tr.happybanking.map.listener.map;


/**
 * Created by hatasoy on 12/06/15.
 */
public interface OnInteractorListener {
    void onBeforeRequest();
    void onAfterRequest();
    void onResponseError(Object error);
}
