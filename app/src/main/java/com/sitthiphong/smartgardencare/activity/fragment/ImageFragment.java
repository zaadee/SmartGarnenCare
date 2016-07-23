package com.sitthiphong.smartgardencare.activity.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sitthiphong.smartgardencare.R;
import com.sitthiphong.smartgardencare.bean.ImageBean;
import com.sitthiphong.smartgardencare.bean.StatusBean;
import com.sitthiphong.smartgardencare.core.MagScreen;
import com.sitthiphong.smartgardencare.listener.ActionListener;
import com.sitthiphong.smartgardencare.provider.SimpleDateProvider;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {
    private String TAG = "ImageFragment";
    private View rootView;
    private ImageView imageGarden;
    private CoordinatorLayout rootLayout;
    private ProgressBar progressBar;
    private TextView exception;
    private TextView dateTime;
    private TextView timeFrequencyValue;
    private ImageView setTimeFrequency;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int ftPubIM;

    private ActionListener actionListener = new ActionListener();
    private OnFragmentInteractionListener mListener;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance() {
        ImageFragment fragment = new ImageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        sharedPreferences = getActivity().getSharedPreferences("Details", getActivity().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ftPubIM = sharedPreferences.getInt("ftPubIM",1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.i(TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_image, container, false);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        MagScreen magScreen = new MagScreen(getActivity(),metrics);

        imageGarden = (ImageView)rootView.findViewById(R.id.imGarden);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                magScreen.getWidthGardenImage(),magScreen.getHeightGardenImage());
        imageGarden.setLayoutParams(param);

        imageGarden.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.bg_garden_im));
        setTimeFrequency = (ImageView)rootView.findViewById(R.id.btn_setting_send_im_garden);
        timeFrequencyValue = (TextView)rootView.findViewById(R.id.setting_value_im_garden);


        dateTime = (TextView)rootView.findViewById(R.id.time_im_garden);

        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.containImage);
        rootLayout.setVisibility(View.GONE);

        exception = (TextView)rootView.findViewById(R.id.exceptionImage);
        exception.setVisibility(View.GONE);

        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBarImage);
        progressBar.setVisibility(View.VISIBLE);

        setTimeFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title(getResources().getString(R.string.frequencySendImage))
                        .items(R.array.ftPubIM_IRDArray)
                        .itemsCallbackSingleChoice(getWhichFromValueSendImage(ftPubIM),
                                new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        int value = getValueSendImageFromWhich(which);

                                        if(value != (sharedPreferences.getInt("ftPubIM",1))){
                                            ftPubIM = value;
                                            timeFrequencyValue.setText(text);
                                            Log.e(TAG,"publish data to net pie");
                                        }
                                        else{
                                        }
                                        return true;
                                    }
                                })
                        .positiveText(R.string.choose)
                        .negativeText(R.string.cancel)
                        .show();
            }
        });


        setActionListener();




        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        actionListener.onRequestUpdateImage.onRequestUpdateImage();

    }
    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
    }
    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();

    }
    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
        mListener = null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void setActionListener(){
        actionListener.setOnException(new ActionListener.OnException() {
            @Override
            public void onException(String error) {
                Log.e(TAG,"onException");
                rootLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                exception.setText(error);
                exception.setVisibility(View.VISIBLE);
            }
        });
        actionListener.setOnUpdateImage(new ActionListener.OnUpdateImage() {
            @Override
            public void onUpdateImage(StatusBean statusBean, ImageBean imageBean) {
                if(statusBean.getStatus() == getActivity().getResources().getInteger(R.integer.IS_CONNECT_NETPIE)){
                    //ถ้าคอนเน็คเสร็จ เมื่อขออัพเดทไปแล้ว แต่มันยังเสือกไม่มี จะไปโหลดข้อมผผูลโดยใช้ Asynctask  พอได้ข้อมูล
                    // เมื่อได้ข้อม฿ลมา ก็จะนำข้อมูลไป update view และส่งข้อมูลไปเก็ยที่ mainactivity
                    progressBar.setVisibility(View.GONE);
                    exception.setVisibility(View.GONE);
                    imageGarden.setImageBitmap(imageBean.getBitmap());

                    dateTime.setText(SimpleDateProvider.getInstance()
                            .format(new Date(imageBean.getTimeStamp()*1000)));
                    rootLayout.setVisibility(View.VISIBLE);

                }
                else if (statusBean.getStatus() == getActivity().getResources().getInteger(R.integer.ERROR)){
                    rootLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    exception.setText(statusBean.getException());
                    exception.setVisibility(View.VISIBLE);

                }
                else if(statusBean.getStatus() == getActivity().getResources().getInteger(R.integer.NO_INTERNET)){

                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public int getValueSendImageFromWhich(int which){
        if(which == 0){
            return 1;
        }
        else if(which == 1){
            return 2;
        }
        else if(which == 2){
            return 3;
        }
        else if(which == 3){
            return 6;
        }
        else if(which == 4){
            return 12;
        }
        else if(which == 5){
            return 24;
        }else {
            return 1;
        }
    }
    public int getWhichFromValueSendImage(int insertDataValue){
        if(insertDataValue == 1){
            return 0;
        }
        else if(insertDataValue == 2){
            return 1;
        }
        else if(insertDataValue == 3){
            return 2;
        }
        else if(insertDataValue == 6){
            return 3;
        }
        else if(insertDataValue == 12){
            return 4;
        }
        else if(insertDataValue == 24){
            return 5;
        }else {
            return 0;
        }
    }

}