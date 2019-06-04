package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.PETBook.Models.ReportModel;
import com.example.pantallafirstview.R;

import java.util.ArrayList;

public class ReportsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ReportModel> reportsList;

    public ReportsAdapter(Context context, ArrayList<ReportModel> reportsList){
        this.context = context;
        this.reportsList = reportsList;
    }

    @Override
    public int getCount() {
        if (reportsList != null)
            return reportsList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return reportsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.report_design,null);
        }


        /*

        TODO Rellenar repor_design
         */


        return convertView;
    }
}
