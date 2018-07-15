package com.example.cool.patient.diagnostic;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.FontManager;
import com.example.cool.patient.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Udayasri on 29-05-2018.
 */

public class DiagnosticSideNavigationExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    public static int Services = 0;
    public static int Address = 1;
    public static int ITEM3 = 2;
    public static int ITEM4 = 3;
    public static int ITEM5 = 4;
    public static int ITEM6 = 5;
    public static int ITEM7 = 6;
    public static int ITEM8 = 7;


    public static int SUBITEM1_1 = 0;
    public static int SUBITEM1_2 = 1;
//    public static int SUBITEM1_3 = 2;


    public static int SUBITEM2_1 = 0;
    public static int SUBITEM2_2 = 1;


    public static int SUBITEM3_1 = 0;
    public static int SUBITEM3_2 = 1;

    public DiagnosticSideNavigationExpandableListAdapter(Context context, List<String> expandableListTitle,
                                                     HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;


    }


    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }


    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }


    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText1 = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.diagnostic_side_navigation_heading, null);
        }
        ImageView img1 = (ImageView) convertView.findViewById(R.id.listitemsIcons);
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItems);
        expandedListTextView.setText(expandedListText1);

        if (listPosition == Services) {
            if (expandedListPosition == SUBITEM1_1) {
                img1.setImageResource(R.drawable.calendar);
            } else if (expandedListPosition == SUBITEM1_2) {
                img1.setImageResource(R.drawable.calendar);
            }
//            else if (expandedListPosition == SUBITEM1_3) {
//                img1.setImageResource(R.drawable.diagnosticcircular);
//            }

        } else if (listPosition == Address) {
            if (expandedListPosition == SUBITEM2_1) {
                img1.setImageResource(R.drawable.addaddress);
            } else if (expandedListPosition == SUBITEM2_2) {
                img1.setImageResource(R.drawable.editiconn);
            }

        } else if (listPosition == ITEM3) {

            if (expandedListPosition == SUBITEM3_1) {
                img1.setImageResource(R.drawable.password_eye_icon);
            } else if (expandedListPosition == SUBITEM3_2) {
                img1.setImageResource(R.drawable.change_mobile_icon);
            }
        }
        return convertView;
    }

    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    public long getGroupId(int listPosition) {
        return listPosition;
    }

    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.diagnostic_side_navigation_subheading, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitles);
        listTitleTextView.setTypeface(null, Typeface.NORMAL);
        listTitleTextView.setText(listTitle);
        TextView listTitleTextArrowView = (TextView) convertView
                .findViewById(R.id.listTitleArrows);
        listTitleTextArrowView.setTypeface(null, Typeface.NORMAL);
        listTitleTextArrowView.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME));
        // set icons for menu items
        ImageView listTitleTextIconView1 = (ImageView) convertView
                .findViewById(R.id.listTitleIcons);
//        listTitleTextIconView.setTypeface(null, Typeface.NORMAL);
//        listTitleTextIconView.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME));
        if (listPosition == Services)
            listTitleTextIconView1.setImageResource(R.drawable.medicalservices);
            //listTitleTextIconView.setText(context.getResources().getString(R.string.fa_glass));
        else if (listPosition == Address)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_music));
            listTitleTextIconView1.setImageResource(R.drawable.addressicon);
        else if (listPosition == ITEM3)
            //   listTitleTextIconView.setText(context.getResources().getString(R.string.fa_search));
            listTitleTextIconView1.setImageResource(R.drawable.setting);
        else if (listPosition == ITEM4)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView1.setImageResource(R.drawable.ic_edit_black_24dp);
        else if (listPosition == ITEM5)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));

            listTitleTextIconView1.setImageResource(R.drawable.subscriptionplan);
        else if (listPosition == ITEM6)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView1.setImageResource(R.drawable.symb)
                    ;
        else if (listPosition == ITEM7)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView1.setImageResource(R.drawable.contant_us)
                    ;
        else if (listPosition == ITEM8)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView1.setImageResource(R.drawable.shutdown)
                    ;
        // set arrow icons for relevant items
        if (listPosition == Services || listPosition == Address || listPosition == ITEM3) {
            if (!isExpanded)
                listTitleTextArrowView.setText(context.getResources().getString(R.string.fa_chevron_right));
            else
                listTitleTextArrowView.setText(context.getResources().getString(R.string.fa_chevron_down));
        } else {
            listTitleTextArrowView.setText("");
        }
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}