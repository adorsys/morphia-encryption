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

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

/**
 * Encryption Provider.
 *
 * @author Sandro Sonntag
 */
@SuppressWarnings("unused")
public class EncryptionConverter extends TypeConverter implements SimpleValueConverter {

    private static final Charset ENCODING = Charset.forName("UTF-8");
    private final AES256CryptoProvider cryptoProvider;

    public EncryptionConverter(AES256CryptoProvider cryptoProvider) {
        super(String.class, BigDecimal.class, byte[].class, Long.class, Integer.class, EncryptValue.class);
        this.cryptoProvider = cryptoProvider;
    }

    @Override
    public Object decode(Class<?> targetClass, Object dbObject, MappedField mappedField) {
        if (dbObject == null || !(dbObject instanceof byte[]) || (!mappedField.getField().isAnnotationPresent(StoreEncrypted.class))) {
        	if (mappedField.getType() == BigDecimal.class) {
        		return new BigDecimal((String)dbObject);
        	}
            return dbObject;
        }
        try {
            byte[] decrypted = cryptoProvider.decrypt((byte[]) dbObject);
            if (mappedField.getType() == String.class) {
                String string = new String(decrypted, ENCODING);
                return string;
            } else if (mappedField.getType() == Integer.class) {
                String string = new String(decrypted, ENCODING);
                return Integer.parseInt(string);
            } else if (mappedField.getType() == Long.class) {
                String string = new String(decrypted, ENCODING);
                return Long.parseLong(string);
            } else if (mappedField.getType() == BigDecimal.class) {
                String string = new String(decrypted, ENCODING);
                return new BigDecimal(string);
            } else {
                return decrypted;
            }
        } catch (CryptException e) {
            //return unencypted bytes if we have a problem with decryption
            return dbObject;
        }
        
    }
    

    @Override
    public Object encode(Object value, MappedField mappedField) {
        if (value == null) {
            return null;
        }

        final byte[] contentToEncrypt; 
        if (value instanceof EncryptValue) {
            contentToEncrypt = ((EncryptValue)value).getData();
        } else if (mappedField == null) {
        	return value;
        } else if (!mappedField.getField().isAnnotationPresent(StoreEncrypted.class)) {
        	if (mappedField.getType() == BigDecimal.class) {
        		return value != null ? value.toString() : null;
        	}
            return value;
        } else if (mappedField.getType() == byte[].class) {
            contentToEncrypt = (byte[]) value;
        } else {
            String stringValue = value.toString();
            contentToEncrypt = stringValue.getBytes(ENCODING);
        }
        byte[] encValue = cryptoProvider.encrypt(contentToEncrypt);
        return encValue;
    }
}