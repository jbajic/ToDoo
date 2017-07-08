package com.jbajic.todoo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jbajic.todoo.R;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.models.Task;
import com.jbajic.todoo.services.APIService;

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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
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

        categoryViewHolder.tvCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                else ((ExpandableListView) parent).expandGroup(groupPosition, true);
            }
        });
        categoryViewHolder.btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService apiService = APIService.getInstance((Activity) context);

                apiService.deleteTask(getGroup(groupPosition), new RequestListener() {
                    @Override
                    public void failed(String message) {
                        Toast
                                .makeText(context, message, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void finished(String message) {
                        categoryList.remove(getGroup(groupPosition));
                        DatabaseHelper.getInstance(context).deleteTask(getGroup(groupPosition));
                        updateExpandableListView(categoryList);
                    }
                });
            }
        });
        categoryViewHolder.llCategoryItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getResources().getString(R.string.item_description, "Category"))
                        .setMessage(categoryList.get(groupPosition).getBody())
                        .setCancelable(true)
                        .show();
                return true;
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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
        taskViewHolder.cbTaskCompleted.setChecked(getChild(groupPosition, childPosition).getCompleted());

        taskViewHolder.cbTaskCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("ONCHECKED", "CHECK");
                APIService apiService = APIService.getInstance((Activity) context);
                getChild(groupPosition, childPosition).setCompleted(isChecked);

                apiService.checkTask(getChild(groupPosition, childPosition), new RequestListener() {
                    @Override
                    public void failed(String message) {
                        Toast
                                .makeText(context, message, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void finished(String message) {
                        DatabaseHelper.getInstance(context).checkTask(getChild(groupPosition, childPosition));
                        updateExpandableListView(categoryList);
                    }
                });
            }
        });
        taskViewHolder.btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService apiService = APIService.getInstance((Activity) context);

                apiService.deleteTask(getChild(groupPosition, childPosition), new RequestListener() {
                    @Override
                    public void failed(String message) {
                        Toast
                                .makeText(context, message, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void finished(String message) {
                        categoryList.get(groupPosition).getTaskList().remove(getChild(groupPosition, childPosition));
                        DatabaseHelper.getInstance(context).deleteTask(getChild(groupPosition, childPosition));
                        updateExpandableListView(categoryList);
                    }
                });
            }
        });
        taskViewHolder.llItemTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getResources().getString(R.string.item_description, "Task"))
                        .setMessage(categoryList.get(groupPosition).getTaskList().get(childPosition).getBody())
                        .setCancelable(true)
                        .show();
                return true;
            }
        });
        return convertView;
    }

//    private void showRemoveImagePopup(View view, final Integer groupPosition, final Integer childPosition) {
//        final PopupMenu popup = new PopupMenu(context, view);
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Log.e("REMOVEV", "CLICK");
//                if (childPosition != null) {
//                    Log.e("REMOVEV", "Child");
//                    categoryList.get(groupPosition).getTaskList().remove(childPosition);
//                } else {
//                    Log.e("REMOVEV", "parent");
//                    categoryList.remove(groupPosition);
//                }
//                notifyDataSetChanged();
//                return true;
//            }
//        });
//        popup.inflate(R.menu.popup_remove);
//        popup.show();
//    }

    public void updateExpandableListView(List<Task> categoryList) {
        if (categoryList.size() != getGroupCount()) {
            this.categoryList = categoryList;
            notifyDataSetChanged();
        }
//        notifyAll();
        notifyDataSetChanged();
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
        @InjectView(R.id.ll_categoryItem)
        LinearLayout llCategoryItem;
        @InjectView(R.id.btn_deleteCategory)
        Button btnDeleteCategory;

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
        @InjectView(R.id.ll_itemTask)
        LinearLayout llItemTask;
        @InjectView(R.id.btn_deleteTask)
        Button btnDeleteTask;

        public TaskViewHolder(View taskViewHolder) {
            ButterKnife.inject(this, taskViewHolder);
        }

    }

}
