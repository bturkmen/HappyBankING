package ingbank.com.tr.happybanking.map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ingbank.com.tr.happybanking.MainActivity;
import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.ui.blurbehind.BlurBehind;
import ingbank.com.tr.happybanking.map.adapter.AtmBranchListFilter;
import ingbank.com.tr.happybanking.map.controller.MapController;
import ingbank.com.tr.happybanking.map.controller.MapControllerListener;
import ingbank.com.tr.happybanking.map.controller.MapLocation;
import ingbank.com.tr.happybanking.map.fragment.AtmBranchListFragment;
import ingbank.com.tr.happybanking.map.fragment.AtmBranchMapFragment;
import ingbank.com.tr.happybanking.map.model.map.Channel;
import ingbank.com.tr.happybanking.presenter.map.MapPresenter;
import ingbank.com.tr.happybanking.ui.BaseActivity;


public class MapActivity extends BaseActivity implements MapView {

    //region Variables
    public static final String TAG = "MapActivity";
    public final static String CHANNELTYPE_BRANCH = "BRANCH";
    public final static String CHANNELTYPE_ATM = "ATM";
    public final static String CHANNELTYPE_HEADER = "HEADER"; //only used in search view for section headers, e.g. Regions and ATMs & Branches
    public static FragmentManager mFragmentManager;
    protected Intent myIntent;
    private boolean isFilterDialogVisible = false;
    private TableLayout mTblFilterOptions;
    private ImageView mImgBranchIcon, mImgATMIcon, mImgSpecialIcon, mImgConceptIcon, mImgDisabledIcon;
    private TextView mTxtBranch, mTxtATM, mTxtSpecial, mTxtConcept, mTxtDisabled;
    private ImageView mImgBranchTick, mImgATMTick, mImgSpecialTick, mImgConceptTick, mImgDisabledTick;
    private TableRow mRowBranch, mRowATM, mRowSpecial, mRowConcept, mRowDisabled;
    private AtmBranchMapFragment mMapFragment;
    private AtmBranchListFragment mListFragment;
    private boolean mMapShown;
    private boolean mLocationSaved;
    private int mMapFilterValue, mTmpFilterValue;
    private boolean mMapFilterDialogOpen;
    private MapLocation mMyLocation;
    private Menu menu;
    private MenuItem menuItemMapView, menuItemListView;
    private ActiveFragment activeFragment;

    //endregion

    //region Activity Lifecycle
    private MapController mMapController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {


            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.pgMap_lblHeader);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            initMemberVariables();

            initViews();
            initCallbacks();
            showMapFragment();
            mMapController = MapController.GetInstance();
            mMapController.setListener(new MapControllerListener() {
                @Override
                public void onLocationServiceStateChanged(boolean enabled) {
                    mMapFragment.setLocationEnabled(enabled);
                    if (enabled) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Location lastBestStaleLocation = MapController.GetInstance().getLastBestStaleLocation();
                                while (lastBestStaleLocation == null) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception ignored) {

                                    }
                                    lastBestStaleLocation = MapController.GetInstance().getLastBestStaleLocation();
                                }
                                final Location lastLocation = lastBestStaleLocation;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MapLocation location = MapLocation.newInstance(lastLocation);
                                        mMapFragment.setLocation(location);
                                        mListFragment.setLocation(location);
                                    }
                                });

                            }
                        }).start();
                    }
                }

                @Override
                public void onLocationFound(MapLocation location) {
                    mMyLocation = location;
                    mMapFragment.setLocation(location);
                    mListFragment.setLocation(location);
                    if (!location.isDefault()) {
                        saveUserLocation(mMyLocation);

                    }
                }

                @Override
                public void onLocationChanged(MapLocation location) {
                    mMapFragment.onLocationChanged(location);
                    if (activeFragment != ActiveFragment.LIST)
                        mListFragment.setLocation(location);
                }
            });
            mMapController.onCreate(this);
            BlurBehind.getInstance().setBackground(this);


        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(status, this, status);
            errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            errorDialog.show();

        }
    }

    @Override
    public void onResume() {
        Log.d("Status: ", "onResume");
        super.onResume();
        if (mMapController != null)
            mMapController.onResume();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_map;
    }

    @Override
    protected void onPause() {
        Log.d("Status: ", "onPause");
        super.onPause();
        if (mMapController != null)
            mMapController.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Status: ", "onStart");
        if (mMapController != null)
            mMapController.onStart();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItemMapView.setVisible(mMapShown);
        menuItemListView.setVisible(!mMapShown);
        return true;

    }

    //endregion

    @Override
    protected void onStop() {
        Log.d("Status: ", "onStop");
        super.onStop();
        if (mMapController != null)
            mMapController.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapController != null)
            mMapController.onDestroy();
    }

    //region Activity UI related methods
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void displayLocationGetDirectionWarning() {
        createAlertDialog(getString(R.string.errorHeader100), getString(R.string.error100), getString(R.string.pgMap_open_location_settings), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
                //get gps
            }
        }, getString(R.string.global_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }, true).show();
    }

    public void switchtoListFragment() {
        activeFragment = ActiveFragment.LIST;
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        ft2.setCustomAnimations(R.anim.map_slide_in_right, R.anim.map_left_evade);
        ft2.show(mListFragment);
        ft2.hide(mMapFragment);
        mMapFragment.setSelectedMarker(null);
        ft2.commitAllowingStateLoss();
        mMapShown = false;
        supportInvalidateOptionsMenu();
    }

    public void switchtoMapFragment() {
        activeFragment = ActiveFragment.MAP;
        mMapShown = true;
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        ft2.setCustomAnimations(R.anim.map_slide_in_left, R.anim.map_right_evade);
        ft2.show(mMapFragment);
        ft2.hide(mListFragment);
        ft2.commitAllowingStateLoss();
        supportInvalidateOptionsMenu();
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void openFilterDialog() {
        if (0 < (mTmpFilterValue & AtmBranchListFilter.BRANCH_SELECTED)) {

            isFilterDialogVisible = true;
            mTxtBranch.setTextColor(getResources().getColor(R.color.ing_orange));
            mImgBranchTick.setVisibility(View.VISIBLE);
        } else {
            mTxtBranch.setTextColor(getResources().getColor(R.color.black_gray));
            mImgBranchTick.setVisibility(View.GONE);
        }
        if (0 < (mTmpFilterValue & AtmBranchListFilter.ATM_SELECTED)) {
            mTxtATM.setTextColor(getResources().getColor(R.color.ing_orange));
            mImgATMTick.setVisibility(View.VISIBLE);
        } else {
            mTxtATM.setTextColor(getResources().getColor(R.color.black_gray));
            mImgATMTick.setVisibility(View.GONE);
        }
        if (0 < (mTmpFilterValue & AtmBranchListFilter.SPECIAL_SELECTED)) {
            mTxtSpecial.setTextColor(getResources().getColor(R.color.ing_orange));
            mImgSpecialTick.setVisibility(View.VISIBLE);
        } else {
            mTxtSpecial.setTextColor(getResources().getColor(R.color.black_gray));
            mImgSpecialTick.setVisibility(View.GONE);
        }
        if (0 < (mTmpFilterValue & AtmBranchListFilter.CONCEPT_SELECTED)) {
            mTxtConcept.setTextColor(getResources().getColor(R.color.ing_orange));
            mImgConceptTick.setVisibility(View.VISIBLE);
        } else {
            mTxtConcept.setTextColor(getResources().getColor(R.color.black_gray));
            mImgConceptTick.setVisibility(View.GONE);
        }
        if (0 < (mTmpFilterValue & AtmBranchListFilter.DISABLED_SELECTED)) {
            mTxtDisabled.setTextColor(getResources().getColor(R.color.ing_orange));
            mImgDisabledTick.setVisibility(View.VISIBLE);
        } else {
            mTxtDisabled.setTextColor(getResources().getColor(R.color.black_gray));
            mImgDisabledTick.setVisibility(View.GONE);
        }

        mTblFilterOptions.setVisibility(View.VISIBLE);
        //        settings.setAllGesturesEnabled(true);
        mMapFilterDialogOpen = true;
    }

    //endregion

    //region Google Play Services API related methods

    public void closeFilterDialog() {
        isFilterDialogVisible = false;
        mTmpFilterValue = mMapFilterValue;

        mTblFilterOptions.setVisibility(View.GONE);
        mMapFragment.hideMapItemContainerFragment();
        mMapFilterDialogOpen = false;
    }

    //endregion

    //region Fragment related methods

    private void showMapFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.frmMain, mMapFragment, "fragmentMap");
        ft.add(R.id.frmMain, mListFragment, "fragmentList"); // added, but will not be show at first
        ft.show(mMapFragment);
        ft.hide(mListFragment);
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        ft.commit();
        mMapShown = true;
    }

    //endregion

    TextWatcher mSearchTextListener = new TextWatcher() {
        Timer timer;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i(TAG, "mEdtSearchBox beforeTextChanged");
        }

        @Override
        public void onTextChanged(final CharSequence s, int start, int before, int count) {
            Log.i(TAG, "mEdtSearchBox onTextChanged");
            if (timer != null) {
                timer.cancel();
            }
        }

        @Override
        public void afterTextChanged(final Editable s) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            searchAtmOrBranch();
                        }
                    });

                }
            }, 200);
        }
    };
    // region Methods called by the fragment

    public ArrayList<Channel> filterChannels() {
        return null;
    }

    //endregion

    //region Activity related methods


    @Override
    public void initViews() {
        mTblFilterOptions = (TableLayout) findViewById(R.id.tblFilterOptions);

        mRowBranch = (TableRow) findViewById(R.id.rowBranch);
        mRowATM = (TableRow) findViewById(R.id.rowATM);
        mRowSpecial = (TableRow) findViewById(R.id.rowSpecial);
        mRowConcept = (TableRow) findViewById(R.id.rowConcept);
        mRowDisabled = (TableRow) findViewById(R.id.rowDisabled);

        mImgBranchIcon = (ImageView) findViewById(R.id.imgBranchIcon);
        mImgATMIcon = (ImageView) findViewById(R.id.imgATMIcon);
        mImgSpecialIcon = (ImageView) findViewById(R.id.imgSpecialIcon);
        mImgConceptIcon = (ImageView) findViewById(R.id.imgConceptIcon);
        mImgDisabledIcon = (ImageView) findViewById(R.id.imgDisabledIcon);

        mTxtBranch = (TextView) findViewById(R.id.txtBranch);
        mTxtATM = (TextView) findViewById(R.id.txtATM);
        mTxtSpecial = (TextView) findViewById(R.id.txtSpecial);
        mTxtConcept = (TextView) findViewById(R.id.txtConcept);
        mTxtDisabled = (TextView) findViewById(R.id.txtDisabled);

        mImgBranchTick = (ImageView) findViewById(R.id.imgBranchTick);
        mImgATMTick = (ImageView) findViewById(R.id.imgATMTick);
        mImgSpecialTick = (ImageView) findViewById(R.id.imgSpecialTick);
        mImgConceptTick = (ImageView) findViewById(R.id.imgConceptTick);
        mImgDisabledTick = (ImageView) findViewById(R.id.imgDisabledTick);
    }

    private void initCallbacks() {
        mRowBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTmpFilterValue ^= AtmBranchListFilter.BRANCH_SELECTED;

                if (0 < (mTmpFilterValue & AtmBranchListFilter.BRANCH_SELECTED)) {
                    mTxtBranch.setTextColor(getResources().getColor(R.color.ing_orange));
                    mImgBranchTick.setVisibility(View.VISIBLE);
                } else {
                    mTxtBranch.setTextColor(getResources().getColor(R.color.black_gray));
                    mImgBranchTick.setVisibility(View.GONE);
                }
            }
        });
        mRowATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTmpFilterValue ^= AtmBranchListFilter.ATM_SELECTED;

                if (0 < (mTmpFilterValue & AtmBranchListFilter.ATM_SELECTED)) {
                    mTxtATM.setTextColor(getResources().getColor(R.color.ing_orange));
                    mImgATMTick.setVisibility(View.VISIBLE);
                } else {
                    mTxtATM.setTextColor(getResources().getColor(R.color.black_gray));
                    mImgATMTick.setVisibility(View.GONE);
                }
            }
        });
        mRowSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTmpFilterValue ^= AtmBranchListFilter.SPECIAL_SELECTED;

                if (0 < (mTmpFilterValue & AtmBranchListFilter.SPECIAL_SELECTED)) {
                    mTxtSpecial.setTextColor(getResources().getColor(R.color.ing_orange));
                    mImgSpecialTick.setVisibility(View.VISIBLE);
                } else {
                    mTxtSpecial.setTextColor(getResources().getColor(R.color.black_gray));
                    mImgSpecialTick.setVisibility(View.GONE);
                }
            }
        });
        mRowConcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTmpFilterValue ^= AtmBranchListFilter.CONCEPT_SELECTED;

                if (0 < (mTmpFilterValue & AtmBranchListFilter.CONCEPT_SELECTED)) {
                    mTxtConcept.setTextColor(getResources().getColor(R.color.ing_orange));
                    mImgConceptTick.setVisibility(View.VISIBLE);
                } else {
                    mTxtConcept.setTextColor(getResources().getColor(R.color.black_gray));
                    mImgConceptTick.setVisibility(View.GONE);
                }
            }
        });
        mRowDisabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTmpFilterValue ^= AtmBranchListFilter.DISABLED_SELECTED;

                if (0 < (mTmpFilterValue & AtmBranchListFilter.DISABLED_SELECTED)) {
                    mTxtDisabled.setTextColor(getResources().getColor(R.color.ing_orange));
                    mImgDisabledTick.setVisibility(View.VISIBLE);
                } else {
                    mTxtDisabled.setTextColor(getResources().getColor(R.color.black_gray));
                    mImgDisabledTick.setVisibility(View.GONE);
                }
            }
        });

    }

    public void filterMarkers() {
        if (mMapFilterDialogOpen) {
            if (mTmpFilterValue != mMapFilterValue) {
                mMapFilterValue = mTmpFilterValue;
                mListFragment.filterByCriteria("," + mMapFilterValue);
            }
            closeFilterDialog();
        } else {
            openFilterDialog();
        }
    }

    private void searchAtmOrBranch() {
        if (mMapShown) {
            switchtoListFragment();
        }
    }

    private void initMemberVariables() {
        // You can pass getFragmentManager() if you are coding for Android 3.0 or above.
        mFragmentManager = getSupportFragmentManager();

        mMapFragment = new AtmBranchMapFragment();
        mListFragment = new AtmBranchListFragment();

        // initially the filter is off
        mMapFilterValue = AtmBranchListFilter.BRANCH_SELECTED | AtmBranchListFilter.ATM_SELECTED | AtmBranchListFilter.SPECIAL_SELECTED | AtmBranchListFilter.CONCEPT_SELECTED | AtmBranchListFilter.DISABLED_SELECTED;
        mTmpFilterValue = mMapFilterValue;

    }

    //endregion

    //region Service Call

    private void saveUserLocation(Location location) {
        Log.e(TAG, "saveLocation called. lat:" + location.getLatitude() + " lng:" + location.getLongitude());

        try {

        } catch (Exception ignored) {
            Log.e(TAG, "saveLocation UnsupportedEncodingException");
        }
    }

    public void onListAtmBranch(final ArrayList<Channel> atmBranchList) {
        if (atmBranchList != null && atmBranchList.size() > 0) {
            Log.d(TAG, "mFilteredChannels was set. Total " + atmBranchList.size() + " channels");
            mMapFragment.setChannels(atmBranchList);
            if (mMyLocation == null || mMyLocation.isDefault()) {
                orderByName(atmBranchList);
            } else {
                calculateDistance(atmBranchList);
                orderByDistance(atmBranchList);
            }

            mListFragment.setChannels(atmBranchList);
            if (!MapController.GetInstance().isLocationServicesEnabled()) {
                mMapFragment.displayLocationWarning();
            }

        } else {
            Log.e(TAG, "server returned nothing :(");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void calculateDistance(Collection<Channel> items) {
        for (Channel channel : items) {

            if (mMyLocation != null) {
                android.location.Location destination = new android.location.Location("");

                try {
                    destination.setLatitude(Double.parseDouble(channel.getLatitude().trim()));
                    destination.setLongitude(Double.parseDouble(channel.getLongitude().trim()));
                    double distance = ((double) destination.distanceTo(mMyLocation));

                    channel.setDistanceToPoint(distance);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private void orderByName(List<Channel> list) {

        if (list == null)
            return;

        if (list.size() > 0) {
            Collections.sort(list, new Comparator<Channel>() {
                @Override
                public int compare(Channel lhs, Channel rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        }
    }


    private void orderByDistance(List<Channel> list) {

        if (list == null) {
            return;
        } else if (list.size() > 0) {
            Collections.sort(list, new Comparator<Channel>() {
                @Override
                public int compare(Channel lhs, Channel rhs) {
                    return (int) (lhs.getDistanceToPoint() - rhs.getDistanceToPoint());
                }
            });

        }
    }

    public void showWaitingDialog() {
        showProgressDialog(getString(R.string.global_waiting), false, null);
    }

    @Override
    public void dismissWaitingDialog() {
        super.dismissProgressDialog();
    }

    //endregion

    //region ActionBar

    //region ActionBar

    @Override
    public void showError(String errorMessage) {
        createAlertDialog(getString(R.string.global_warning), errorMessage, getString(R.string.global_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }, false).show();
    }

    @Override
    public void onNetworkError() {
        createAlertDialog(getString(R.string.global_warning), getString(R.string.global_alertHttpError), getString(R.string.global_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        }, false).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                myIntent = new Intent(MapActivity.this, MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(myIntent);
                finish();
                break;
            case R.id.action_map_view:


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switchtoListFragment();
                    }
                });
                break;
            case R.id.action_list_view:

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switchtoMapFragment();
                    }
                });
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        this.menu = menu;
        menuItemListView = this.menu.findItem(R.id.action_list_view);
        menuItemMapView = this.menu.findItem(R.id.action_map_view);
        return true;
    }

    public Boolean getIsFilterDialogVisible() {
        return isFilterDialogVisible;
    }

    public MenuItem getMenuItemMapView() {
        return menuItemMapView;
    }

    public MenuItem getMenuItemListView() {
        return menuItemListView;
    }

    public Menu getMenu() {
        return menu;
    }

    public AtmBranchMapFragment getmMapFragment() {
        return mMapFragment;
    }

    public ActiveFragment getActiveFragment() {
        return activeFragment;
    }

    public enum ActiveFragment {
        MAP,
        LIST
    }

    //endregion

    //region Utils

    //endregion
}