package org.yokekhei.examples.cram.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import com.nimbusds.jose.util.X509CertUtils;

public class PkcsUtil {

    public static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;
    public static final Integer KEY_SIZE = 2048;
    public static final String KEYPAIR_GENERATOR_ALGORITHM = "RSA"; // ECDSA
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA"; // SHA256withECDSA

    public PkcsUtil() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public KeyPair generateKeyPair()
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEYPAIR_GENERATOR_ALGORITHM, PROVIDER);
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());

//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", PROVIDER);
//        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
//        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());

        return keyPairGenerator.generateKeyPair();
    }

    public PublicKey getPublicKey(String base64Key)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        byte[] decodedKeyBytes = Base64.getDecoder().decode(base64Key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEYPAIR_GENERATOR_ALGORITHM, PROVIDER);
//      KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", PROVIDER);

        return keyFactory.generatePublic(keySpec);
    }

    public byte[] getSignature(byte[] payload, PrivateKey privateKey)
            throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {
        Signature signatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM);

        signatureInstance.initSign(privateKey);
        signatureInstance.update(payload);

        return signatureInstance.sign();
    }

    public boolean verify(byte[] payload, byte[] signature, PublicKey publicKey)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature signatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM);
        signatureInstance.initVerify(publicKey);
        signatureInstance.update(payload);

        return signatureInstance.verify(signature);
    }

    public void writePublicKey(PublicKey publicKey) throws IOException {
        StringWriter writer = new StringWriter();
        PemWriter pemWriter = new PemWriter(writer);
        pemWriter.writeObject(new PemObject("PUBLIC KEY", publicKey.getEncoded()));
        pemWriter.flush();
        pemWriter.close();
        String x509Key = writer.toString();

        FileOutputStream fos1 = new FileOutputStream("public-key.x509");
        fos1.write(x509Key.getBytes());
        fos1.flush();
        fos1.close();
    }

    public void writePrivateKey(PrivateKey privateKey) throws IOException {
        StringWriter writer = new StringWriter();
        PemWriter pemWriter = new PemWriter(writer);
        pemWriter.writeObject(new PemObject("RSA PRIVATE KEY", privateKey.getEncoded()));
        pemWriter.flush();
        pemWriter.close();
        String pkcs8Key = writer.toString();

        FileOutputStream fos1 = new FileOutputStream("private-key.pkcs8");
        fos1.write(pkcs8Key.getBytes());
        fos1.flush();
        fos1.close();
    }

    public PublicKey getPublicKeyFromX509Cert(String encodedCert) {
        Provider jcaProvider = Security.getProvider(PROVIDER);
        X509CertUtils.setProvider(jcaProvider);

        X509Certificate cert = X509CertUtils.parse(encodedCert);
        PublicKey publicKey = cert.getPublicKey();

        if (publicKey instanceof RSAPublicKey) {
            System.out.println("We have an RSA public key");
        } else if (publicKey instanceof ECPublicKey) {
            System.out.println("We have an EC public key");
        } else {
            System.err.println("Unknown key type, should never happen");
        }

        return publicKey;
    }

}
