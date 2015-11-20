package ingbank.com.tr.happybanking.common.ui.fingerprint;

//import com.samsung.android.sdk.SsdkUnsupportedException;
//import com.samsung.android.sdk.pass.Spass;
//import com.samsung.android.sdk.pass.SpassFingerprint;

/**
 * Created on 7/8/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class FingerprintUtility {

//    private static FingerprintUtility instance;
//
//    private Context context;
//    private boolean featureEnabled;
//    private SpassFingerprint spassFingerprint;
//    private Spass spass;
//
//
//    public static FingerprintUtility getInstance(Context context) {
//        if (instance == null) {
//            instance = new FingerprintUtility(context);
//        }
//        return instance;
//    }
//
//    private FingerprintUtility(Context context) {
//        this.context = context;
//        spass = new Spass();
//
//        boolean initCheck = true;
//        try {
//            spass.initialize(context);
//        } catch (SsdkUnsupportedException e) {
//            initCheck = false;
//            e.printStackTrace();
//        } catch (UnsupportedOperationException e) {
//            initCheck = false;
//            e.printStackTrace();
//        }
//        if (initCheck) {
//            featureEnabled = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);
//            spassFingerprint = new SpassFingerprint(this.context);
//        } else {
//            featureEnabled = false;
//        }
//    }
//
//    public void showFingerprintDialog(final FingerprintListener mListener) {
//        try {
//            if (featureEnabled) {
//                if (hasRegisteredFinger()) {
//                    spassFingerprint.startIdentifyWithDialog(context, new SpassFingerprint.IdentifyListener() {
//                        @Override
//                        public void onFinished(int i) {
//                            if (i == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS) {
//                                mListener.onAuthenticationCompleted(FingerprintListener.AUTHENTICATION_SUCCESS);
//                            } else {
//                                mListener.onAuthenticationCompleted(FingerprintListener.AUTHENTICATION_FAILURE);
//                            }
//                        }
//
//                        @Override
//                        public void onReady() {
//
//                        }
//
//                        @Override
//                        public void onStarted() {
//
//                        }
//                    }, false);
//                }
//            }
//        } catch (UnsupportedOperationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean hasRegisteredFinger() {
//        if (featureEnabled) {
//            return spassFingerprint.hasRegisteredFinger();
//        } else {
//            return false;
//        }
//    }
//
//    public boolean isFeatureEnabled() {
//        return featureEnabled;
//    }
//
//    public void registerFingerprint(final FingerprintListener mListener) {
//        try {
//            if (featureEnabled) {
//                spassFingerprint.registerFinger(context, new SpassFingerprint.RegisterListener() {
//                    @Override
//                    public void onFinished() {
//                        mListener.onRegisterCompleted();
//                    }
//                });
//            }
//        } catch (UnsupportedOperationException e) {
//            e.printStackTrace();
//        }
//    }
}
