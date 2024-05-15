package weteam.backend.domain.common;

import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
  public static String hashId(final Long id) {
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] encodedHash = digest.digest(Long.toString(id).getBytes(StandardCharsets.UTF_8));

      StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
      for (byte b : encodedHash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }

      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, e.getMessage());
    }
  }
}
