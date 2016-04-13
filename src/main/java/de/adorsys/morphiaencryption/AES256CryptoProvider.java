/**
 * Copyright (C) 2016 Sandro Sonntag (sso@adorsys.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.adorsys.morphiaencryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES/CBC/PKCS5Padding crypto provider
 *
 * @author Sandro Sonntag
 */
public class AES256CryptoProvider implements CryptoProvider {

    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private final SecretKey key;

    public AES256CryptoProvider(String base64key) {
        key = createKey(base64key);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key, getIV());
            byte[] encrypted = cipher.doFinal(data);
            return encrypted;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException
                | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new CryptException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] encData) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key, getIV());
            byte[] decrypted = cipher.doFinal(encData);
            return decrypted;
        } catch (BadPaddingException | IllegalBlockSizeException
                | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new CryptException(e);
        }
    }

    private SecretKey createKey(String key) {
        byte[] inputKey = Base64.decodeBase64(key);
        return new SecretKeySpec(inputKey, "AES");
    }
    
    protected IvParameterSpec getIV() {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]);
        return ivParameterSpec;
    }
}