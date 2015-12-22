package com.zaidhuda.pollease.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zaidhuda.pollease.Objects.Poll;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Zaid on 20/12/2015.
 */
public class GETPoll extends AsyncTask<String, Void, String> {
    private Activity activity;
    private String POLLS_URL;
    private String requestUrl;
    private String jsonResult;
    private Poll poll;
    private ProgressDialog progressDialog;
    private OnGETPollListener mListener;
    private int responseCode;


    public GETPoll(String polls_url, String requestUrl, Activity activity) {
        POLLS_URL = polls_url;
        this.activity = activity;
        this.requestUrl = requestUrl;
        this.execute(requestUrl);
        mListener = (OnGETPollListener) activity;
    }

    private void setPoll(Poll poll) {
        this.poll = poll;
        mListener.setPoll(poll);
    }

    public void detachListener() {
        mListener = null;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream response = conn.getInputStream();
            jsonResult = inputStreamToString(response).toString();
            responseCode = conn.getResponseCode();
        } catch (ProtocolException e) {
            e.printStackTrace();
            showErrorToast("Error in retrieving poll");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            showErrorToast("Error in retrieving poll");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            showErrorToast("Error in retrieving poll");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorToast("Error in retrieving poll");
        }
        return null;
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            showErrorToast("Error in retrieving poll");
        }
        return answer;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity, "", "Retrieving data, please wait", false);
    }

    @Override
    protected void onPostExecute(String result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (responseCode == HttpURLConnection.HTTP_OK && requestUrl.startsWith(POLLS_URL)) {
            ProcessPollJSON();
        }
    }

    public void ProcessPollJSON() {
        try {
            JSONObject jPoll = new JSONObject(jsonResult);
            setPoll(new Gson().fromJson(jPoll.toString(), Poll.class));
            Toast.makeText(activity, "Poll retrieved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showErrorToast("Error retrieving poll");
        }
    }

    private void showErrorToast(final String msg) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnGETPollListener {
        void setPoll(Poll poll);
    }
}