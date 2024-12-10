package com.af.aasthafincorp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> parentList;
    private Map<String, List<String>> childList;

    public CustomExpandableListAdapter(Context context, List<String> parentList, Map<String, List<String>> childList) {
        this.context = context;
        this.parentList = parentList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.containsKey(parentList.get(groupPosition)) ? childList.get(parentList.get(groupPosition)).size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(parentList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_list_group, null);
        }

        TextView label = convertView.findViewById(R.id.label);
        ImageView icon = convertView.findViewById(R.id.icon);
        ImageView chevron = convertView.findViewById(R.id.chevron);

        label.setText((String) getGroup(groupPosition));
        icon.setImageResource(R.drawable.circle16); // Set your group icon here
        chevron.setImageResource(isExpanded ? R.drawable.chevron_up : R.drawable.chevron_down);

        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Dashboard")){
            chevron.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.dashboard);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Meeting Calendar")){
            chevron.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.meeting_calendar);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Todo")){
            chevron.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.todo);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("View Leads")){
            chevron.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.leads2);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("View Accounts")){
            chevron.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.accounting);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Tasks")){
            icon.setImageResource(R.drawable.task);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Meetings")){
            icon.setImageResource(R.drawable.meeting);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Casual Meetings")){
            icon.setImageResource(R.drawable.casual_meeting);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Business Achievements")){
            icon.setImageResource(R.drawable.business_achievements);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Contacts")){
            icon.setImageResource(R.drawable.contacts);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Reference")){
            icon.setImageResource(R.drawable.reference);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Leaves")){
            icon.setImageResource(R.drawable.leave);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Reports")){
            icon.setImageResource(R.drawable.report);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Announcements")){
            icon.setImageResource(R.drawable.announcement);
        }
        if(((String) getGroup(groupPosition)).equalsIgnoreCase("Training Files")){
            icon.setImageResource(R.drawable.training);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_list_child, null);
        }

        TextView label = convertView.findViewById(R.id.label);
        ImageView icon = convertView.findViewById(R.id.icon);

        label.setText((String) getChild(groupPosition, childPosition));
        // Set an icon for the child
        icon.setImageResource(R.drawable.circle16); // Replace with actual resource

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
