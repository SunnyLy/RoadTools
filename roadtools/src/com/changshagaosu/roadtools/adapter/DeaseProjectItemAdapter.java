package com.changshagaosu.roadtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.DeaseProjectBean;

import java.util.List;

/**
 * 病害维修项目列表适配器
 */
public class DeaseProjectItemAdapter extends BaseAdapter {
    private List<DeaseProjectBean> mList;
    private Context mContext;

    public DeaseProjectItemAdapter(Context pContext, List<DeaseProjectBean> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DeaseProjectBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.item_dease_project,
                null);
        DeaseProjectBean projectBean = mList.get(position);
        if (convertView != null && projectBean != null) {
            TextView deaseTypeNumber = (TextView) convertView.findViewById(R.id.tv_dease_type_number);
            TextView deaseProjName = (TextView) convertView.findViewById(R.id.tv_dease_project_name);
            TextView deaseUnit = (TextView) convertView.findViewById(R.id.tv_dease_unit);
            TextView deaseProjNumber = (TextView) convertView.findViewById(R.id.tv_dease_project_number);
            TextView deaseId = (TextView) convertView.findViewById(R.id.deaseId);
            deaseId.setText((position + 1) + "");
            deaseTypeNumber.setText(projectBean.getItemCode());
            deaseProjName.setText(projectBean.getItemName());
            deaseUnit.setText(projectBean.getItemUnit());
            deaseProjNumber.setText(projectBean.getEngineering());
        }
        return convertView;
    }
}