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
/**
 * 
 */
package de.adorsys.morphiaencryption;

import java.nio.charset.Charset;

/**
 * 
 * Use this class for seraching encrypted fields to tell morphia to search with an encrypted value. 
 * @author Sandro Sonntag
 *
 */
public class EncryptValue {

    private static final Charset ENCODING = Charset.forName("UTF-8");

    private final byte[] data;

    /**
     * 
     */
    public EncryptValue(byte[] data) {
        this.data = data;
    }

    public EncryptValue(Object value) {
        String stringValue = value.toString();
        data = stringValue.getBytes(ENCODING);
    }

    public byte[] getData() {
        return data;
    }

}
