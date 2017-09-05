package com.changshagaosu.roadtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.R_RoadPart;

import java.util.List;

public class R_RoadPartAdapter extends BaseAdapter {
	private List<R_RoadPart> mList;
	private Context mContext;

	public R_RoadPartAdapter(Context pContext, List<R_RoadPart> pList) {
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

	/**
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
		convertView = _LayoutInflater.inflate(R.layout.simple_spinner_item,
				null);
		if (convertView != null) {
			TextView _TextView1 = (TextView) convertView
					.findViewById(R.id.text1);
			_TextView1.setText(mList.get(position).getRoadPartName());
		}
		return convertView;
	}
}