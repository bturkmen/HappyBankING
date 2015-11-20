package ingbank.com.tr.happybanking.presenter.map;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import ingbank.com.tr.happybanking.map.MapView;
import ingbank.com.tr.happybanking.map.listener.map.onListAtmBranchListener;
import ingbank.com.tr.happybanking.map.model.map.Channel;
import volley.VolleyError;

public class MapPresenter {
    private MapInteractor interactor;
    private MapView view;

    public MapPresenter(MapView view) {
        this.interactor = new MapInteractor();
        this.view = view;
    }

    public void listAtmBranch() {
        this.interactor.listAtmBranch(new onListAtmBranchListener() {
            @Override
            public void onListAtmBranch(ArrayList<Channel> channelList) {
                view.onListAtmBranch(channelList);
            }

            @Override
            public void onBeforeRequest() {
                view.showWaitingDialog();
            }

            @Override
            public void onAfterRequest() {
                //Map loading takes time...
                view.dismissWaitingDialog();
            }

            @Override
            public void onResponseError(Object error) {
                handleServiceError((VolleyError) error);
            }
        });
    }


    /**
     * Handle Service Error for ConsumerLoanCalculatorResponse
     *
     * @param error response error to display on the screen
     */
    private void handleServiceError(VolleyError error) {
        String LOG_TAG = "AccountsActivity";
        try {

            if (error.networkResponse != null && error.networkResponse.data != null) {
                Log.e(LOG_TAG, "RAW onErrorResponse: " + new String(error.networkResponse.data));

                JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                String errorMsg = jsonObject.getJSONObject("Header").getString("ResponseMessage");
                @SuppressWarnings("unused")
                String errorCode = jsonObject.getJSONObject("Header").getString("ResponseCode");

                view.showError(errorMsg);
            } else {
                view.onNetworkError();
            }
        } catch (Exception ignored) {
            Log.e(LOG_TAG, "EXCEPTION onErrorResponse: " + ignored.toString());
        }
    }
}
