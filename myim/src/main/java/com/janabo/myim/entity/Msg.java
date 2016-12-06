package com.janabo.myim.entity;

/**
 * 消息实体类
 */
public class Msg {

	public static final int TYPE_RECEIVED = 0;
	public static final int TYPE_SENT = 1;

	private String content; // 消息内容
	private int type;		// 消息类型，可选值是上面的两个常量
	private String code;
	private String msg;

	public Msg(String msg, int type) {
		this.msg = msg;
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public int getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setType(int type) {
		this.type = type;
	}
}
