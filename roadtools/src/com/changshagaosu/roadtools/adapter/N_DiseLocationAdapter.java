package com.changshagaosu.roadtools.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.sub.RoadLine;

public class N_DiseLocationAdapter extends BaseAdapter {
	private List<RoadLine.Traffic> mList;
	private Context mContext;

	public N_DiseLocationAdapter(Context pContext, List<RoadLine.Traffic> pList) {
		this.mContext = pContext;
		this.mList = pList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
		convertView = _LayoutInflater.inflate(R.layout.simple_spinner_item,
				null);
		if (convertView != null) {
			TextView _TextView1 = (TextView) convertView
					.findViewById(R.id.text1);
			_TextView1.setText(mList.get(position).getTrafficLineName());
		}
		return convertView;
	}
}