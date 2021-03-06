package com.exmaple.jarvis.running.AsyncTask;

import android.text.TextUtils;
import android.util.Log;

import com.exmaple.jarvis.running.Model.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryUtil {
    private static final String LOG_TAG = "NewsUtil";

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    public static Object postHistory(String requestUrl, String duration) {
        URL url = createUrl(requestUrl);

        Object jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url, duration);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    public static Object getHistory(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        jsonResponse = fetchHistoryList(url);

        ArrayList<History> historyList = extractFeatureFromJson(jsonResponse);

        return historyList;
    }

    public static Object deleteHistory(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        jsonResponse = fetchHistoryById(url);

        return jsonResponse;
    }


    public static String putHistory(String requestUrl, RequestBody formBody) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        jsonResponse = putHistoryById(url, formBody);

        return jsonResponse;
    }

    private static String putHistoryById(URL url, RequestBody formBody) {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .put(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to get data.");
            return null;
        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static ArrayList<History> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        ArrayList<History> historyList = new ArrayList<>();

        try {
            JSONArray responseArray = new JSONArray(jsonResponse);

            for (int i =  0; i < responseArray.length(); i++) {
                JSONObject currentHistory = responseArray.getJSONObject(i);

                String id = currentHistory.optString("_id");

                String duration = currentHistory.optString("duration");

                String createdAt = currentHistory.optString("created_at");

                String original = currentHistory.optString("original");

                String destination = currentHistory.optString("destination");

                historyList.add(new History(id, duration, createdAt, original, destination));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    private static Object makeHttpRequest(URL url, String duration) throws IOException, JSONException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("duration", duration)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            jsonResponse = response.body().string();
        }

        // process response (string -> json)
        Log.e("ResultObject", jsonResponse);
        Object resultObject = new JSONObject(jsonResponse);
        return resultObject;

    }

    private static String fetchHistoryList(URL url) {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to get data.");
            return null;
        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String fetchHistoryById(URL url) {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to get data.");
            return null;
        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
