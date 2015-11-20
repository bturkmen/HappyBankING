package ingbank.com.tr.happybanking.map;

public interface BaseView {

    void showWaitingDialog();

    void dismissWaitingDialog();

    void onNetworkError();

}
