package com.example.ram.benellacompat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.ram.benellacompat.R;
import com.example.ram.benellacompat.pojo.SubCategoryData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ram on 2/15/18.
 */

public class EpandedeListAdapter extends BaseExpandableListAdapter {
    Context context;
    private ArrayList<String> category=new ArrayList<>();
    static OnRecordSelect listener;
    private HashMap<String,ArrayList<SubCategoryData>> subcategory=new HashMap<>();
    public EpandedeListAdapter(Context context,ArrayList<String> header,HashMap<String,ArrayList<SubCategoryData>> child){
        this.context=context;
        category.addAll(header);
        subcategory=child;
    }
    @Override
    public int getGroupCount() {
        return category.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
            return subcategory.get(category.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return category.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return subcategory.get(category.get(groupPosition)).get(childPosition);
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
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.nav_row_item,parent,false);
        TextView textView=convertView.findViewById(R.id.category);
        textView.setText(getGroup(groupPosition).toString()+"");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.nav_row_category,parent,false);
        TextView textView=convertView.findViewById(R.id.category);
        final SubCategoryData subCategoryData= (SubCategoryData) getChild(groupPosition,childPosition);
        textView.setText(subCategoryData.getTitle()+"");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.SetrecordSelect(subCategoryData.getId(),subCategoryData.getTitle());
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public void setOnRecordSelect(OnRecordSelect listen){
        listener=listen;
    }
    public interface OnRecordSelect{
        void SetrecordSelect(String id,String name2);
    }
}
