package com.fanlitou.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	
	String key;
	String initVector;
	
	public AES(String key){
		this.key = key;
		this.initVector = key;
	}
	
    public String encrypt(String value) {
        try {
        	int length = 16;
        	int textLength = value.getBytes("UTF-8").length;
        	for (int i = 0; i < length - (textLength % length); i++) {
				value += "\0";
			}
            IvParameterSpec iv = new IvParameterSpec(this.initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(this.key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return parseByte2HexStr(encrypted).trim();
        } catch (Exception ex) {
        	ex.printStackTrace();
            return null;
        }
    }

    public String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(this.initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(this.key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(parseHexStr2Byte(encrypted));

            return new String(original).trim();
        } catch (Exception ex) {
        	ex.printStackTrace();
            return null;
        }
    }
    
    /**将16进制转换为二进制 
     * @param hexStr 
      * @return 
      */   
     public static byte[] parseHexStr2Byte(String hexStr) {   
             if (hexStr.length() < 1)   
                     return null;   
             byte[] result = new byte[hexStr.length()/2];   
             for (int i = 0;i< hexStr.length()/2; i++) {   
                     int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);   
                     int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);   
                     result[i] = (byte) (high * 16 + low);   
             }   
             return result;   
     }  
     
     /**将二进制转换成16进制 
      * @param buf 
       * @return 
       */   
      public static String parseByte2HexStr(byte buf[]) {   
              StringBuffer sb = new StringBuffer();   
              for (int i = 0; i < buf.length; i++) {   
                      String hex = Integer.toHexString(buf[i] & 0xFF);   
                      if (hex.length() == 1) {   
                              hex = '0' + hex;   
                      }   
                      sb.append(hex);   
              }   
              return sb.toString();   
      }   

}