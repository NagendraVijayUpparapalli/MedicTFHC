package com.example.cool.patient.patient;

import java.util.HashMap;
import java.util.List;
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


/**
 * Created by Udayasri on 16-05-2018.
 */

public class PatientSideNavigationExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    public static int ITEM1 = 0;
    public static int ITEM2 = 1;
    public static int ITEM3 = 2;
    public static int ITEM4 = 3;
    public static int ITEM5 = 4;
    public static int ITEM6 = 5;
    public static int ITEM7 = 6;
    public static int ITEM8 = 7;


    public static int SUBITEM1_1 = 0;
    public static int SUBITEM1_2 = 1;
    public static int SUBITEM1_3 = 2;
    public static int SUBITEM1_4 = 3;
    public static int SUBITEM1_5 = 4;
    public static int SUBITEM1_6 = 5;


    public static int SUBITEM2_1 = 0;
    public static int SUBITEM2_2 = 1;
    public static int SUBITEM2_3 = 2;
    public static int SUBITEM2_4 = 3;
    public static int SUBITEM2_5 = 4;
    public static int SUBITEM2_6 = 5;

    public static int SUBITEM3_1 = 0;
    public static int SUBITEM3_2 = 1;

    public PatientSideNavigationExpandableListAdapter(Context context, List<String> expandableListTitle,
                                                      HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;


    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.side_navigation_heading, null);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.listitemsIcon);
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);

        if (listPosition == ITEM1) {
            if (expandedListPosition == SUBITEM1_1) {
                img.setImageResource(R.drawable.doctorcircular);
            }
            else if (expandedListPosition == SUBITEM1_2) {
                img.setImageResource(R.drawable.diagnosticcircular);
            }
            else if (expandedListPosition == SUBITEM1_3) {
                img.setImageResource(R.drawable.medicalcircular);
            }
            else if (expandedListPosition == SUBITEM1_4) {
                img.setImageResource(R.drawable.hospitalcircular);
            }
            else if (expandedListPosition == SUBITEM1_5) {
                img.setImageResource(R.drawable.bloodbankcircular);
            }
            else if (expandedListPosition == SUBITEM1_6) {
                img.setImageResource(R.drawable.ambulancecircular);
            }
        }
        else if(listPosition == ITEM2) {
            if(expandedListPosition == SUBITEM2_1 ) {
                img.setImageResource(R.drawable.doctorcircular);
            }
            else if (expandedListPosition == SUBITEM2_2) {
                img.setImageResource(R.drawable.diagnosticcircular);
            }
            else if (expandedListPosition == SUBITEM2_3) {
                img.setImageResource(R.drawable.medicalcircular);
            }
            else if (expandedListPosition == SUBITEM2_4) {
                img.setImageResource(R.drawable.hospitalcircular);
            }
            else if (expandedListPosition == SUBITEM2_5) {
                img.setImageResource(R.drawable.hospitalcircular);
            }
            else if (expandedListPosition == SUBITEM2_6) {
                img.setImageResource(R.drawable.hospitalcircular);
            }
        }
        else if(listPosition == ITEM3) {

            if(expandedListPosition == SUBITEM3_1 ) {
                img.setImageResource(R.drawable.setting);
            }
            else if (expandedListPosition == SUBITEM3_2)
            {
                img.setImageResource(R.drawable.setting);
            }
        }
        return convertView;
    }
    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }
    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }
    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }
    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }
    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.side_navigation_subheading, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.NORMAL);
        listTitleTextView.setText(listTitle);
        TextView listTitleTextArrowView = (TextView) convertView
                .findViewById(R.id.listTitleArrow);
        listTitleTextArrowView.setTypeface(null, Typeface.NORMAL);
       listTitleTextArrowView.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME));
        // set icons for menu items
        ImageView listTitleTextIconView = (ImageView) convertView
                .findViewById(R.id.listTitleIcon);
//        listTitleTextIconView.setTypeface(null, Typeface.NORMAL);
//        listTitleTextIconView.setTypeface(FontManager.getTypeface(context, FontManager.FONTAWESOME));
        if (listPosition == ITEM1)
            listTitleTextIconView.setImageResource(R.drawable.medicalservices);
            //listTitleTextIconView.setText(context.getResources().getString(R.string.fa_glass));
        else if (listPosition == ITEM2)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_music));
            listTitleTextIconView.setImageResource(R.drawable.history);
        else if (listPosition == ITEM3)
            //   listTitleTextIconView.setText(context.getResources().getString(R.string.fa_search));
            listTitleTextIconView.setImageResource(R.drawable.setting);
        else if (listPosition == ITEM4)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView.setImageResource(R.drawable.ic_edit_black_24dp);
        else if (listPosition == ITEM5)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView.setImageResource(R.drawable.myfamily)
                    ;
        else if (listPosition == ITEM6)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView.setImageResource(R.drawable.symb);
        // set arrow icons for relevant items
        else if (listPosition == ITEM7)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView.setImageResource(R.drawable.contant_us);
        else if (listPosition == ITEM8)
            // listTitleTextIconView.setText(context.getResources().getString(R.string.fa_envelope_o));
            listTitleTextIconView.setImageResource(R.drawable.shutdown);

        if (listPosition == ITEM1 ||  listPosition == ITEM3 || listPosition == ITEM2) {
            if (!isExpanded)
                listTitleTextArrowView.setText(context.getResources().getString(R.string.fa_chevron_right));
            else
                listTitleTextArrowView.setText(context.getResources().getString(R.string.fa_chevron_down));
        } else {
            listTitleTextArrowView.setText("");
        }
        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}