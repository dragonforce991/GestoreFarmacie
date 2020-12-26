package RestServices;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.ZoneId;

import java.sql.Date;
import io.jsonwebtoken.*;
import com.google.gson.*;
import Model.User;
//import java.util.Date;
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
	
	public static LocalDate fromDateToLocalDate(Date d) {
			if(d == null ) return null;
			return d.toLocalDate();
	}
	public static void setStatement(PreparedStatement stmt, int index, Float value) throws SQLException {
		if(value != null) {
			stmt.setFloat(index, value);
		}else {
			stmt.setNull(index, Types.VARCHAR);
		}
	}
	public static void setStatement(PreparedStatement stmt, int index, String value) throws SQLException {
		if(value != null) {
			stmt.setString(index, value);
		}else {
			stmt.setNull(index, Types.VARCHAR);
		}
	}
	public static void setStatement(PreparedStatement stmt, int index, Date value) throws SQLException {
		if(value != null) {
			//System.out.println(value);
			
			stmt.setObject(index, fromDateToLocalDate(value));
		}else {
			stmt.setNull(index, Types.VARCHAR);
		}
	}
	public static void setStatement(PreparedStatement stmt, int index, Integer value) throws SQLException {
		if(value != null) {
			stmt.setInt(index, value);
		}else {
			stmt.setNull(index, Types.VARCHAR);
		}
	}
	public static void setStatement(PreparedStatement stmt, int index, LocalDate value) throws SQLException {
		if(value != null) {
			stmt.setObject(index, value);
		}else {
			stmt.setNull(index, Types.VARCHAR);
		}
	}
	public static void setStatement(PreparedStatement stmt, int index, Boolean value) throws SQLException {
		if(value != null) {
			stmt.setBoolean(index, value);
		}else {
			stmt.setBoolean(index, false);
		}
	}
}
