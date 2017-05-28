package com.jbajic.todoo.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jbajic.todoo.helpers.CustomJSONAuthObject;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.helpers.VolleyRequestQueue;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.models.User;
import com.jbajic.todoo.utilis.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jure on 14.05.17..
 */

public class APIService {

    private static APIService APIService;
    private Activity activity;
    private VolleyRequestQueue volleyRequestQueue;

    public static synchronized APIService getInstance(Activity activity) {
        if (APIService == null) {
            APIService = new APIService(activity);
        }
        return APIService;
    }

    private APIService(Activity activity) {
        this.activity = activity;
    }

    public void login(final String email, final String password, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_EMAIL, email);
            jsonObject.put(AppConstants.KEY_PASSWORD, password);
        } catch (JSONException e) {
            Log.e("ERROR Login", e.getMessage());
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String jwt = response.getString(AppConstants.KEY_JWT);

                    if (jwt != null) {
                        SharedPreferences sharedPreferences = activity.getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(AppConstants.KEY_JWT, jwt);
                        editor.putString(AppConstants.KEY_EMAIL, email);
                        editor.putString(AppConstants.KEY_PASSWORD, password);
                        editor.commit();
                        requestListener.finished("success");
                    } else {
                        requestListener.failed("Login failed");
                    }
                } catch (Exception e) {
                    Log.e("ERROR Login2", e.getMessage());
                    requestListener.failed(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR Login", String.valueOf(error));
                requestListener.failed(error.getMessage());
            }
        });

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void register(final String email, final String password, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_EMAIL, email);
            jsonObject.put(AppConstants.KEY_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_REGISTER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Integer status = response.getInt(AppConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Email is taken");
                    } else {
                        requestListener.finished("success");
                    }
                } catch (Exception e) {
                    requestListener.failed(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.failed(error.getMessage());
            }
        });

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void synchronizeUsers(final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(activity);

        CustomJSONAuthObject jsonAuthObject = new CustomJSONAuthObject(Request.Method.GET,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_USERS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getInt(AppConstants.KEY_STATUS) == 1) {
                        JSONArray users = response.getJSONArray(AppConstants.KEY_USERS);
                        Integer usersLength = users.length();
                        if(usersLength != 0) {
                            for (int i = 0; i < usersLength; ++i) {
                                JSONObject userObject  = (JSONObject) users.get(i);
                                databaseHelper.addUser(new User(
                                        userObject.getLong(AppConstants.KEY_ID),
                                        userObject.getString(AppConstants.KEY_EMAIL),
                                        userObject.getString(AppConstants.KEY_USERNAME),
                                        userObject.getString(AppConstants.KEY_FIRST_NAME),
                                        userObject.getString(AppConstants.KEY_LAST_NAME),
                                        userObject.getString(AppConstants.KEY_ADDRESS),
                                        userObject.getString(AppConstants.KEY_CITY),
                                        userObject.getBoolean(AppConstants.KEY_IS_ME)
                                ));
                                Log.e("ID", String.valueOf(userObject.getLong(AppConstants.KEY_ID)));
                                Log.e("EMAIL", userObject.getString(AppConstants.KEY_EMAIL));
                                Log.e("FIRST NAME", userObject.getString(AppConstants.KEY_FIRST_NAME));
                                Log.e("LAST NAME", userObject.getString(AppConstants.KEY_LAST_NAME));
                                Log.e("USERNAME", userObject.getString(AppConstants.KEY_USERNAME));
                                Log.e("ADDRESS", userObject.getString(AppConstants.KEY_CITY));
                                Log.e("CITY", userObject.getString(AppConstants.KEY_ADDRESS));
                            }
                        }
                    } else {
                        requestListener.failed("Server error");
                        Log.e("ERROR SYNCUS", "SERVEr error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR SYNCUS", e.getMessage());
                    requestListener.failed(e.getMessage());
                }
                requestListener.finished("Users updated");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    requestListener.failed(error.getMessage());
                    Log.e("ERROR SYNCUS2", error.getMessage());
                } else {
                    Log.e("ERROR SYNCUS3", "FAILED");
                    requestListener.failed("Request failed");
                }
            }
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonAuthObject);
    }

    public void synchronizeProjects(final RequestListener requestListener) {

    }

}
