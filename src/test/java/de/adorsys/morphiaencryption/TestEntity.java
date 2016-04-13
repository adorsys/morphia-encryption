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

import java.math.BigDecimal;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

@Entity(value = "test", noClassnameStored = true)
public class TestEntity {

    @Id
    @Indexed(unique = true)
    private ObjectId id;

    @StoreEncrypted
    @Property
    private String string;
    
    @StoreEncrypted
    @Property
    private String nullVal;

    @StoreEncrypted
    @Property
    private Integer integer;
    
    @StoreEncrypted
    @Property
    private BigDecimal bigDecimal;
    
    @StoreEncrypted
    @Property
    private Long longval;
    
    @StoreEncrypted
    @Property
    private byte[] bytes;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Long getLongval() {
        return longval;
    }

    public void setLongval(Long longval) {
        this.longval = longval;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    

    public String getNullVal() {
        return nullVal;
    }

    public void setNullVal(String nullVal) {
        this.nullVal = nullVal;
    }

}