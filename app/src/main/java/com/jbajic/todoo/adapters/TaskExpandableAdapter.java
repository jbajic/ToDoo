package com.jbajic.todoo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jbajic.todoo.R;
import com.jbajic.todoo.models.Task;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jbajic on 06.07.17..
 */

public class TaskExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Task> categoryList;

    public TaskExpandableAdapter(Context context, List<Task> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryList.get(groupPosition).getTaskList().size();
    }

    @Override
    public Task getGroup(int groupPosition) {
        return categoryList.get(groupPosition);
    }

    @Override
    public Task getChild(int groupPosition, int childPosition) {
        return categoryList.get(groupPosition).getTaskList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return categoryList.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return categoryList.get(groupPosition).getTaskList().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CategoryViewHolder categoryViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_category, parent, false);
            categoryViewHolder = new CategoryViewHolder(convertView);
            convertView.setTag(categoryViewHolder);
        } else {
            categoryViewHolder = (CategoryViewHolder) convertView.getTag();
        }

        categoryViewHolder.tvCategoryName.setText(getGroup(groupPosition).getName());
        categoryViewHolder.tvNumberOfTasks.setText(String.valueOf(getChildrenCount(groupPosition)));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TaskViewHolder taskViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_task, parent, false);
            taskViewHolder = new TaskViewHolder(convertView);
            convertView.setTag(taskViewHolder);
        } else {
            taskViewHolder = (TaskViewHolder) convertView.getTag();
        }

        taskViewHolder.tvTaskName.setText(getChild(groupPosition, childPosition).getName());
        StringBuilder estimatedTimeString = new StringBuilder();
        estimatedTimeString.append(getChild(groupPosition, childPosition).getEstimatedTime()).append(" h");
        taskViewHolder.tvEstimatedTime.setText(estimatedTimeString.toString());
        return convertView;
    }

    public void updateExpandableListView(List<Task> categoryList) {
        if(categoryList.size() != getGroupCount()) {
            this.categoryList = categoryList;
//            notifyDataSetChanged();
            notifyAll();
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class CategoryViewHolder {
        @InjectView(R.id.tv_categoryName)
        TextView tvCategoryName;
        @InjectView(R.id.tv_numberOfTasks)
        TextView tvNumberOfTasks;

        public CategoryViewHolder(View categoryView) {
            ButterKnife.inject(this, categoryView);
        }

    }

    static class TaskViewHolder {
        @InjectView(R.id.tv_taskName)
        TextView tvTaskName;
        @InjectView(R.id.tv_estimatedTime)
        TextView tvEstimatedTime;
        @InjectView(R.id.cb_taskCompleted)
        CheckBox cbTaskCompleted;

        public TaskViewHolder(View taskViewHolder) {
            ButterKnife.inject(this, taskViewHolder);
        }

    }

}
