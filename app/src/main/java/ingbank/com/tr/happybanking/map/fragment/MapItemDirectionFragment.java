package ingbank.com.tr.happybanking.map.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.ui.BaseFragment;


public class MapItemDirectionFragment extends BaseFragment {

    private static final String ARG_PARAM_IVID = "ivId";
    private static final String ARG_PARAM_NAME = "name";
    private static final String ARG_PARAM_DISTANCE = "distance";
    private static final String ARG_PARAM_REGION = "region";
    private static TextView tvName, tvDistance, tvClose, tvRegion;
    private static ImageView mImgName;
    private OnFragmentInteractionListener mListener;
    private int ivId;
    private String name, distance, region;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapItemDirectionFragment.
     */
    public static MapItemDirectionFragment newInstance(int ivId, String name, String distance, String region) {
        MapItemDirectionFragment fragment = new MapItemDirectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_IVID, ivId);
        args.putString(ARG_PARAM_NAME, name);
        args.putString(ARG_PARAM_DISTANCE, distance);
        args.putString(ARG_PARAM_REGION, region);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_map_item_direction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ivId = getArguments().getInt(ARG_PARAM_IVID);
            name = getArguments().getString(ARG_PARAM_NAME);
            distance = getArguments().getString(ARG_PARAM_DISTANCE);
            region = getArguments().getString(ARG_PARAM_REGION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = super.onCreateView(inflater, container, savedInstanceState) ;

            mImgName = (ImageView) myView.findViewById(R.id.imgName);
            mImgName.setImageResource(ivId);
            tvName = (TextView) myView.findViewById(R.id.tvName);
            tvDistance = (TextView) myView.findViewById(R.id.tvDistance);
            tvRegion = (TextView) myView.findViewById(R.id.tvRegion);
            tvClose = (TextView) myView.findViewById(R.id.tvClose);

            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Parent Fragment of this is MapItemContainerFragment
                    if (getParentFragment() != null)
                        // Parent Fragment of MapItemContainerFragment is MapFragmentV2
                        if (getParentFragment().getParentFragment() != null)
                            ((AtmBranchMapFragment) getParentFragment().getParentFragment()).showSearchAndListComponents();
                }
            });

            tvName.setText(name);
            tvDistance.setText(distance);
            tvRegion.setText(region);

        return myView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (mListener != null)
                mListener = (OnFragmentInteractionListener) activity;
            else
                mListener = null;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
