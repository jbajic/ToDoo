package com.jbajic.todoo.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jbajic.todoo.R;
import com.jbajic.todoo.helpers.CustomJSONAuthObject;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.helpers.VolleyRequestQueue;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.Task;
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
            Log.e("ERROR Login1", e.getMessage());
            e.printStackTrace();
            requestListener.failed(e.getMessage());
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
                        requestListener.failed("Login4 failed");
                    }
                } catch (Exception e) {
                    Log.e("ERROR Login2", e.getMessage());
                    requestListener.failed(activity.getString(R.string.volley_request_fail));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR Login3", String.valueOf(error));
                requestListener.failed(activity.getString(R.string.volley_request_fail));
            }
        });

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void register(final String email, final String password, String fName, String lName, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_EMAIL, email);
            jsonObject.put(AppConstants.KEY_PASSWORD, password);
            jsonObject.put(AppConstants.KEY_FIRST_NAME, fName);
            jsonObject.put(AppConstants.KEY_LAST_NAME, lName);
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
                requestListener.failed("Server error please try again");
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
                    if (response.getInt(AppConstants.KEY_STATUS) == 1) {
                        JSONArray users = response.getJSONArray(AppConstants.KEY_USERS);
                        Integer usersLength = users.length();
                        if (usersLength != 0) {
                            for (int i = 0; i < usersLength; ++i) {
                                JSONObject userObject = (JSONObject) users.get(i);
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
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(activity);

        CustomJSONAuthObject jsonAuthObject = new CustomJSONAuthObject(Request.Method.GET,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_PROJECTS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(AppConstants.KEY_STATUS) == 1) {
                        JSONArray projects = response.getJSONArray(AppConstants.KEY_PROJECTS);
                        Integer projectsLength = projects.length();
                        if (projectsLength != 0) {
                            for (int i = 0; i < projectsLength; ++i) {
                                JSONObject projectObject = (JSONObject) projects.get(i);
                                Long projectId = databaseHelper.addProject(new Project(
                                        projectObject.getLong(AppConstants.KEY_ID),
                                        projectObject.getString(AppConstants.KEY_NAME),
                                        projectObject.getString(AppConstants.KEY_BODY),
                                        projectObject.getString(AppConstants.KEY_CLIENT),
                                        projectObject.getString(AppConstants.KEY_DEADLINE),
                                        projectObject.getBoolean(AppConstants.KEY_COMPLETED),
                                        projectObject.getLong(AppConstants.KEY_MANAGER_ID)
                                ));

                                JSONArray members = (JSONArray) projectObject.get(AppConstants.KEY_MEMBERS);
                                Integer membersLength = members.length();
                                if (membersLength != 0) {
                                    for (int j = 0; j < membersLength; ++j) {
                                        JSONObject memberObject = (JSONObject) members.get(j);
                                        databaseHelper.associateUserProject(projectId, memberObject.getLong(AppConstants.KEY_ID));
                                    }
                                }

                                JSONArray tasks = (JSONArray) projectObject.get(AppConstants.KEY_TASKS);
                                Integer tasksLength = tasks.length();
                                if (tasksLength != 0) {
                                    for (int j = 0; j < tasksLength; ++j) {
                                        JSONObject taskObject = (JSONObject) tasks.get(j);
                                        databaseHelper.addTask(new Task(
                                                taskObject.getLong(AppConstants.KEY_ID),
                                                taskObject.getString(AppConstants.KEY_NAME),
                                                taskObject.getString(AppConstants.KEY_BODY),
                                                taskObject.getBoolean(AppConstants.KEY_COMPLETED),
                                                taskObject.getInt(AppConstants.KEY_ESTIMATED_TIME),
                                                taskObject.isNull(AppConstants.KEY_TASK_ID) ? null : taskObject.getLong(AppConstants.KEY_TASK_ID),
                                                taskObject.getLong(AppConstants.KEY_PROJECT_ID),
                                                taskObject.isNull(AppConstants.KEY_USER_ID) ? null: taskObject.getLong(AppConstants.KEY_USER_ID)
                                        ));
                                    }
                                }
                            }
                        }
                    } else {
                        requestListener.failed("Server error");
                        Log.e("ERROR SYNCPR", "Server error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR SYNCPR", e.getMessage());
                    requestListener.failed(e.getMessage());
                }
                requestListener.finished("Projects updated");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    requestListener.failed(error.getMessage());
                    Log.e("ERROR SYNCPR2", error.getMessage());
                } else {
                    Log.e("ERROR SYNCPR3", "FAILED");
                    requestListener.failed("Request failed");
                }
            }
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonAuthObject);
    }

    public void createProject(Project project, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_NAME, project.getName());
            jsonObject.put(AppConstants.KEY_BODY, project.getBody());
            jsonObject.put(AppConstants.KEY_CLIENT, project.getClient());
            jsonObject.put(AppConstants.KEY_DEADLINE, project.getDeadline());
            jsonObject.put(AppConstants.KEY_MANAGER_ID, project.getManagerId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_CREATE_PROJECT, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
                try {
                    Integer status = response.getInt(AppConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Project creation failed");
                    } else {
                        Long projectId = response.getLong(AppConstants.KEY_PROJECT_ID);
                        requestListener.finished(String.valueOf(projectId));
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
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void updateProject(Project project, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_ID, project.getServerId());
            jsonObject.put(AppConstants.KEY_NAME, project.getName());
            jsonObject.put(AppConstants.KEY_BODY, project.getBody());
            jsonObject.put(AppConstants.KEY_CLIENT, project.getClient());
            jsonObject.put(AppConstants.KEY_DEADLINE, project.getDeadline());
            jsonObject.put(AppConstants.KEY_MANAGER_ID, project.getManagerId());
            jsonObject.put(AppConstants.KEY_COMPLETED, project.getCompleted());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_UPDATE_PROJECT, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
                try {
                    Integer status = response.getInt(AppConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Project update failed");
                    } else {
                        requestListener.finished("Update successful");
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
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void deleteProject(Project project, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_ID, project.getServerId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_DELETE_PROJECT, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
                try {
                    Integer status = response.getInt(AppConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Project deletion failed");
                    } else {
                        requestListener.finished("Deletion successful");
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
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void associateUserProject(User user, Project project, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_USER_ID, user.getServerId());
            jsonObject.put(AppConstants.KEY_PROJECT_ID, project.getServerId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_ASSOCIATE_USER_PROJECT, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
                try {
                    Integer status = response.getInt(AppConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Project user association failed");
                    } else {
                        requestListener.finished("Project user association successful");
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
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void createTask(Task task, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        Log.e("CREAtE TAS", task.getName());
        Log.e("CREAtE TAS", String.valueOf(task.getCategoryId()));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_NAME, task.getName());
            jsonObject.put(AppConstants.KEY_BODY, task.getBody());
            jsonObject.put(AppConstants.KEY_COMPLETED, task.getCompleted());
            jsonObject.put(AppConstants.KEY_ESTIMATED_TIME, task.getEstimatedTime());
            jsonObject.put(AppConstants.KEY_PROJECT_ID, task.getProjectId());
            if(task.getUserId() != null) {
                jsonObject.put(AppConstants.KEY_USER_ID, task.getUserId());
            }
            Log.e("CATE ID", String.valueOf(task.getCategoryId()));
            if(task.getCategoryId() != null) {
                Log.e("DIFFRENT ID", String.valueOf(task.getCategoryId()));
                jsonObject.put(AppConstants.KEY_TASK_ID, task.getCategoryId());
            }
            jsonObject.put(AppConstants.KEY_USER_ID, task.getUserId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_CREATE_TASK, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
                try {
                    Integer status = response.getInt(AppConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Task creation failed");
                    } else {
                        Long projectId = response.getLong(AppConstants.KEY_TASK_ID);
                        requestListener.finished(String.valueOf(projectId));
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
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void checkTask(Task task, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_ID, task.getServerId());
            jsonObject.put(AppConstants.KEY_COMPLETED, task.getCompleted());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_CHECK_TASK, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
                try {
                    Integer status = response.getInt(AppConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Task update failed");
                    } else {
                        requestListener.finished("Update successful");
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
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void deleteTask(Task task, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppConstants.KEY_ID, task.getServerId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.POST,
                AppConstants.API_BASE_URL + AppConstants.ENDPOINT_DELETE_TASK, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
//                try {
//                    Log.e("RES STATUS", "BEFORE");
//                    Integer status = response.getInt(AppConstants.KEY_STATUS);
//                    Log.e("RES STATUS", String.valueOf(status));
//                    if (status.equals(0)) {
//                        Log.e("RES STATUS", "ALES GUT");
//
//                        requestListener.failed("Task deletion failed");
//                    } else {
                        requestListener.finished("Deleted successfully");
//                        Log.e("RES STATUS", "NICHTS GUT");
//                    }
//                } catch (Exception e) {
//                    Log.e("DELETTE task API", e.getMessage());
//                    requestListener.failed(e.getMessage());
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.failed(error.getMessage());
            }
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }
}
