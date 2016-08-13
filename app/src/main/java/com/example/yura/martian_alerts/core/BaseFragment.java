package com.example.yura.martian_alerts.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class BaseFragment extends Fragment {


    protected View mView ;
    protected Context mContext;

    public String LOG_TAG ;

    protected String errorMessage;

    public BaseFragment() {
        // Required empty public constructor
        LOG_TAG = getClass().getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        mContext = null;

        super.onPause();
    }

    @Override
    public void onResume() {
        mContext = getContext();
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity)getActivity()).setCurrentFragment(this);
    }

    protected void hideKeyboard(){
        if(mView == null)
            return;
        InputMethodManager imm = (InputMethodManager) mView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
        }
        mView.requestFocus();
    }

    public boolean onBackPress(){
        hideKeyboard();
        return true ;
    }

    protected void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected void showLoadingView(){
        ((BaseActivity)getActivity()).showLoadingView();
    }

    protected void hideLoadingView(){
        ((BaseActivity)getActivity()).hideLoadingView();
    }

    /*protected boolean checkInternet(){
        boolean result = CommonUtilities.Internet.haveInternet(mContext);
        if(!result){
            showToast(getString(R.string.error_no_internet));
        }
        return result;
    }*/

}
