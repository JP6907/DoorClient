package com.example.chat.Activity;

import java.util.List;

import com.example.chat.Activity.FriendsReqMsg.MsgState;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.neo.huikaimen.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import neo.door.cache.FriendCache;
import neo.door.cache.FriendsReqMsgCache;
import neo.door.usermanager.User;
import neo.door.utils.MyToast;

public class FriendsManage 
{
	private MyToast myToast;
	private FriendsDateBaseHelper baseHelper;
	private ProgressDialog progressDialog;
	private Context mContext;
	
	public FriendsManage(Context context)
	{
		this.mContext=context;
	}
	/**
	 * 添加朋友
	 * @param toAddUsername
	 * @param reason
	 */
	public void addContact(final String toAddUsername,String reason)
	{
		if(EMClient.getInstance().getCurrentUser().equals(toAddUsername))
		{
			new EaseAlertDialog(mContext,R.string.not_add_myself).show();
			return;
		}
		
//		if(baseHelper.search(toAddUsername))
//		{
//			new EaseAlertDialog(mContext, R.string.This_user_is_already_your_friend).show();
//			return;
//		}
//		//参数为要添加的好友的username和添加理由
//		try 
//		{
//			EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
//		} catch (HyphenateException e) 
//		{
//			e.printStackTrace();
//			myToast.show("发送消息失败", Toast.LENGTH_SHORT);
//		}

		progressDialog = new ProgressDialog(mContext);
		String stri = mContext.getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		new Thread(new Runnable() {
			public void run() {

				try {
					// demo use a hardcode reason here, you need let user to
					// input if you like
					String s = mContext.getResources().getString(R.string.Add_a_friend);
					EMClient.getInstance().contactManager().addContact(toAddUsername, s);
					((Activity)mContext).runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = mContext.getResources().getString(R.string.send_successful);
							Toast.makeText(mContext, s1, Toast.LENGTH_LONG).show();
						}
					});
				} catch (final Exception e) {
					((Activity)mContext).runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = mContext.getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(mContext, s2 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
	}
	/**
	 * 接受好友请求
	 * @param buttonAgree
	 * @param buttonRefuse
	 * @param friendName
	 */
	public void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final FriendsReqMsg reqMsg,
			final List<FriendsReqMsg> reqList) {
		final ProgressDialog pd = new ProgressDialog(mContext);
		String str1 = mContext.getResources().getString(R.string.Are_agree_with);
		final String str2 = mContext.getResources().getString(R.string.Has_agreed_to);
		final String str3 = mContext.getResources().getString(R.string.Agree_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// call api
				try {
//					if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {//accept be friends
						EMClient.getInstance().contactManager().acceptInvitation(reqMsg.getName());
//						baseHelper.insert(reqMsg.getName());
//						baseHelper.closeDB();
//					} else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //accept application to join group
//						EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
//					} else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
//					    EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
//					}
                    reqMsg.setState(MsgState.AGREED);
                    FriendsReqMsgCache.writeCache(reqList);//更新好友通知消息
                    List<User> contactList= FriendCache.readCache();
                    User user=new User(reqMsg.getName());
                    user.setImgPath("");
                    user.setNickname("");
                    contactList.add(user);
                    FriendCache.writeCache(contactList);
//                    // update database
//                    ContentValues values = new ContentValues();
//                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
//                    messgeDao.updateMessage(msg.getId(), values);
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							buttonAgree.setText(str2);
							buttonAgree.setBackgroundDrawable(null);
							buttonAgree.setEnabled(false);
							buttonRefuse.setVisibility(View.INVISIBLE);
						}
					});
				} catch (final Exception e) {
					((Activity) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(mContext, str3 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});

				}
			}
		}).start();
	}
	
	/**
	 * 拒绝好友请求
	 * @param buttonAgree
	 * @param buttonRefuse
	 * @param friendsName
	 */
	 public void refuseInvitation(final Button buttonAgree, final Button buttonRefuse, final FriendsReqMsg reqMsg,
				final List<FriendsReqMsg> reqList) {
	        final ProgressDialog pd = new ProgressDialog(mContext);
	        String str1 = mContext.getResources().getString(R.string.Are_refuse_with);
	        final String str2 = mContext.getResources().getString(R.string.Has_refused_to);
	        final String str3 = mContext.getResources().getString(R.string.Refuse_with_failure);
	        pd.setMessage(str1);
	        pd.setCanceledOnTouchOutside(false);
	        pd.show();

	        new Thread(new Runnable() {
	            public void run() {
	                // call api
	                try {
//	                    if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {//decline the invitation
	                        EMClient.getInstance().contactManager().declineInvitation(reqMsg.getName());
//	                    } else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { //decline application to join group
//	                        EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
//	                    } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
//	                        EMClient.getInstance().groupManager().declineInvitation(msg.getGroupId(), msg.getGroupInviter(), "");
//	                    }
	                    reqMsg.setState(MsgState.REFUSED);
	                    FriendsReqMsgCache.writeCache(reqList);
	                    // update database
//	                    ContentValues values = new ContentValues();
//	                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
//	                    messgeDao.updateMessage(msg.getId(), values);
	                    ((Activity) mContext).runOnUiThread(new Runnable() {

	                        @Override
	                        public void run() {
	                            pd.dismiss();
	                            buttonRefuse.setText(str2);
	                            buttonRefuse.setBackgroundDrawable(null);
	                            buttonRefuse.setEnabled(false);

	                            buttonAgree.setVisibility(View.INVISIBLE);
	                        }
	                    });
	                } catch (final Exception e) {
	                    ((Activity) mContext).runOnUiThread(new Runnable() {

	                        @Override
	                        public void run() {
	                            pd.dismiss();
	                            Toast.makeText(mContext, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
	                        }
	                    });

	                }
	            }
	        }).start();
	    }
	
}
