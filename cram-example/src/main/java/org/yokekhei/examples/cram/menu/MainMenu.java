package org.yokekhei.examples.cram.menu;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Scanner;

import org.yokekhei.examples.cram.util.ChallengeCodeCache;
import org.yokekhei.examples.cram.util.PkcsUtil;
import org.yokekhei.examples.cram.util.PublicKeyCache;
import org.yokekhei.examples.cram.util.StringUtil;

public class MainMenu extends Menu {

    private boolean exit;
    private PkcsUtil pkcsUtil;
    private PublicKeyCache publicKeyCache;
    private ChallengeCodeCache challengeCodeCache;
    private KeyPair keyPair;

    private enum MenuOption {
        EXIT, GENERATE_KEY_PAIR, ENROLL_PUBLIC_KEY, GET_CHALLENGE_CODE, COMPUTE_SIGN_RESPONSE_CODE, VERIFY_SIGNATURE
    };

    public MainMenu(Scanner scanner) {
        super(scanner);
        pkcsUtil = new PkcsUtil();
        publicKeyCache = new PublicKeyCache(pkcsUtil);
        challengeCodeCache = new ChallengeCodeCache();
    }

    @Override
    public void run() {
        while (!exit) {
            print();

            int choice = getInput(MenuOption.EXIT.ordinal(), MenuOption.VERIFY_SIGNATURE.ordinal());
            performAction(MenuOption.values()[choice]);
        }
    }

    @Override
    protected void print() {
        System.out.println(System.lineSeparator() + "Please make a selection: ");
        System.out.println("1) FE to generate key pair");
        System.out.println("2) FE to enroll public key to BE");
        System.out.println("3) FE to get challenge code from BE");
        System.out.println("4) FE to compute and sign response code");
        System.out.println("5) BE to verify signature");
        System.out.println("0) Exit");
    }

    private void performAction(MenuOption choice) {
        switch (choice) {
        case EXIT:
            exit = true;
            System.out.println("Thank you for using our application.");
            break;

        case GENERATE_KEY_PAIR:
            generateKeyPair();
            break;

        case ENROLL_PUBLIC_KEY:
            enrollPublicKey();
            break;

        case GET_CHALLENGE_CODE:
            getChallengeCode();
            break;

        case COMPUTE_SIGN_RESPONSE_CODE:
            computeAndSignResponseCode();
            break;

        case VERIFY_SIGNATURE:
            verifySignature();
            break;

        default:
            System.err.println("An unknown error has occured.");
        }
    }

    private void generateKeyPair() {
        try {
            keyPair = pkcsUtil.generateKeyPair();

            System.out.println("[Generate Key Pair with RSA Algorithm (2048-bit key size)]");
            System.out.println("keyPair.getPrivate().getAlgorithm()=>" + keyPair.getPrivate().getAlgorithm());
            System.out.println("keyPair.getPrivate().getFormat()=>" + keyPair.getPrivate().getFormat());
            System.out.println("keyPair.getPrivate().toString()=>" + keyPair.getPrivate().toString());

            System.out.println("keyPair.getPublic().getAlgorithm()=>" + keyPair.getPublic().getAlgorithm());
            System.out.println("keyPair.getPublic().getFormat()=>" + keyPair.getPublic().getFormat());
            System.out.println("keyPair.getPublic().toString()=>" + keyPair.getPublic().toString());

            String base64PrivateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            String base64PublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

            System.out.println(
                    "Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())=>" + base64PrivateKey);
            System.out.println(
                    "Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded())=>" + base64PublicKey);

            System.out.println(System.lineSeparator() + "The expected base64 public key to be sent from FE to BE=>"
                    + base64PublicKey);

            pkcsUtil.writePrivateKey(keyPair.getPrivate());
            pkcsUtil.writePublicKey(keyPair.getPublic());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException: " + e.getMessage());
        } catch (NoSuchProviderException e) {
            System.err.println("NoSuchProviderException: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            System.err.println("InvalidAlgorithmParameterException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    private void enrollPublicKey() {
        System.out.print(System.lineSeparator() + "Enter email address: ");
        String email = getScanner().nextLine();
        System.out.print(System.lineSeparator() + "Enter base64 public key: ");
        String base64PublicKey = getScanner().nextLine();

        publicKeyCache.enroll(email, base64PublicKey);
    }

    private void getChallengeCode() {
        System.out.print(System.lineSeparator() + "Enter email address: ");
        String email = getScanner().nextLine();

        try {
            String challengeCode = challengeCodeCache.generateChallengeCode(email);
            System.out.println(System.lineSeparator() + "Challenge code=>" + challengeCode);
            System.out.println("Base64.getEncoder().encodeToString(challengeCode.getBytes())=>"
                    + Base64.getEncoder().encodeToString(challengeCode.getBytes()));
            System.out.println("The expected base64 challenge code to be sent from BE to FE=>"
                    + Base64.getEncoder().encodeToString(challengeCode.getBytes()));
        } catch (Exception e) {
            System.err.println("getChallengeCode : " + e.getMessage());
        }
    }

    private void computeAndSignResponseCode() {
        System.out.print(System.lineSeparator() + "Enter base64 challenge code: ");
        String base64ChallengeCode = getScanner().nextLine();
        System.out.println("Enter base64 public key: ");
        String base64PublicKey = getScanner().nextLine();

        System.out.println("[COMPUTE]");
        System.out
                .println("Decoded base64 challenge code = new String(Base64.getDecoder().decode(base64ChallengeCode))=>"
                        + new String(Base64.getDecoder().decode(base64ChallengeCode)));

        System.out.println(
                "Response code bytes = reverse(Bytes(decodeBase64(base64PublicKey))) XOR Bytes(decodeBase64(base64ChallengeCode))");
        System.out.println(
                "Response code bytes = reverse(Base64.getDecoder().decode(base64PublicKey)) XOR Base64.getDecoder().decode(base64ChallengeCode)");
        byte[] responseCodeBytes = StringUtil.xor(StringUtil.reverse(Base64.getDecoder().decode(base64PublicKey)),
                Base64.getDecoder().decode(base64ChallengeCode));
        System.out.println("Response code bytes = " + responseCodeBytes);

        System.out.println(System.lineSeparator() + "[SIGN with SHA256withRSA Algorithm]");
        signResponseCode(responseCodeBytes);
    }

    private void signResponseCode(byte[] responseCodeBytes) {
        if (keyPair == null) {
            System.err.println("Please generate key pair first.");
            return;
        }

        PrivateKey privateKey = keyPair.getPrivate();

        try {
            System.out.println("Signature = Sign response code bytes with private key");
            byte[] signature = pkcsUtil.getSignature(responseCodeBytes, privateKey);
            System.out.println(
                    "Base64.getEncoder().encodeToString(signature)=>" + Base64.getEncoder().encodeToString(signature));
            System.out.println(System.lineSeparator() + "The expected base64 signature to be sent from FE to BE=>"
                    + Base64.getEncoder().encodeToString(signature));
        } catch (InvalidKeyException e) {
            System.err.println("InvalidKeyException: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("SignatureException: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException: " + e.getMessage());
        } catch (NoSuchProviderException e) {
            System.err.println("NoSuchProviderException: " + e.getMessage());
        }
    }

    private void verifySignature() {
        System.out.print(System.lineSeparator() + "Enter email address: ");
        String email = getScanner().nextLine();
        System.out.print(System.lineSeparator() + "Enter base64 signature: ");
        String base64Signature = getScanner().nextLine();

        try {
            // Get public key from publicKeyCache
            String base64PublicKey = publicKeyCache.retrieve(email);
            PublicKey publicKey = pkcsUtil.getPublicKey(base64PublicKey);

            // Generate response code from challenge code in challengeCodeCache
            byte[] responseCodeBytes = challengeCodeCache.generateResponseCode(email, base64PublicKey);

            // Verify signature
            byte[] signature = Base64.getDecoder().decode(base64Signature);
            boolean verified = pkcsUtil.verify(responseCodeBytes, signature, publicKey);

            if (verified) {
                System.out.println("Signature verified!!!");
            } else {
                System.err.println("Invalid signature!!!");
            }

        } catch (NoSuchAlgorithmException e) {
            System.err.println("NoSuchAlgorithmException: " + e.getMessage());
        } catch (NoSuchProviderException e) {
            System.err.println("NoSuchProviderException: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.err.println("InvalidKeySpecException: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println("InvalidKeyException: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("SignatureException: " + e.getMessage());
        }
    }

}
