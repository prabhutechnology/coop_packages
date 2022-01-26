package com.prabhutech.coop.wallet.core.api.utils.auth;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;

public class JWTDecoder {
    public static String decodeToken(String token) {
        try {
            JWT jwt = new JWT(token);
            Claim claim = jwt.getClaim("Id");
            return claim.asString();
        } catch (DecodeException e) {
            return null;
        }
    }

    public static String decodeExpireTime(String token) {
        try {
            JWT jwt = new JWT(token);
            Claim claim = jwt.getClaim("exp");
            return claim.asString();
        } catch (DecodeException e) {
            return null;
        }
    }
}
