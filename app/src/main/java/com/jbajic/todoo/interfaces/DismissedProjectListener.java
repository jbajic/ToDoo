package com.jbajic.todoo.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by jbajic on 02.07.17..
 */

public interface DismissedProjectListener {

    void onDismissed(RecyclerView.ViewHolder viewHolder);
}
