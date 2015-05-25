package com.example.zf_pad.aadpter;

import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.Config;
import com.example.zf_pad.MyApplication;
import com.epalmpay.userPad.R;
import com.example.zf_pad.activity.PostSonList;
import com.example.zf_pad.entity.PortSon;
import com.example.zf_pad.entity.PosItem;
import com.example.zf_pad.entity.PosPortChild;
import com.example.zf_pad.entity.PostPortEntity;
import com.example.zf_pad.entity.TestEntitiy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PosPortAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<PostPortEntity> list;
	private List<PostPortEntity> glist = new ArrayList<PostPortEntity>();
	private LayoutInflater inflater;
	public ExpandableListView listView;

	public PosPortAdapter(Context context, List<PostPortEntity> list,
			List<PostPortEntity> glist) {
		this.context = context;
		this.list = list;
		this.glist = glist;
	}

	public void setListView(ExpandableListView listView) {
		this.listView = listView;
	}

	public void change() {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getChildlist().size() != 0)
				list.get(i).getChildlist().remove(0);
			if (list.get(i).getChildlist().size() != 0)
				list.get(i).getChildlist().remove(0);
			if (list.get(i).getChildlist().size() != 0)
				list.get(i).getChildlist().remove(0);
			if (list.get(i).getChildlist().size() != 0)
				list.get(i).getChildlist().remove(0);

		}
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return (int) Math.ceil(((double) list.get(groupPosition).getChildlist()
				.size()) / 4);
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).getChildlist().get(childPosition);
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
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final int childSize = (int) Math.ceil(((double) list.get(groupPosition)
				.getChildlist().size()) / 4);
		final int childListSize = list.get(groupPosition).getChildlist().size() - 1;
		inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.pos_port_item, null);
		TextView text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView text3 = (TextView) convertView.findViewById(R.id.text3);
		TextView text4 = (TextView) convertView.findViewById(R.id.text4);
		CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.item_cb1);
		CheckBox cb2 = (CheckBox) convertView.findViewById(R.id.item_cb2);
		CheckBox cb3 = (CheckBox) convertView.findViewById(R.id.item_cb3);
		CheckBox cb4 = (CheckBox) convertView.findViewById(R.id.item_cb4);

		if (childPosition == childSize - 1) {
			// Toast.makeText(context, +childSize-1+"", 1000).show();
			if (childListSize - childPosition * 4 == 0) {
				text1.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4).getValue());
				text2.setVisibility(View.INVISIBLE);
				text3.setVisibility(View.INVISIBLE);
				text4.setVisibility(View.INVISIBLE);
				cb2.setVisibility(View.INVISIBLE);
				cb3.setVisibility(View.INVISIBLE);
				cb4.setVisibility(View.INVISIBLE);
			} else if (childListSize - childPosition * 4 == 1) {
				text1.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4).getValue());
				text2.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 1).getValue());
				text3.setVisibility(View.INVISIBLE);
				text4.setVisibility(View.INVISIBLE);
				cb3.setVisibility(View.INVISIBLE);
				cb4.setVisibility(View.INVISIBLE);
			} else if (childListSize - childPosition * 4 == 2) {
				text1.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4).getValue());
				text2.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 1).getValue());
				text3.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 2).getValue());
				text4.setVisibility(View.INVISIBLE);
				cb4.setVisibility(View.INVISIBLE);
			} else if (childListSize - childPosition * 4 == 3) {
				text1.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4).getValue());
				text2.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 1).getValue());
				text3.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 2).getValue());
				text4.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 3).getValue());
			}

		} else {

			text1.setText(list.get(groupPosition).getChildlist()
					.get(childPosition * 4).getValue());
			text2.setText(list.get(groupPosition).getChildlist()
					.get(childPosition * 4 + 1).getValue());
			text3.setText(list.get(groupPosition).getChildlist()
					.get(childPosition * 4 + 2).getValue());
			text4.setText(list.get(groupPosition).getChildlist()
					.get(childPosition * 4 + 3).getValue());
		}
		if (groupPosition == 1) {
			cb1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
		
						((CheckBox) v).setChecked(true);
						for (int i = 0; i < list.get(groupPosition)
								.getChildlist().size(); i++) {
							list.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						for (int i = 0; i < glist.get(groupPosition)
								.getChildlist().size(); i++) {
							glist.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						list.get(groupPosition).getChildlist().get(childPosition * 4)
								.setIsCheck(true);

						PosPortAdapter.this.notifyDataSetChanged();
						Config.son = list.get(groupPosition).getChildlist()
								.get(childPosition * 4).getSon();
					Config.portindex=(childPosition+1)*4;
						if (Config.son != null) {
							context.startActivity(new Intent(context,
									PostSonList.class));
						} else {
							PortSon p = new PortSon();
							p.setId(list.get(groupPosition).getChildlist()
									.get(childPosition * 4).getId());
							p.setValue(list.get(groupPosition).getChildlist()
									.get(childPosition * 4).getValue());
							Config.myson = p;
						}
					} else {
					
						((CheckBox) v).setChecked(false);
						list.get(groupPosition).getChildlist().get(childPosition * 4)
								.setIsCheck(false);
						Config.myson = null;
					
					}

				}
			});
			cb2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
		
						((CheckBox) v).setChecked(true);
						for (int i = 0; i < list.get(groupPosition)
								.getChildlist().size(); i++) {
							list.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						for (int i = 0; i < glist.get(groupPosition)
								.getChildlist().size(); i++) {
							glist.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						list.get(groupPosition).getChildlist().get(childPosition * 4+1)
								.setIsCheck(true);

						PosPortAdapter.this.notifyDataSetChanged();
						Config.son = list.get(groupPosition).getChildlist()
								.get(childPosition * 4+1).getSon();
						Config.portindex=(childPosition+1)*4+1;
						if (Config.son != null) {
							context.startActivity(new Intent(context,
									PostSonList.class));
						} else {
							PortSon p = new PortSon();
							p.setId(list.get(groupPosition).getChildlist()
									.get(childPosition * 4+1).getId());
							p.setValue(list.get(groupPosition).getChildlist()
									.get(childPosition * 4+1).getValue());
							Config.myson = p;
						}
					} else {
					
						((CheckBox) v).setChecked(false);
						list.get(groupPosition).getChildlist().get(childPosition * 4+1)
								.setIsCheck(false);
						Config.myson = null;
					
					}

				}
			});
			cb3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
		
						((CheckBox) v).setChecked(true);
						for (int i = 0; i < list.get(groupPosition)
								.getChildlist().size(); i++) {
							list.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						for (int i = 0; i < glist.get(groupPosition)
								.getChildlist().size(); i++) {
							glist.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						list.get(groupPosition).getChildlist().get(childPosition * 4+2)
								.setIsCheck(true);

						PosPortAdapter.this.notifyDataSetChanged();
						Config.son = list.get(groupPosition).getChildlist()
								.get(childPosition * 4+2).getSon();
						Config.portindex=(childPosition+1)*4+2;
						if (Config.son != null) {
							context.startActivity(new Intent(context,
									PostSonList.class));
						} else {
							PortSon p = new PortSon();
							p.setId(list.get(groupPosition).getChildlist()
									.get(childPosition * 4+2).getId());
							p.setValue(list.get(groupPosition).getChildlist()
									.get(childPosition * 4+2).getValue());
							Config.myson = p;
						}
					} else {
					
						((CheckBox) v).setChecked(false);
						list.get(groupPosition).getChildlist().get(childPosition * 4+2)
								.setIsCheck(false);
						Config.myson = null;
					
					}

				}
			});
			cb4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
		
						((CheckBox) v).setChecked(true);
						for (int i = 0; i < list.get(groupPosition)
								.getChildlist().size(); i++) {
							list.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						for (int i = 0; i < glist.get(groupPosition)
								.getChildlist().size(); i++) {
							glist.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						list.get(groupPosition).getChildlist().get(childPosition * 4+3)
								.setIsCheck(true);

						PosPortAdapter.this.notifyDataSetChanged();
						Config.son = list.get(groupPosition).getChildlist()
								.get(childPosition * 4+3).getSon();
						Config.portindex=(childPosition+1)*4+3;
						if (Config.son != null) {
							context.startActivity(new Intent(context,
									PostSonList.class));
						} else {
							PortSon p = new PortSon();
							p.setId(list.get(groupPosition).getChildlist()
									.get(childPosition * 4+3).getId());
							p.setValue(list.get(groupPosition).getChildlist()
									.get(childPosition * 4+3).getValue());
							Config.myson = p;
						}
					} else {
					
						((CheckBox) v).setChecked(false);
						list.get(groupPosition).getChildlist().get(childPosition * 4+3)
								.setIsCheck(false);
						Config.myson = null;
					
					}

				}
			});
		} else {
			cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

					if (arg1) {
						list.get(groupPosition).getChildlist()
								.get(childPosition * 4).setIsCheck(true);
					} else {
						list.get(groupPosition).getChildlist()
								.get(childPosition * 4).setIsCheck(false);
					}

				}
			});

			cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (childPosition == childSize - 1) {
						if (childListSize - childPosition * 4 <= 0)
							return;
					}
					if (arg1) {
						list.get(groupPosition).getChildlist()
								.get(childPosition * 4 + 1).setIsCheck(true);
						// Toast.makeText(context, +childPosition*4+1+"被选",
						// 1000).show();
					} else {
						list.get(groupPosition).getChildlist()
								.get(childPosition * 4 + 1).setIsCheck(false);
					}
				}
			});

			cb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (childPosition == childSize - 1) {

						if (childListSize - childPosition * 4 <= 1)
							return;
					} else {

					}
					if (arg1) {
						list.get(groupPosition).getChildlist()
								.get(childPosition * 4 + 2).setIsCheck(true);

					} else {
						list.get(groupPosition).getChildlist()
								.get(childPosition * 4 + 2).setIsCheck(false);
					}
				}
			});
			cb4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

					if (childPosition == childSize - 1) {
						if (childListSize - childPosition * 4 <= 2)
							return;
					}
					if (arg1) {
						list.get(groupPosition).getChildlist()
								.get(childPosition * 4 + 3).setIsCheck(true);
					} else {
						list.get(groupPosition).getChildlist()
								.get(childPosition * 4 + 3).setIsCheck(false);
					}
				}
			});
		}
		// 判断选项是否选中
		/*
		 * if (list.get(groupPosition).getChildlist().get(childPosition)
		 * .isSeleck()) { cb1.setChecked(true); Toast.makeText(context,
		 * +childPosition + "2222", 1000).show(); } if
		 * (list.get(groupPosition).getChildlist().get(childPosition + 1)
		 * .isSeleck()) { cb2.setChecked(true); Toast.makeText(context,
		 * +childPosition + 1 + "3333", 1000).show(); } if
		 * (list.get(groupPosition).getChildlist().get(childPosition + 2)
		 * .isSeleck()) { cb3.setChecked(true); Toast.makeText(context,
		 * +childPosition + 2 + "44444", 1000).show(); } if
		 * (list.get(groupPosition).getChildlist().get(childPosition + 3)
		 * .isSeleck()) { cb4.setChecked(true); }
		 */
		for (int i = 0; i < list.get(groupPosition).getChildlist().size(); i++) {
			if (list.get(groupPosition).getChildlist().get(i).getIsCheck()) {
				int index = i / 4;
				if (childPosition == index) {
					int indexof = i - index * 4;
					switch (indexof) {
					case 0:
						cb1.setChecked(true);
						break;
					case 1:
						cb2.setChecked(true);
						break;
					case 2:

						cb3.setChecked(true);
						break;
					case 3:
						cb4.setChecked(true);
						break;
					default:
						break;
					}
				}
			}
		}
		return convertView;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, ViewGroup parent) {
		// Log.e(glist.get(groupPosition).getTitle(),glist.get(groupPosition).getChildlist().size()+"");
		inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.pos_port_parent_item, null);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView tv = (TextView) convertView.findViewById(R.id.tv1);
		tv.setId(groupPosition);
		if (list.get(groupPosition).isMore()) {
			tv.setText("收起");

		} else {
			tv.setText("更多");
		}
		if (glist.get(groupPosition).getChildlist().size() <= 4) {
			tv.setVisibility(View.GONE);
			tv.setText(list.get(groupPosition).getChildlist().size() + "");
		}
		CheckBox cba = (CheckBox) convertView.findViewById(R.id.item_cb_all);
		TextView text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView text3 = (TextView) convertView.findViewById(R.id.text3);
		TextView text4 = (TextView) convertView.findViewById(R.id.text4);
		final CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.item_cb1);
		final CheckBox cb2 = (CheckBox) convertView.findViewById(R.id.item_cb2);
		final CheckBox cb3 = (CheckBox) convertView.findViewById(R.id.item_cb3);
		final CheckBox cb4 = (CheckBox) convertView.findViewById(R.id.item_cb4);
		title.setText(list.get(groupPosition).getTitle());
		// Toast.makeText(context, +childSize-1+"", 1000).show();
		if (glist.get(groupPosition).getChildlist().size() == 0) {

			text1.setVisibility(View.INVISIBLE);
			text2.setVisibility(View.INVISIBLE);
			text3.setVisibility(View.INVISIBLE);
			text4.setVisibility(View.INVISIBLE);
			cb1.setVisibility(View.INVISIBLE);
			cb2.setVisibility(View.INVISIBLE);
			cb3.setVisibility(View.INVISIBLE);
			cb4.setVisibility(View.INVISIBLE);
		} else if (glist.get(groupPosition).getChildlist().size() == 1) {
			text1.setText(glist.get(groupPosition).getChildlist().get(0)
					.getValue());
			text2.setVisibility(View.INVISIBLE);
			text3.setVisibility(View.INVISIBLE);
			text4.setVisibility(View.INVISIBLE);
			cb2.setVisibility(View.INVISIBLE);
			cb3.setVisibility(View.INVISIBLE);
			cb4.setVisibility(View.INVISIBLE);
		} else if (glist.get(groupPosition).getChildlist().size() == 2) {
			text1.setText(glist.get(groupPosition).getChildlist().get(0)
					.getValue());
			text2.setText(glist.get(groupPosition).getChildlist().get(1)
					.getValue());
			text3.setVisibility(View.INVISIBLE);
			cb3.setVisibility(View.INVISIBLE);
			text4.setVisibility(View.INVISIBLE);
			cb4.setVisibility(View.INVISIBLE);
		} else if (glist.get(groupPosition).getChildlist().size() == 3) {
			text1.setText(glist.get(groupPosition).getChildlist().get(0)
					.getValue());
			text2.setText(glist.get(groupPosition).getChildlist().get(1)
					.getValue());
			text1.setText(glist.get(groupPosition).getChildlist().get(2)
					.getValue());
			text4.setVisibility(View.INVISIBLE);
			cb4.setVisibility(View.INVISIBLE);

		} else {
			text1.setText(glist.get(groupPosition).getChildlist().get(0)
					.getValue());
			text2.setText(glist.get(groupPosition).getChildlist().get(1)
					.getValue());
			text3.setText(glist.get(groupPosition).getChildlist().get(2)
					.getValue());
			text4.setText(glist.get(groupPosition).getChildlist().get(3)
					.getValue());

		}
		if (list.get(groupPosition).isSeleck())
			cba.setChecked(true);
		/*
		 * cba.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton arg0, boolean
		 * arg1) {
		 * 
		 * if (arg1) { for (PosItem ppc :
		 * list.get(groupPosition).getChildlist()) { ppc.setIsCheck(true);
		 * 
		 * } for (PosItem ppc : glist.get(groupPosition).getChildlist()) {
		 * ppc.setIsCheck(true);
		 * 
		 * } list.get(groupPosition).setSeleck(true); } else { for (PosItem ppc
		 * : list.get(groupPosition).getChildlist()) { ppc.setIsCheck(false); }
		 * for (PosItem ppc : glist.get(groupPosition).getChildlist()) {
		 * ppc.setIsCheck(false);
		 * 
		 * }
		 * 
		 * list.get(groupPosition).setSeleck(false); }
		 * PosPortAdapter.this.notifyDataSetChanged(); } });
		 */
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isExpanded) {
					list.get(v.getId()).setMore(false);
					listView.collapseGroup(groupPosition);

				} else {
					list.get(v.getId()).setMore(true);
					listView.expandGroup(groupPosition);

				}

			}
		});
		if (groupPosition == 1) {
			cb1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
		
						((CheckBox) v).setChecked(true);
						for (int i = 0; i < glist.get(groupPosition)
								.getChildlist().size(); i++) {
							glist.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						for (int i = 0; i < list.get(groupPosition)
								.getChildlist().size(); i++) {
							list.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						glist.get(groupPosition).getChildlist().get(0)
								.setIsCheck(true);
						Config.portindex=0;
						PosPortAdapter.this.notifyDataSetChanged();
						Config.son = glist.get(groupPosition).getChildlist()
								.get(0).getSon();
						if (Config.son != null) {
							context.startActivity(new Intent(context,
									PostSonList.class));
						} else {
							PortSon p = new PortSon();
							p.setId(glist.get(groupPosition).getChildlist()
									.get(0).getId());
							p.setValue(glist.get(groupPosition).getChildlist()
									.get(0).getValue());
							Config.myson = p;
						}
					} else {
					
						((CheckBox) v).setChecked(false);
						glist.get(groupPosition).getChildlist().get(0)
								.setIsCheck(false);
						Config.myson = null;
					
					}

				}
			});
			cb2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
						((CheckBox) v).setChecked(true);
						for (int i = 0; i < glist.get(groupPosition)
								.getChildlist().size(); i++) {
							glist.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						for (int i = 0; i < list.get(groupPosition)
								.getChildlist().size(); i++) {
							list.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						glist.get(groupPosition).getChildlist().get(1)
								.setIsCheck(true);
						Config.portindex=1;
						PosPortAdapter.this.notifyDataSetChanged();
						Config.son = glist.get(groupPosition).getChildlist()
								.get(1).getSon();
						if (Config.son != null) {
							context.startActivity(new Intent(context,
									PostSonList.class));
						} else {
							PortSon p = new PortSon();
							p.setId(glist.get(groupPosition).getChildlist()
									.get(1).getId());
							p.setValue(glist.get(groupPosition).getChildlist()
									.get(1).getValue());
							Config.myson = p;
						}
					} else {
						((CheckBox) v).setChecked(false);
						glist.get(groupPosition).getChildlist().get(1)
								.setIsCheck(false);
						Config.myson = null;
					}

				}
			});
			cb3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
						((CheckBox) v).setChecked(true);
						for (int i = 0; i < glist.get(groupPosition)
								.getChildlist().size(); i++) {
							glist.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						for (int i = 0; i < list.get(groupPosition)
								.getChildlist().size(); i++) {
							list.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						glist.get(groupPosition).getChildlist().get(2)
								.setIsCheck(true);
						Config.portindex=2;
						PosPortAdapter.this.notifyDataSetChanged();
						Config.son = glist.get(groupPosition).getChildlist()
								.get(2).getSon();
						if (Config.son != null) {
							context.startActivity(new Intent(context,
									PostSonList.class));
						} else {
							PortSon p = new PortSon();
							p.setId(glist.get(groupPosition).getChildlist()
									.get(2).getId());
							p.setValue(glist.get(groupPosition).getChildlist()
									.get(2).getValue());
							Config.myson = p;
						}
					} else {
						((CheckBox) v).setChecked(false);
						glist.get(groupPosition).getChildlist().get(2)
								.setIsCheck(false);
						Config.myson = null;
					}

				}
			});
			
			
			cb4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
						((CheckBox) v).setChecked(true);
						for (int i = 0; i < glist.get(groupPosition)
								.getChildlist().size(); i++) {
							glist.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						for (int i = 0; i < list.get(groupPosition)
								.getChildlist().size(); i++) {
							list.get(groupPosition).getChildlist().get(i)
									.setIsCheck(false);
						}
						glist.get(groupPosition).getChildlist().get(3)
								.setIsCheck(true);
						Config.portindex=3;
						PosPortAdapter.this.notifyDataSetChanged();
						Config.son = glist.get(groupPosition).getChildlist()
								.get(3).getSon();
						if (Config.son != null) {
							context.startActivity(new Intent(context,
									PostSonList.class));
						} else {
							PortSon p = new PortSon();
							p.setId(glist.get(groupPosition).getChildlist()
									.get(3).getId());
							p.setValue(glist.get(groupPosition).getChildlist()
									.get(3).getValue());
							Config.myson = p;
						}
					} else {
						((CheckBox) v).setChecked(false);
						glist.get(groupPosition).getChildlist().get(3)
								.setIsCheck(false);
						Config.myson = null;
					}

				}
			});
		} else {
			cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

					if (arg1) {
						glist.get(groupPosition).getChildlist().get(0)
								.setIsCheck(true);

					} else {
						glist.get(groupPosition).getChildlist().get(0)
								.setIsCheck(false);
					}

				}
			});

			cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (arg1) {
						glist.get(groupPosition).getChildlist().get(1)
								.setIsCheck(true);
					} else {
						glist.get(groupPosition).getChildlist().get(1)
								.setIsCheck(false);
					}

				}
			});

			cb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (arg1) {
						glist.get(groupPosition).getChildlist().get(2)
								.setIsCheck(true);
					} else {
						glist.get(groupPosition).getChildlist().get(2)
								.setIsCheck(false);
					}

				}
			});

			cb4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (arg1) {
						glist.get(groupPosition).getChildlist().get(3)
								.setIsCheck(true);
					} else {
						glist.get(groupPosition).getChildlist().get(3)
								.setIsCheck(false);
					}

				}
			});

		}

		for (int i = 0; i < glist.get(groupPosition).getChildlist().size(); i++) {
			if (glist.get(groupPosition).getChildlist().get(i).getIsCheck()) {

				switch (i) {
				case 0:
					cb1.setChecked(true);

					break;
				case 1:
					cb2.setChecked(true);
					break;
				case 2:
					cb3.setChecked(true);
					break;
				case 3:
					cb4.setChecked(true);
					break;
				default:
					break;

				}
			}
		}
		return convertView;
	}
}
