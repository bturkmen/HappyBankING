package ingbank.com.tr.happybanking.common.request.map.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aydinozkan on 25/05/15.
 */
public class ResponseHeader {
    @SerializedName("ResponseMessage")
    private
    String responseMessage;

    @SerializedName("ResponseCode")
    private
    String responseCode;


    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
