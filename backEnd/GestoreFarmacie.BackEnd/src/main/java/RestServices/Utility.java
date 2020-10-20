package RestServices;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import io.jsonwebtoken.*;
import com.google.gson.*;

import Model.User;
import RestServices.Login.LoginWrapper;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

public class Utility {

	final static String SECRET_KEY = "secretKey";
	public static String createJWT(String id, String issuer, String subject, long ttlMillis) {
		  
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);

	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

	    JwtBuilder builder = Jwts.builder().setId(id)
	            .setIssuedAt(now)
	            .setSubject(subject)
	            .setIssuer(issuer)
	            .signWith(signatureAlgorithm, signingKey);
	  
	    if (ttlMillis > 0) {
	        long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }  
	  
	    return builder.compact();
	}
	
	
	public static Claims verify(String jwt) throws Exception {
	    //This line will throw an exception if it is not a signed JWS (as expected)
		try {
	    Claims claims = Jwts.parser()
	            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
	            .parseClaimsJws(jwt).getBody();
	    return claims;
		}catch(Exception e) {
			throw e;
		}
	}
	
	public static User getUserFromJWT(String jwt) {
		Claims claims = Jwts.parser()
	            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
	            .parseClaimsJws(jwt).getBody();
		Gson g = new Gson();
		User user= g.fromJson(claims.getSubject(), User.class);
		return user;
	}
}
