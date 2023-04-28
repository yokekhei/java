package org.yokekhei.examples.cram.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class PublicKeyCache {

    private static final Integer VALIDITY_SECONDS = 3600;

    private LoadingCache<String, String> publicKeyCache;
    private PkcsUtil pkcsUtil;

    public PublicKeyCache(PkcsUtil pkcsUtil) {
        this.pkcsUtil = pkcsUtil;
        publicKeyCache = CacheBuilder.newBuilder().expireAfterWrite(VALIDITY_SECONDS, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String email) {
                        return "";
                    }
                });
    }

    public void enroll(String email, String base64PublicKey) {
        try {
            pkcsUtil.getPublicKey(base64PublicKey);
            publicKeyCache.put(email, base64PublicKey);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException: " + e.getMessage());
        } catch (NoSuchProviderException e) {
            System.err.println("NoSuchProviderException: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.err.println("InvalidKeySpecException: " + e.getMessage());
        }
    }

    public String retrieve(String email) {
        try {
            return publicKeyCache.get(email);
        } catch (Exception e) {
            System.err.println("Failed to retrieve public key - " + e.getMessage());
            return "";
        }
    }

}
