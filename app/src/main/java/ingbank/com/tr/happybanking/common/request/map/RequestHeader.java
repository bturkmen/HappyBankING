package ingbank.com.tr.happybanking.common.request.map;


import com.google.gson.annotations.SerializedName;

public class RequestHeader {

    @SerializedName("Channel")
    private String channel;
    @SerializedName("Language")
    private String language;
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("DeviceModel")
    private String deviceModel;
    @SerializedName("ApplicationID")
    private String applicationID;
    @SerializedName("SessionToken")
    private String sessionToken;
    @SerializedName("ActivationId")
    private String activationId;
    @SerializedName("AuthKey")
    private String authKey;
    @SerializedName("IpAddress")
    private String ipAddress;
    @SerializedName("DebugMode")
    private int debugMode;

    public RequestHeader copy() {
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setChannel(this.getChannel());
        requestHeader.setLanguage(this.getLanguage());
        requestHeader.setDeviceId(this.getDeviceId());
        requestHeader.setDeviceModel(this.getDeviceModel());
        requestHeader.setApplicationID(this.getApplicationID());
        requestHeader.setSessionToken(this.getSessionToken());
        requestHeader.setActivationId(this.getActivationId());
        requestHeader.setAuthKey(this.getAuthKey());
        requestHeader.setIpAddress(this.getIpAddress());
        requestHeader.setDebugMode(this.getDebugMode());

        return requestHeader;
    }

    public static RequestHeader createRequestHeader(String channel, String language, String deviceId, String deviceModel, String applicationId,
                                                    String authKey, String sessionToken, String activationId, String ipAddress, boolean debugMode) {
        RequestHeader bodyHeader = new RequestHeader();
        bodyHeader.setChannel(channel);
        bodyHeader.setLanguage(language);
        bodyHeader.setDeviceId(deviceId);
        bodyHeader.setDeviceModel(deviceModel);
        bodyHeader.setApplicationID(applicationId);
        bodyHeader.setAuthKey(authKey);
        bodyHeader.setSessionToken(sessionToken);
        bodyHeader.setActivationId(activationId);
        bodyHeader.setIpAddress(ipAddress);
        if(debugMode)
            bodyHeader.setDebugMode(1);

        return bodyHeader;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getActivationId() {
        return activationId;
    }

    public void setActivationId(String activationId) {
        this.activationId = activationId;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(int debugMode) {
        this.debugMode = debugMode;
    }
}
