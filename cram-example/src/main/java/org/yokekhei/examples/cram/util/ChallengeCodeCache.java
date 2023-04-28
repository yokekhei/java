package org.yokekhei.examples.cram.util;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ChallengeCodeCache {

    private static final Integer VALIDITY_SECONDS = 600;

    private LoadingCache<String, String> challengeCodeCache;

    public ChallengeCodeCache() {
        challengeCodeCache = CacheBuilder.newBuilder().expireAfterWrite(VALIDITY_SECONDS, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        return null;
                    }
                });
    }

    public String generateChallengeCode(String key) throws Exception {
        return generateCode(key);
    }

    public byte[] generateResponseCode(String key, String base64PublicKey) {
        String challengeCode = getCode(key);
        byte[] responseCodeBytes = StringUtil.xor(
                StringUtil.reverse(Base64.getDecoder().decode(base64PublicKey.getBytes())),
                challengeCode.toString().getBytes());

        return responseCodeBytes;
    }

    private String generateCode(String key) throws Exception {
        String code = RandomStringUtils.randomAlphabetic(6);

        if (code == null) {
            throw new Exception("Challenge code is null");
        }

        challengeCodeCache.put(key, code);

        return code;
    }

    private String getCode(String key) {
        try {
            return challengeCodeCache.get(key);
        } catch (Exception e) {
            return null;
        }
    }

//    private void clearCode(String key) {
//        challengeCodeCache.invalidate(key);
//    }

}
