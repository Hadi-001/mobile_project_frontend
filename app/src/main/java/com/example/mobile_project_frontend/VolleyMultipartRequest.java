package com.example.mobile_project_frontend;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Base64;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    private final Map<String, String> mHeaders = new HashMap<>();
    private final Map<String, DataPart> mByteData = new HashMap<>();

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders.putAll(headers);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return buildMultipartBody();
    }

    private final String boundary = "apiclient-" + System.currentTimeMillis();

    private byte[] buildMultipartBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            try {
                for (Map.Entry<String, String> entry : getParams().entrySet()) {
                    bos.write(("--" + boundary + "\r\n").getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
                    bos.write((entry.getValue() + "\r\n").getBytes());
                }
            } catch (AuthFailureError e) {
                throw new RuntimeException(e);
            }

            try {
                for (Map.Entry<String, DataPart> entry : getByteData().entrySet()) {
                    DataPart dataPart = entry.getValue();
                    bos.write(("--" + boundary + "\r\n").getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" +
                            entry.getKey() + "\"; filename=\"" + dataPart.getFileName() + "\"\r\n").getBytes());
                    bos.write(("Content-Type: " + dataPart.getType() + "\r\n\r\n").getBytes());

                    bos.write(dataPart.getContent());
                    bos.write("\r\n".getBytes());
                }
            } catch (AuthFailureError e) {
                throw new RuntimeException(e);
            }

            bos.write(("--" + boundary + "--\r\n").getBytes());
            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract Map<String, String> getParams() throws AuthFailureError;

    protected abstract Map<String, DataPart> getByteData() throws AuthFailureError;

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String fileName, byte[] content) {
            this(fileName, content, "application/octet-stream");
        }

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
