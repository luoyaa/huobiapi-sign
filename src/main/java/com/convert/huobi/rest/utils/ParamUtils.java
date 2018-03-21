package com.convert.huobi.rest.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParamUtils {
	
	private static SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
	public static String getGetParam(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			if (sb.length() > 1) {
				sb.append("&");
			}
			sb.append(key).append("=").append(map.get(key));
		}
		return sb.toString();
	}

	/**
	 * ����������Ԫ�����򣬲����ա�����=����ֵ����ģʽ�á�&���ַ�ƴ�ӳ��ַ���
	 * 
	 * @param params
	 *            ��Ҫ���򲢲����ַ�ƴ�ӵĲ�����
	 * @return ƴ�Ӻ��ַ���
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = "";
			try {
				value = URLEncoder.encode(params.get(key),"UTF-8");
				key = URLEncoder.encode(key,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (i == keys.size() - 1) {// ƴ��ʱ�����������һ��&�ַ�
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	public static String getUTCDate() {
		// ȡ�ñ���ʱ�䣺
		Calendar cal = Calendar.getInstance();
		// ȡ��ʱ��ƫ������
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		// ȡ������ʱ�
		int dstOffset = cal.get(Calendar.DST_OFFSET);
		
		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		String rtn = "";
	
			rtn = UTC_FORMAT.format(cal.getTime());
		
		return rtn;
	}
}
