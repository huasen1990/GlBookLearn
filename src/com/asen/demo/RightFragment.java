package com.asen.demo;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RightFragment extends Fragment implements OnClickListener{
	
	private Button 	bt;
	private ListView lv;
	private View view;
	private ArrayAdapter<String> adapter;
	private List<String> list;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view  = inflater.inflate(R.layout.fragment_right, container, false);
		return  view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	private void initView() {
		bt = (Button) view.findViewById(R.id.button);
		lv = (ListView) view.findViewById(R.id.listview);
		list = new ArrayList<String>();
		adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);
		
		bt.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button) {
			getContacts();
		}
	}
	private void getContacts() {
		Cursor cursor  = null;
		try {
			cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
			while (cursor.moveToNext()) {
				String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				
				list.add(displayName+"\n"+number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
		}
		adapter.notifyDataSetChanged();
	}
}
