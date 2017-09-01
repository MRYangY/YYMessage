package com.example.yymessage.globle;

import com.example.yymessage.provider.GroupProvider;

import android.net.Uri;

public class Constant {
	public interface URI{
		Uri URI_SMS_CONVERSATION=Uri.parse("content://sms/conversations");
		Uri URI_SMS=Uri.parse("content://sms");
		Uri URI_GROUP_INSERT=Uri.parse(GroupProvider.BASE_URI+"/groups/insert");
		Uri URI_GROUP_QUERY=Uri.parse(GroupProvider.BASE_URI+"/groups/query");
		Uri URI_GROUP_DELETE=Uri.parse(GroupProvider.BASE_URI+"/groups/delete");
		Uri URI_GROUP_UPDATE=Uri.parse(GroupProvider.BASE_URI+"/groups/update");
		Uri URI_THREAD_GROUP_INSERT=Uri.parse(GroupProvider.BASE_URI+"/thread_group/insert");
		Uri URI_THREAD_GROUP_QUERY=Uri.parse(GroupProvider.BASE_URI+"/thread_group/query");
		Uri URI_THREAD_GROUP_DELETE=Uri.parse(GroupProvider.BASE_URI+"/thread_group/delete");
		Uri URI_THREAD_GROUP_UPDATE=Uri.parse(GroupProvider.BASE_URI+"/thread_group/update");
	}
	public interface SMS{
		int TYPE_RECEIVE=1;
		int TYPE_SEND=2;
	}
}
