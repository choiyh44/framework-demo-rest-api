package kr.co.ensmart.frameworkdemo.common.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ClientInfo {
	public final static String CLIENT_INFO_HEADER_NAME = "X-ClientInfo";
	
	private String dbLocaleLanguage;
	private String dbTimeZone;
	private String javaTimeZone;
	
	public static ClientInfo defaultValue() {
		ClientInfo info = new ClientInfo();
		info.setDbLocaleLanguage("en");
		info.setDbTimeZone("UTC");
		info.setJavaTimeZone("UTC");
		return info;
	}
}
