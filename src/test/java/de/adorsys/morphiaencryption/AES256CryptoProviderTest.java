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

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test DemoCryptoProvider
 *
 * @author Michael Bickel
 */
public class AES256CryptoProviderTest {

    @Test
    public void testEncryptDecrypt() {
        String wrongKey = "4L3gj21IXIOnA7xzi0HF2g==";
        String encodeBase64Key = "Ie1imDDBddkhEqnUsntfsQ==";
        System.out.println(encodeBase64Key);
        AES256CryptoProvider aes256CryptoProvider = new AES256CryptoProvider(encodeBase64Key);
        
        String testText = "Dies ist ein im Zukunftsbild zu schützender Text!";
        byte[] encrypted = aes256CryptoProvider.encrypt(testText.getBytes());
        System.out.println(Base64.encodeBase64String(encrypted));
        
        // neuen Crypto-Provider zum Entschlüsseln erzeugen
        
        byte[] decrypted = aes256CryptoProvider.decrypt(encrypted);;
        System.out.println(new String(decrypted));
        Assert.assertEquals(testText, new String(decrypted));
        // Crypto-Provider mit abweichdendem Kennwort erzeugen
        try {
            new AES256CryptoProvider(wrongKey).decrypt(encrypted);
            Assert.fail("Fehler erwartet: Versuchte Entschlüsselung mit abweichendem Key");
        } catch (CryptException e) {
            // o.k. erwartet
        }
    }

}