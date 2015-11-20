package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Channel implements Serializable {

    @SerializedName("SpecialBank")
    private boolean specialBank;
    @SerializedName("FaxNumber")
    private String faxNumber;
    @SerializedName("RentalCase")
    private boolean rentalCase;
    @SerializedName("CityCode")
    private int cityCode;
    @SerializedName("OnlineDeposite")
    private boolean onlineDeposite;
    @SerializedName("PostCode")
    private String postCode;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("City")
    private String city;
    @SerializedName("Phsycaldisabled")
    private boolean phsycaldisabled;
    @SerializedName("Obcorner")
    private boolean obcorner;
    @SerializedName("Name")
    private String name;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("BranchRegion")
    private int branchRegion;
    @SerializedName("BranchConcept")
    private int branchConcept;
    @SerializedName("Internet")
    private boolean internet;
    @SerializedName("BranchType")
    private String branchType;
    @SerializedName("County")
    private String county;
    @SerializedName("Adress")
    private String adress;
    @SerializedName("BranchIngName")
    private String branchIngName;
    @SerializedName("BranchCode")
    private int branchCode;
    @SerializedName("BankChannelType")
    private String bankChannelType;
    @SerializedName("SightlessDisabled")
    private boolean sightlessDisabled;
    @SerializedName("Latitude")
    private String latitude;
    @SerializedName("BranchClass")
    private String branchClass;
    @SerializedName("AtmNo")
    private String atmNo;
    @SerializedName("BranchLunch")
    private boolean branchLunch;
    @SerializedName("CountyCode")
    private int countyCode;
    @SerializedName("BranchEmail")
    private String branchEmail;

    private double distanceToPoint = Double.MIN_VALUE;

    public Channel() {
    }

    public Channel(Channel temp) {
        this.specialBank = temp.isSpecialBank();
        this.faxNumber = temp.getFaxNumber();
        this.rentalCase = temp.isRentalCase();
        this.cityCode = temp.getCityCode();
        this.onlineDeposite = temp.isOnlineDeposite();
        this.postCode = temp.getPostCode();
        this.phoneNumber = temp.getPhoneNumber();
        this.city = temp.getCity();
        this.phsycaldisabled = temp.isPhsycaldisabled();
        this.obcorner = temp.isObcorner();
        this.name = temp.getName();
        this.longitude = temp.getLongitude();
        this.branchRegion = temp.getBranchRegion();
        this.branchConcept = temp.getBranchConcept();
        this.internet = temp.isInternet();
        this.branchType = temp.getBranchType();
        this.county = temp.getCounty();
        this.adress = temp.getAdress();
        this.branchIngName = temp.getBranchIngName();
        this.branchCode = temp.getBranchCode();
        this.bankChannelType = temp.getBankChannelType();
        this.sightlessDisabled = temp.isSightlessDisabled();
        this.latitude = temp.getLatitude();
        this.branchClass = temp.getBranchClass();
        this.atmNo = temp.getAtmNo();
        this.branchLunch = temp.isBranchLunch();
        this.countyCode = temp.getCountyCode();
        this.branchEmail = temp.getBranchEmail();
        this.distanceToPoint = temp.getDistanceToPoint();
    }

    public static String formatDistance(double meters) {
        if (meters < 1000) {
            return ((int) meters) + "m";
        } else if (meters < 10000) {
            return formatDec(meters / 1000f, 1) + "km";
        } else {
            return ((int) (meters / 1000f)) + "km";
        }
    }

    static String formatDec(double val, int dec) {
        int factor = (int) Math.pow(10, dec);

        int front = (int) (val);
        int back = (int) Math.abs(val * (factor)) % factor;

        return front + "," + back;
    }

    public String getDistanceToPointAsString() {
        return formatDistance(distanceToPoint);
    }

    public double getDistanceToPoint() {
        return distanceToPoint;
    }

    public void setDistanceToPoint(double distanceToPoint) {
        this.distanceToPoint = distanceToPoint;
    }

    public boolean isSpecialBank() {
        return specialBank;
    }

    public void setSpecialBank(boolean specialBank) {
        this.specialBank = specialBank;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public boolean isRentalCase() {
        return rentalCase;
    }

    public void setRentalCase(boolean rentalCase) {
        this.rentalCase = rentalCase;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public boolean isOnlineDeposite() {
        return onlineDeposite;
    }

    public void setOnlineDeposite(boolean onlineDeposite) {
        this.onlineDeposite = onlineDeposite;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isPhsycaldisabled() {
        return phsycaldisabled;
    }

    public void setPhsycaldisabled(boolean phsycaldisabled) {
        this.phsycaldisabled = phsycaldisabled;
    }

    public boolean isObcorner() {
        return obcorner;
    }

    public void setObcorner(boolean obcorner) {
        this.obcorner = obcorner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getBranchRegion() {
        return branchRegion;
    }

    public void setBranchRegion(int branchRegion) {
        this.branchRegion = branchRegion;
    }

    public int getBranchConcept() {
        return branchConcept;
    }

    public void setBranchConcept(int branchConcept) {
        this.branchConcept = branchConcept;
    }

    public boolean isInternet() {
        return internet;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public String getBranchType() {
        return branchType;
    }

    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBranchIngName() {
        return branchIngName;
    }

    public void setBranchIngName(String branchIngName) {
        this.branchIngName = branchIngName;
    }

    public int getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(int branchCode) {
        this.branchCode = branchCode;
    }

    public String getBankChannelType() {
        return bankChannelType;
    }

    public void setBankChannelType(String bankChannelType) {
        this.bankChannelType = bankChannelType;
    }

    public boolean isSightlessDisabled() {
        return sightlessDisabled;
    }

    public void setSightlessDisabled(boolean sightlessDisabled) {
        this.sightlessDisabled = sightlessDisabled;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getBranchClass() {
        return branchClass;
    }

    public void setBranchClass(String branchClass) {
        this.branchClass = branchClass;
    }

    public String getAtmNo() {
        return atmNo;
    }

    public void setAtmNo(String atmNo) {
        this.atmNo = atmNo;
    }

    public boolean isBranchLunch() {
        return branchLunch;
    }

    public void setBranchLunch(boolean branchLunch) {
        this.branchLunch = branchLunch;
    }

    public int getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(int countyCode) {
        this.countyCode = countyCode;
    }

    public String getBranchEmail() {
        return branchEmail;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

//    public void updateWithChannel(Channel temp) {
//        this.specialBank = temp.isSpecialBank();
//        this.faxNumber = temp.getFaxNumber();
//        this.rentalCase = temp.isRentalCase();
//        this.cityCode = temp.getCityCode();
//        this.onlineDeposite = temp.isOnlineDeposite();
//        this.postCode = temp.getPostCode();
//        this.phoneNumber = temp.getPhoneNumber();
//        this.city = temp.getCity();
//        this.phsycaldisabled = temp.isPhsycaldisabled();
//        this.obcorner = temp.isObcorner();
//        this.name = temp.getName();
//        this.longitude = temp.getLongitude();
//        this.branchRegion = temp.getBranchRegion();
//        this.branchConcept = temp.getBranchConcept();
//        this.internet = temp.isInternet();
//        this.branchType = temp.getBranchType();
//        this.county = temp.getCounty();
//        this.adress = temp.getAdress();
//        this.branchIngName = temp.getBranchIngName();
//        this.branchCode = temp.getBranchCode();
//        this.bankChannelType = temp.getBankChannelType();
//        this.sightlessDisabled = temp.isSightlessDisabled();
//        this.latitude = temp.getLatitude();
//        this.branchClass = temp.getBranchClass();
//        this.atmNo = temp.getAtmNo();
//        this.branchLunch = temp.isBranchLunch();
//        this.countyCode = temp.getCountyCode();
//        this.branchEmail = temp.getBranchEmail();
//        this.distanceToPoint = temp.getDistanceToPoint();
//    }

    public String getChannelId() {
        String channelId = null;

        if (getAtmNo() != null) {
            //ATM
            channelId = String.format("A%s", this.getAtmNo());
        } else if (getBranchCode() != 0) {
            //Branch
            channelId = String.format("B%d", this.getBranchCode());
        } else {
            //Log.e(TAG, "FATAL Error: undefined channel type");
        }

        return channelId;
    }

    @Override
    public int hashCode() {
        return getChannelId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Channel) {
            Channel channel = (Channel) obj;
            return this.getChannelId().equals(channel.getChannelId());
        }
        return false;
    }

    public boolean isAtm() {
        return getBankChannelType().equals("ATM");
    }

    public boolean isBranch() {
        return getBankChannelType().equals("BRANCH");
    }

    public boolean isConceptBank() {
        return (getBranchConcept() == 1 || getBranchConcept() == 2);
    }

    public boolean isDisabledBank() {
        return isSightlessDisabled() || isPhsycaldisabled();
    }

}
