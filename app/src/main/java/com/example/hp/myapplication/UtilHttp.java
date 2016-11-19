package com.example.hp.myapplication;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UtilHttp {
    private static final String TAG = "UtilHttp";
    public static String err;

    public static String doHttpPutBasicAuthentication(Context ctx, String serverUrl, String params) {

        Log.v(TAG, "HTTPPUT:" + serverUrl);
        String result = null;
        err = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        OutputStreamWriter out = null;
        String json = params.substring(0,(params.indexOf('}')+1));
        String basicAuth = params.substring(params.indexOf('}')+1);
        Log.d("Hihi", "basic Auth is : " + basicAuth);
        try {
            int TIMEOUT_MILLISEC = 15000;

            conn = (HttpURLConnection) new URL(serverUrl).openConnection();
            conn.setRequestMethod("PUT");
            conn.setConnectTimeout(TIMEOUT_MILLISEC);
            conn.setReadTimeout(TIMEOUT_MILLISEC);
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty ("Authorization", basicAuth);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(0);


            if (json != null) {
                out = new OutputStreamWriter(conn.getOutputStream());
                out.write(json);
                out.flush();
            }


            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.v(TAG, "Return code = " + responseCode);
            if (responseCode == 200 || responseCode == 201) {
                in = new BufferedInputStream(conn.getInputStream());
                String encoding = conn.getContentEncoding() == null ? "UTF-8"
                        : conn.getContentEncoding();
                result = IOUtils.toString(in, encoding);
            } else {
                String responseMsg = conn.getResponseMessage();
                if (responseMsg != null) {
                    Log.v(TAG, "Response message = " + responseMsg);
                    err = responseCode + ":" + responseMsg;
                } else {
                    Log.v(TAG, "Response message is null");
                    err = responseCode + ":"
                            + ctx.getString(R.string.responseisnull);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            err = e.getMessage();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {

                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        if (result != null) {
            Log.v(TAG, result);
        } else {
            Log.v(TAG, "Result is null");
        }
        return result;

    }

    public static String doHttpGet(Context ctx, String serverUrl) {
        Log.v(TAG, "HTTPGET:" + serverUrl);
        String result = null;
        err = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            int TIMEOUT_MILLISEC = 15000;

            conn = (HttpURLConnection) new URL(serverUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MILLISEC);
            conn.setReadTimeout(TIMEOUT_MILLISEC);
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);

            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.v(TAG, "Return code = " + responseCode);
            if (responseCode == 200 || responseCode == 201) {
                in = new BufferedInputStream(conn.getInputStream());
                String encoding = conn.getContentEncoding() == null ? "UTF-8"
                        : conn.getContentEncoding();
                result = IOUtils.toString(in, encoding);
            }else{
                String responseMsg = conn.getResponseMessage();
                if (responseMsg != null) {
                    Log.v(TAG, "Response message = " + responseMsg);
                    err = responseCode + ":" + responseMsg;
                } else {
                    Log.v(TAG, "Response message is null");
                    err = responseCode + ":"
                            + ctx.getString(R.string.responseisnull);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            err = e.getMessage();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        if (result != null) {
            Log.v(TAG, result);
        } else {
            Log.v(TAG, "Result is null");
        }
        return result;
    }

    public static String doHttpPost(Context ctx, String serverUrl) {
        Log.v(TAG, "HTTPPOST:" + serverUrl);
        String result = null;
        err = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        OutputStreamWriter out = null;
        try {
            int TIMEOUT_MILLISEC = 15000;

            conn = (HttpURLConnection) new URL(serverUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT_MILLISEC);
            conn.setReadTimeout(TIMEOUT_MILLISEC);
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(0);

            /*
            if (params != null) {
                out = new OutputStreamWriter(conn.getOutputStream());
                out.write(params);
                out.flush();
            }

            */
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.v(TAG, "Return code = " + responseCode);
            if (responseCode == 200 || responseCode == 201) {
                in = new BufferedInputStream(conn.getInputStream());
                String encoding = conn.getContentEncoding() == null ? "UTF-8"
                        : conn.getContentEncoding();
                result = IOUtils.toString(in, encoding);
            } else {
                String responseMsg = conn.getResponseMessage();
                if (responseMsg != null) {
                    Log.v(TAG, "Response message = " + responseMsg);
                    err = responseCode + ":" + responseMsg;
                } else {
                    Log.v(TAG, "Response message is null");
                    err = responseCode + ":"
                            + ctx.getString(R.string.responseisnull);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            err = e.getMessage();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        if (result != null) {
            Log.v(TAG, result);
        } else {
            Log.v(TAG, "Result is null");
        }
        return result;
    }

    public static String doHttpPostJson(Context ctx, String serverUrl, String params) {
        Log.v(TAG, "HTTPPOST:" + serverUrl);
        String result = null;
        err = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        OutputStreamWriter out = null;
        try {
            int TIMEOUT_MILLISEC = 15000;

            conn = (HttpURLConnection) new URL(serverUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT_MILLISEC);
            conn.setReadTimeout(TIMEOUT_MILLISEC);
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);


            if (params != null) {
                out = new OutputStreamWriter(conn.getOutputStream());
                out.write(params);
                out.flush();
            }


            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.v(TAG, "Return code = " + responseCode);
            if (responseCode == 200 || responseCode == 400 ||responseCode == 201) {
                in = new BufferedInputStream(conn.getInputStream());
                String encoding = conn.getContentEncoding() == null ? "UTF-8"
                        : conn.getContentEncoding();
                result = IOUtils.toString(in, encoding);
            } else {
                if(conn.getContentType().equals("application/json") || conn.getContentType().equals("application/json;charset=UTF-8")){
                    in = new BufferedInputStream(conn.getErrorStream());
                    String encoding = conn.getContentEncoding() == null ? "UTF-8"
                            : conn.getContentEncoding();
                    String errorMsg = IOUtils.toString(in, encoding);
                    try {

                        JSONObject jso = new JSONObject(errorMsg);

                        err = jso.getString("message");
                    }catch(JSONException e){
                        e.printStackTrace();
                        err = e.getMessage();
                    }

                }else{

                    String responseMsg = conn.getResponseMessage();
                    if (responseMsg != null) {
                        Log.v(TAG, "Response message = " + responseMsg);
                        err = responseCode + ":" + responseMsg;
                    } else {
                        Log.v(TAG, "Response message is null");
                        err = responseCode + ":"
                                + ctx.getString(R.string.responseisnull);

                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            err = e.getMessage();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        if (result != null) {
            Log.v(TAG, result);
        } else {
            Log.v(TAG, "Result is null");
        }
        return result;
    }

    public static String doHttpGetBasicAuthentication(Context ctx, String serverUrl, String params) {

        String basicAuth = params;
        Log.v(TAG, "HTTPGET:" + serverUrl);
        String result = null;
        err = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            int TIMEOUT_MILLISEC = 15000;

            conn = (HttpURLConnection) new URL(serverUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MILLISEC);
            conn.setReadTimeout(TIMEOUT_MILLISEC);
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty ("Authorization", basicAuth);
            conn.setDoInput(true);

            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.v(TAG, "Return code = " + responseCode);
            if (responseCode == 200 || responseCode == 201) {
                in = new BufferedInputStream(conn.getInputStream());
                String encoding = conn.getContentEncoding() == null ? "UTF-8"
                        : conn.getContentEncoding();
                result = IOUtils.toString(in, encoding);
            } else{
                String responseMsg = conn.getResponseMessage();
                if (responseMsg != null) {
                    Log.v(TAG, "Response message = " + responseMsg);
                    err = responseCode + ":" + responseMsg;
                } else {
                    Log.v(TAG, "Response message is null");
                    err = responseCode + ":"
                            + ctx.getString(R.string.responseisnull);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            err = e.getMessage();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        if (result != null) {
            Log.v(TAG, result);
        } else {
            Log.v(TAG, "Result is null");
        }
        return result;
    }

    public static String doHttpPostBasicAuthentication(Context ctx, String serverUrl, String params) {
        Log.v(TAG, "HTTPPOST:" + serverUrl);
        String result = null;
        err = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        OutputStreamWriter out = null;
        String json = params.substring(0,(params.indexOf('}')+1));
        String basicAuth = params.substring(params.indexOf('}')+1);
        try {
            int TIMEOUT_MILLISEC = 15000;

            conn = (HttpURLConnection) new URL(serverUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT_MILLISEC);
            conn.setReadTimeout(TIMEOUT_MILLISEC);
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty ("Authorization", basicAuth);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(0);


            if (json != null) {
                out = new OutputStreamWriter(conn.getOutputStream());
                out.write(json);
                out.flush();
            }


            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.v(TAG, "Return code = " + responseCode);
            if (responseCode == 200 ||responseCode == 201) {
                in = new BufferedInputStream(conn.getInputStream());
                String encoding = conn.getContentEncoding() == null ? "UTF-8"
                        : conn.getContentEncoding();
                result = IOUtils.toString(in, encoding);
            } else {
                if(conn.getContentType().equals("application/json") || conn.getContentType().equals("application/json;charset=UTF-8")){
                    in = new BufferedInputStream(conn.getErrorStream());
                    String encoding = conn.getContentEncoding() == null ? "UTF-8"
                            : conn.getContentEncoding();
                    String errorMsg = IOUtils.toString(in, encoding);
                    try {

                        JSONObject jso = new JSONObject(errorMsg);

                        err = jso.getString("message");
                    }catch(JSONException e){
                        e.printStackTrace();
                        err = e.getMessage();
                    }

                }else{

                    String responseMsg = conn.getResponseMessage();
                    if (responseMsg != null) {
                        Log.v(TAG, "Response message = " + responseMsg);
                        err = responseCode + ":" + responseMsg;
                    } else {
                        Log.v(TAG, "Response message is null");
                        err = responseCode + ":"
                                + ctx.getString(R.string.responseisnull);

                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            err = e.getMessage();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        if (result != null) {
            Log.v(TAG, result);
        } else {
            Log.v(TAG, "Result is null");
        }
        return result;
    }


    public static String doHttpDeleteBasicAuthenticaion(Context ctx, String serverUrl, String params) {
        String basicAuth = params;
        Log.v(TAG, "HTTPDELETE:" + serverUrl);
        String result = null;
        err = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            int TIMEOUT_MILLISEC = 15000;

            conn = (HttpURLConnection) new URL(serverUrl).openConnection();
            conn.setRequestMethod("DELETE");
            conn.setConnectTimeout(TIMEOUT_MILLISEC);
            conn.setReadTimeout(TIMEOUT_MILLISEC);
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty ("Authorization", basicAuth);
            conn.setDoInput(true);

            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.v(TAG, "Return code = " + responseCode);
            if (responseCode == 200 || responseCode == 201) {
                in = new BufferedInputStream(conn.getInputStream());
                String encoding = conn.getContentEncoding() == null ? "UTF-8"
                        : conn.getContentEncoding();
                result = IOUtils.toString(in, encoding);
            } else{
                if(conn.getContentType().equals("application/json") || conn.getContentType().equals("application/json;charset=UTF-8")){
                    in = new BufferedInputStream(conn.getErrorStream());
                    String encoding = conn.getContentEncoding() == null ? "UTF-8"
                            : conn.getContentEncoding();
                    String errorMsg = IOUtils.toString(in, encoding);
                    try {

                        JSONObject jso = new JSONObject(errorMsg);

                        err = jso.getString("message");
                    }catch(JSONException e){
                        e.printStackTrace();
                        err = e.getMessage();
                    }

                }else{

                    String responseMsg = conn.getResponseMessage();
                    if (responseMsg != null) {
                        Log.v(TAG, "Response message = " + responseMsg);
                        err = responseCode + ":" + responseMsg;
                    } else {
                        Log.v(TAG, "Response message is null");
                        err = responseCode + ":"
                                + ctx.getString(R.string.responseisnull);

                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            err = e.getMessage();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        if (result != null) {
            Log.v(TAG, result);
        } else {
            Log.v(TAG, "Result is null");
        }
        return result;
    }
}
