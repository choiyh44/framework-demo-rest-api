package kr.co.ensmart.frameworkdemo.common.token;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecretKeyProvider {
	private final byte[] key = "PwowAdDhxJlCrnnQfpW/78y3I8I3YWUTc4WBfP2Jgag=".getBytes();
	private final String algorithm = "HmacSHA256";
	
	private final SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(new String(key)), algorithm);
	
    public SecretKey getSecretKey() {
        return secretKey;
    }
}
