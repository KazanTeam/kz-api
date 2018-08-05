package com.kazan.cognito;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

public class CognitoJWTParser {

    private static final int HEADER = 0;

    private static final int PAYLOAD = 1;

    private static final int SIGNATURE = 2;

    private static final int JWT_PARTS = 3;

    public static JSONObject getHeader(String jwt) {
        try {
            validateJWT(jwt);
            Base64.Decoder dec= Base64.getDecoder();
            final byte[] sectionDecoded = dec.decode(jwt.split("\\.")[HEADER]);
            final String jwtSection = new String(sectionDecoded, "UTF-8");
            return new JSONObject(jwtSection);
        } catch (final UnsupportedEncodingException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (final Exception e) {
            throw new InvalidParameterException("error in parsing JSON");
        }
    }

    public static JSONObject getPayload(String jwt) {
        try {
            validateJWT(jwt);
            Base64.Decoder dec= Base64.getDecoder();
            final String payload = jwt.split("\\.")[PAYLOAD];
            final byte[] sectionDecoded = dec.decode(payload);
            final String jwtSection = new String(sectionDecoded, "UTF-8");
            return new JSONObject(jwtSection);
        } catch (final UnsupportedEncodingException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (final Exception e) {
            throw new InvalidParameterException("error in parsing JSON");
        }
    }

    public static String getSignature(String jwt) {
        try {
            validateJWT(jwt);
            Base64.Decoder dec= Base64.getDecoder();
            final byte[] sectionDecoded = dec.decode(jwt.split("\\.")[SIGNATURE]);
            return new String(sectionDecoded, "UTF-8");
        } catch (final Exception e) {
            throw new InvalidParameterException("error in parsing JSON");
        }
    }

    public static String getClaim(String jwt, String claim) {
        try {
            final JSONObject payload = getPayload(jwt);
            final Object claimValue = payload.get(claim);

            if (claimValue != null) {
                return claimValue.toString();
            }

        } catch (final Exception e) {
            throw new InvalidParameterException("invalid token");
        }
        return null;
    }

    public static void validateJWT(String jwt) {
        // Check if the the JWT has the three parts
        final String[] jwtParts = jwt.split("\\.");
        if (jwtParts.length != JWT_PARTS) {
            throw new InvalidParameterException("not a JSON Web Token");
        }
    }
}
