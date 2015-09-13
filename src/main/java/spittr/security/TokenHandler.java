package spittr.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spittr.entity.Spitter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TokenHandler {
	private static final Logger logger = LoggerFactory.getLogger(TokenHandler.class);

	private static final String HMAC_ALGO = "HmacSHA256";
	private static final String SEPARATOR = ".";
	private static final String SEPARATOR_SPLITTER = "\\.";

	private final Mac hmac;	
	private ObjectMapper mapper;
	
	public TokenHandler(byte[] secretKey) {
		try {
			this.mapper = new ObjectMapper();
			hmac = Mac.getInstance(HMAC_ALGO);
			hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
		} catch (InvalidKeyException e) {
			throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
		
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
		}
	}

	public Spitter parseUserFromToken(String token) {
		final String[] parts = token.split(SEPARATOR_SPLITTER);
		if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
			try {
				final byte[] userBytes = fromBase64(parts[0]);
				final byte[] hash = fromBase64(parts[1]);
				
				logger.info("userBytes: {}", new String(userBytes));

				boolean validHash = Arrays.equals(createHmac(userBytes), hash);
				if (validHash) {
					final Spitter spitter = fromJSON(userBytes);
//					if (new Date().getTime() < user.getExpires()) {
					return spitter;
//					}
				}
			} catch (IllegalArgumentException e) {
				//log tempering attempt here
			}
		}
		return null;
	}

	public String createTokenForUser(Spitter spitter) {
		byte[] userBytes = toJSON(spitter);
		byte[] hash = createHmac(userBytes);
		final StringBuilder sb = new StringBuilder(170);
		sb.append(toBase64(userBytes));
		sb.append(SEPARATOR);
		sb.append(toBase64(hash));
		
		logger.info(sb.toString());
		
		return sb.toString();
	}

	/**
	 * This requires an Entity declaration
	 * What if there are weird inputs????
	 * @param userBytes
	 * @return
	 */
	private Spitter fromJSON(final byte[] bytes) {
		Spitter spitter = null; 
		try{
			spitter = mapper.readValue(new ByteArrayInputStream(bytes), Spitter.class);
			return spitter;
			
		} catch (JsonMappingException jme) {
			logger.error("from JSON JsonMappingException", jme);
			return null;
		} catch (JsonParseException jpe) {
			logger.error("from JSON JsonParseException", jpe);
			return null;
		} catch (IOException ioe) {
			logger.error("from JSON IOException", ioe);
			return null;
		}				
	}

	/**
	 * This requires an entity declaration
	 * @param user
	 * @return
	 */
	private byte[] toJSON(Spitter spitter) {
		try {
			return mapper.writeValueAsBytes(spitter);			
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private String toBase64(byte[] content) {
		return DatatypeConverter.printBase64Binary(content);
	}

	private byte[] fromBase64(String content) {
		return DatatypeConverter.parseBase64Binary(content);
	}

	// synchronized to guard internal hmac object
	private synchronized byte[] createHmac(byte[] content) {
		return hmac.doFinal(content);
	}
}
