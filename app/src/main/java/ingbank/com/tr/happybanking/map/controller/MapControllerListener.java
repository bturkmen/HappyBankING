package ingbank.com.tr.happybanking.map.controller;

public interface MapControllerListener {
    void onLocationServiceStateChanged(boolean enabled);

    void onLocationFound(MapLocation location);

    void onLocationChanged(MapLocation location);

}