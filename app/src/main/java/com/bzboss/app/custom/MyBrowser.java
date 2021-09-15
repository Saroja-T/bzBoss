package com.bzboss.app.custom;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MyBrowser extends WebViewClient {

    private Activity mActivity;
    private Dialog mProgressDialog;
    private String mainUrl = "";
    String[] nums={};
    String[] array = new String[4];

    public MyBrowser(Activity activity) {
        mActivity = activity;
    }

    public MyBrowser(Activity activity, String mainUrl) {
        this.mainUrl = mainUrl;
        mActivity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        //showProgress();
        return true;
    }

    /*@Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        view.loadUrl("");
        Toast.makeText(mActivity, mActivity.getString(R.string.not_load_data), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
//        Toast.show(mActivity, mActivity.getString(R.string.not_load_data), Toast.ToastType.ALERT);
    }
*/
    @Override
    public void onPageFinished(WebView view, String url) {
        try {
            Log.e("Progress==>", view.getProgress() + "");
            /*if (view.getProgress() == 100) {
                dismissProgress();
            }*/

        } catch (Exception exception) {
            Log.e("Exception==", exception + "");
            exception.printStackTrace();
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
       /* if (mProgressDialog == null) {
            showProgress();
        }*/
    }

   /* private void showProgress() {
        mProgressDialog = getProgressDialog(mActivity);
    }

    private void dismissProgress() {
        mProgressDialog.dismiss();
    }*/
}