package ingbank.com.tr.happybanking.common.request.map.response;

import com.google.gson.annotations.SerializedName;


public class CompositionResponse<T> {
    @SerializedName("Header")
    private ResponseHeader header;


    @SerializedName("ResponseBody")
    private T mResponse;

    public T getResponse() {
        return mResponse;
    }

    public void setResponse(T response) {
        mResponse = response;
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

}

/*
import com.ingbanktr.common.model.response.map.ListAtmBranchResponse;

public class CompositionResponse  {

    @SerializedName("CompositionResponse")
    private ListAtmBranchResponse mListAtmBranchResponse;

    public ListAtmBranchResponse getListAtmBranchResponse() {
        return mListAtmBranchResponse;
    }

    public void setListAtmBranchResponse(ListAtmBranchResponse listAtmBranchResponse) {
        mListAtmBranchResponse = listAtmBranchResponse;
    }
}
*/