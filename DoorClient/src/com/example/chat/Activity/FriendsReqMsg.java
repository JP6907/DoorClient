package com.example.chat.Activity;

public class FriendsReqMsg 
{
	private String name;
	private MsgState state;
	private String reason;
	
	public void setName(String name)
	{
		this.name=name;
	}
	public String getName()
	{
		return this.name;
	}
	public void setState(MsgState state)
	{
		this.state=state;
	}
	public MsgState getState()
	{
		return this.state;
	}
	public String getReason() 
	{
		return reason;
	}
	public void setReason(String reason) 
	{
		this.reason = reason;
	}
	
	public enum MsgState {
		// ==contact
		/** being invited */
		BEINVITEED,
		/** being refused */
		BEREFUSED,
		/** remote user already agreed */
		BEAGREED, 
		
		REFUSED, 
		
		AGREED

//		// ==group application
//		/** remote user apply to join */
//		BEAPPLYED,
//		/** you have agreed to join */
//		AGREED,
//		/** you refused the join request */
//		REFUSED,
//
//		// ==group invitation
//		/** received remote user's invitation **/
//		GROUPINVITATION,
//		/** remote user accept your invitation **/
//		GROUPINVITATION_ACCEPTED,
//		/** remote user declined your invitation **/
//		GROUPINVITATION_DECLINED
	}

}
