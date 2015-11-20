package ingbank.com.tr.happybanking.presenter.map;

import android.util.Log;

import java.util.ArrayList;

import ingbank.com.tr.happybanking.SuccessListener;
import ingbank.com.tr.happybanking.common.request.map.ListAtmBranchRequest;
import ingbank.com.tr.happybanking.common.request.map.response.CompositionResponse;
import ingbank.com.tr.happybanking.map.listener.map.onListAtmBranchListener;
import ingbank.com.tr.happybanking.map.model.map.Channel;
import ingbank.com.tr.happybanking.map.model.map.ListAtmBranchResponse;
import volley.Response;
import volley.VolleyError;

public class MapInteractor {

    public void listAtmBranch(final onListAtmBranchListener listener) {
        ListAtmBranchRequest request = new ListAtmBranchRequest();
        try {
            listener.onBeforeRequest();
            listAtmBranch(request, new SuccessListener<CompositionResponse<ListAtmBranchResponse>>() {
                @Override
                public void onSuccess(CompositionResponse<ListAtmBranchResponse> response) {
                    ArrayList<Channel> channelList = response.getResponse().getChannelList();
                    Log.d("Size of Channel List: ", Integer.toString(channelList.size()));

                    listener.onAfterRequest();
                    listener.onListAtmBranch(channelList);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onAfterRequest();
                    listener.onResponseError(error);
                }
            });
        } catch (Exception ignored) {
            listener.onAfterRequest();
        }
    }

    private void listAtmBranch(ListAtmBranchRequest request, SuccessListener<CompositionResponse<ListAtmBranchResponse>> successListener, Response.ErrorListener errorListener) {

    }

}
