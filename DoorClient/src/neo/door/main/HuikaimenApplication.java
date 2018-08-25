package neo.door.main;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;

import android.app.Application;
import neo.door.cache.FriendCache;
import neo.door.usermanager.User;

public class HuikaimenApplication extends Application{
	
	EaseUI easeUI= EaseUI.getInstance();
    @Override
    public void onCreate() {
        super.onCreate();
     EMOptions options = new EMOptions();
     // 默认添加好友时，是不需要验证的，改成需要验证
     options.setAcceptInvitationAlways(false);
     options.setAutoLogin(true);
     //初始化
     EaseUI.getInstance().init(this, options);
     setEaseUIProviders();
     
    }
    
    protected void setEaseUIProviders() {
    	// set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUserProfileProvider() {
            
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

	protected EaseUser getUserInfo(String username) 
	{
		List<User> userList = null;
		try {
			userList=FriendCache.readCache();
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(User user :userList)
			if(user.getUsername().equals(username))
			{
			EaseUser easeUser =new EaseUser(username);
			easeUser.setAvatar(user.getAvatar());
			easeUser.setNickname(user.getNickname());
		    return easeUser;
			}
		return null;
	}

}
