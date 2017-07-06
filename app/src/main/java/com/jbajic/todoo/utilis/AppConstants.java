package com.jbajic.todoo.utilis;

/**
 * Created by jure on 13.05.17..
 */

public final class AppConstants {

    public static final String SHARED_PREFERENCES_NAME = "toDoo";
    public static final String API_TAG = "toDoo";
    public static final String API_BASE_URL = "http://todoos.azurewebsites.net/api/";

    /**
     * API KEYS
     */
    public static final String APPLICATION_AUTHORIZATION = "Authorization";
    public static final String KEY_JWT = "jwt";
    public static final String KEY_STATUS = "status";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ID = "id";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_IS_ME = "isMe";
    public static final String KEY_USERS = "users";

    public static final String KEY_PROJECTS = "projects";
    public static final String KEY_NAME = "name";
    public static final String KEY_BODY = "body";
    public static final String KEY_CLIENT = "client";
    public static final String KEY_DEADLINE = "deadline";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_MANAGER_ID = "manager_id";
    public static final String KEY_TASKS = "tasks";
    public static final String KEY_ESTIMATED_TIME = "estimated_time";
    public static final String KEY_PROJECT_ID = "project_id";
    public static final String KEY_TASK_ID = "task_id";

    /**
     * API ENDPOINTS
     */
    public static final String ENDPOINT_LOGIN = "login";
    public static final String ENDPOINT_REGISTER = "register";
    public static final String ENDPOINT_USERS = "synchronize-users";
    public static final String ENDPOINT_PROJECTS = "synchronize-projects";
    public static final String ENDPOINT_CREATE_PROJECT = "create-project";
    public static final String ENDPOINT_UPDATE_PROJECT = "update-project";
    public static final String ENDPOINT_DELETE_PROJECT = "delete-project";
    public static final String ENDPOINT_CREATE_TASK = "create-task";
    public static final String ENDPOINT_DELETE_TASK = "delete-task";

    /**
     * Extra info
     */
    public static final String EXTRA_KEY_PROJECT = "project";
}
