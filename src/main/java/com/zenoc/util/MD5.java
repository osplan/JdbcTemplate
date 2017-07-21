package com.zenoc.util;

import java.security.MessageDigest;

public class MD5 {

	
	public final static String encode(String plainText, Boolean upperCase){
		return encode(plainText, upperCase, "utf-8");
	}
	/**
	 * md5加密
	 * @param plainText
	 * @param upperCase true为大写，false为
	 * @return
	 */
	public final static String encode(String plainText, Boolean upperCase, String charset){
		String str = "";
		if(upperCase){
			str = encodeToUpper(plainText,charset);
		}else{
			str = encodeToLower(plainText,charset);
		}
		return str;
	}

	
	public final static String encode(String plainText){
		return encode(plainText, "utf-8");
	}
	/**
	 * 
	 * @param plainText
	 * @return
	 */
	public final static String encode(String plainText, String charset){
		return encodeToLower(plainText,charset);		
	}
	
	private static String encodeToLower(String plainText, String charset) {
		String str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes(charset));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	private final static String encodeToUpper(String s, String charset) {
        char hexDigits[] = { '0', '1', '2', '3', '4',                    
                             '5', '6', '7', '8', '9',                    
                             'A', 'B', 'C', 'D', 'E', 'F'};              
        try {                                                            
            byte[] btInput = s.getBytes(charset);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput); 
            byte[] md = mdInst.digest();
            int j = md.length;                                           
            char str[] = new char[j * 2];                                
            int k = 0;                                                   
            for (int i = 0; i < j; i++) {                                
                byte byte0 = md[i];                                      
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];                 
                str[k++] = hexDigits[byte0 & 0xf];                       
            }                                                            
            return new String(str);
        }                                                                
        catch (Exception e) {
            e.printStackTrace();                                         
            return null;                                                 
        }                                                                
    }
}



