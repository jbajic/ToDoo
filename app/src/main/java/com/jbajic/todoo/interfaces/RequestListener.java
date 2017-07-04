package com.jbajic.todoo.interfaces;

/**
 * Created by jure on 14.05.17..
 */

public interface RequestListener {

    void failed(String message);

    void finished(String message);
}
