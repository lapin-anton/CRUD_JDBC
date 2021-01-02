import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.sql.*;

public class Encryptor {

    private Cipher cipher;
    private byte[] key;
    private static final String ALGORITHM = "AES";
    private SecretKeySpec keySpec;
    private Connection connection;

    public Encryptor(Connection connection) {
        this.connection = connection;
        this.key = getKey();
        this.keySpec = new SecretKeySpec(key, ALGORITHM);
        Security.addProvider(new BouncyCastleProvider());
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            ExceptionHandler.log(e);
        }
    }

    private byte[] getKey() {
        byte[] result = new byte[16];
        try {
            result = extractSecretKey();
            if (Arrays.areAllZeroes(result, 0, 15)) {
                KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
                SecureRandom secRandom = new SecureRandom();
                keyGen.init(secRandom);
                Key key = keyGen.generateKey();
                result = key.getEncoded();
                insertSecretKey(result);
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
        return result;
    }

    public byte[] code(String original) {
        byte[] encrypted = new byte[16];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
            byte[] in = original.getBytes(Charset.forName("UTF-8"));
            encrypted = cipher.doFinal(in);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
        return encrypted;
    }

    public String decode(byte[] encrypted) {
        String original = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new byte[16]));
            byte[] out = cipher.doFinal(encrypted);
            original = new String(out, Charset.forName("UTF-8"));
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
        return original;
    }

    private byte[] extractSecretKey() throws SQLException {
        byte[] result = new byte[16];
        String sql = "SELECT * FROM secret";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        int i = 0;
        while (rs.next()) {
            result[i] = (byte) rs.getInt("val");
            i++;
        }
        return result;
    }


    private void insertSecretKey(byte[] bytes) {
        String sql = "INSERT INTO secret (id, val) " +
                "VALUES (?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < bytes.length; i++) {
                statement.setInt(1, i + 1);
                statement.setInt(2, bytes[i]);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Ключ успешно сохранен в базе! " + i);
                } else  {
                    System.out.println("Ключ не сохранился! " + i);
                }
            }
        } catch (SQLException throwables) {
            ExceptionHandler.log(throwables);
        }
    }
}
