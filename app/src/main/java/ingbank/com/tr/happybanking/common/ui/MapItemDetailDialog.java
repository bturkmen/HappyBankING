package ingbank.com.tr.happybanking.common.ui;

//import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.ingbanktr.ingmobil.R;
//import com.ingbanktr.ingmobil.activity.map.MapActivity;
//import com.ingbanktr.ingmobil.activity.map.fragment.MapFragment;

/**
 * Created by sezercansezer on 09/04/15.
 */
public class MapItemDetailDialog extends Dialog {
    public MapItemDetailDialog(final Context context) {
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
    }

//    private TextView mTxtName, mTxtAddress, mTxtPhoneNumber, mTxtDirection, mTxtAppointment;
//    private ImageView mImgName, mImgPhoneNumber;
//    private Context mContext;
//    private String mPhoneNumber;
//
//    public MapItemDetailDialog(final Context context) {
//        super(context,android.R.style.Theme_Translucent_NoTitleBar);
//        mContext = context;
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.dialog_map_item_detail, null);
//
//        initViews(view);
//
//        mTxtPhoneNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle(R.string.global_warning)
//                        .setMessage(R.string.pgMapDetail_callBranchMessage)
//                        .setPositiveButton(R.string.global_confirm, new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent intent = new Intent(Intent.ACTION_CALL);
//                                intent.setData(Uri.parse("tel:"+mPhoneNumber));
//                                context.start(intent);
//                            }
//                        })
//                        .setNegativeButton(R.string.global_cancel, new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).create();
//                builder.show();
//            }
//        });
//        mTxtDirection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((MapActivity) context).getDirection();
//                dismiss();
//            }
//        });
//        mTxtAppointment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //this is temporarily here
//                ((MapFragment)MapActivity.mFragmentManager
//                        .findFragmentByTag("fragmentMap")).clearRoute();
//            }
//        });
//
//        setCancelable(true);
//        setCanceledOnTouchOutside(true);
//
//        this.setContentView(view);
//    }
//
//    private void initViews(View view) {
//        mImgName = (ImageView) view.findViewById(R.id.imgName);
//        mImgPhoneNumber = (ImageView) view.findViewById(R.id.imgPhoneNumber);
//        mTxtName = (TextView) view.findViewById(R.id.txtName);
//        mTxtAddress = (TextView) view.findViewById(R.id.txtAddress);
//        mTxtPhoneNumber = (TextView) view.findViewById(R.id.txtPhoneNumber);
//        mTxtDirection = (TextView) view.findViewById(R.id.txtDirection);
//        mTxtAppointment = (TextView) view.findViewById(R.id.txtAppointment);
//    }
//
//
//    public void setMapDetail(int ivId, String name, String address, String phoneNumber, boolean isCallable) {
//        mImgName.setImageResource(ivId);
//        mTxtName.setText(name);
//        mTxtAddress.setText(address);
//
//        if (null != phoneNumber && true == isCallable) {
//            mPhoneNumber = String.format("+90%s", phoneNumber);
//            mTxtPhoneNumber.setText(String.format("Tel: +90 (%s) %s %s %s", phoneNumber.substring(0, 3), phoneNumber.substring(3,6), phoneNumber.substring(6,8), phoneNumber.substring(8,10)));
//            mImgPhoneNumber.setVisibility(View.VISIBLE);
//            mTxtPhoneNumber.setVisibility(View.VISIBLE);
//        } else {
//            mImgPhoneNumber.setVisibility(View.GONE);
//            mTxtPhoneNumber.setVisibility(View.GONE);
//        }
//    }
}
