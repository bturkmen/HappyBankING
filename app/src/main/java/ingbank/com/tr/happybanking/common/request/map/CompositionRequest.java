package ingbank.com.tr.happybanking.common.request.map;


import java.lang.reflect.Type;

/**
 * This is the class that contains common body properties of the request that used in ParaMara Service.
 *
 * @SerializedName annotation is used  for json serialization due to Gson Library.
 * It is important to know that this class is application specific class.
 * If necessary to send and request with different body, it is required that new class is written.
 * The requests that belongs to ParaMara Application have to inherit this class because of their request body...
 */
public abstract class CompositionRequest {
    //@SerializedName("Header")
    private transient RequestHeader mHeader;

    public RequestHeader getHeader() {
        return mHeader;
    }

    public void setHeader(RequestHeader mHeader) {
        if (mHeader != null && mHeader.getActivationId() != null) {
            if (mHeader.getActivationId().equals("-1")) {
                mHeader.setActivationId("");
            }
        }
        this.mHeader = mHeader;
    }

    public abstract Type getResponseType();
}
