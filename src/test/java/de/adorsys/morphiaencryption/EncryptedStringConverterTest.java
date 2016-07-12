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

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.github.fakemongo.Fongo;
import com.mongodb.client.FindIterable;

public class EncryptedStringConverterTest {
    
    private Datastore datastore;
    private AES256CryptoProvider cryptoProvider;
    private Morphia morphia;
    private EncryptionConverter encConverter;

    @Before
    public void init() {
        Fongo fongo = new Fongo("test");
        morphia = new Morphia();
        morphia.map(TestEntity.class);
        cryptoProvider = new AES256CryptoProvider("Ie1imDDBddkhEqnUsntfsQ==");
        encConverter = new EncryptionConverter(cryptoProvider);
        morphia.getMapper().getConverters().addConverter(encConverter);

        datastore = morphia.createDatastore(fongo.getMongo(), "test");
        datastore.ensureIndexes();
    }

    @Test
    public void testStoreLoad() {
        TestEntity testEntity = createTestEntity();
        datastore.save(testEntity);
        System.out.println(testEntity.getId());
        TestEntity entity = datastore.get(TestEntity.class, testEntity.getId());
        
        assertEquals(testEntity.getBigDecimal(), entity.getBigDecimal());
        assertArrayEquals(testEntity.getBytes(), entity.getBytes());
        assertEquals(testEntity.getInteger(), entity.getInteger());
        assertEquals(testEntity.getLongval(), entity.getLongval());
        assertEquals(testEntity.getString(), entity.getString());
        assertNull(testEntity.getNullVal());
    }
    
    @Test
    public void testLoadUnencrypted() {
        //remove crypt converter to store unencrypted data for backward compatibility test
        morphia.getMapper().getConverters().removeConverter(encConverter);
        TestEntity testEntity = createTestEntity();
        
        //bd is not supported by default
        testEntity.setBigDecimal(null);
        testEntity.setUnencryptedBigDecimal(null);
        datastore.save(testEntity);
        
        morphia.getMapper().getConverters().addConverter(encConverter);
        System.out.println(testEntity.getId());
        TestEntity entity = datastore.get(TestEntity.class, testEntity.getId());
        
        assertEquals(testEntity.getBigDecimal(), entity.getBigDecimal());
        assertArrayEquals(testEntity.getBytes(), entity.getBytes());
        assertEquals(testEntity.getInteger(), entity.getInteger());
        assertEquals(testEntity.getLongval(), entity.getLongval());
        assertEquals(testEntity.getString(), entity.getString());
        assertNull(testEntity.getNullVal());
    }

    private TestEntity createTestEntity() {
        TestEntity testEntity = new TestEntity();
        testEntity.setBigDecimal(new BigDecimal("12.12"));
        testEntity.setBytes("test".getBytes());
        testEntity.setInteger(1);
        testEntity.setLongval(1l);
        testEntity.setString("test");
        testEntity.setUnencryptedString("foo");
        testEntity.setUnencryptedBigDecimal(new BigDecimal("12.11"));
        return testEntity;
    }
    
    @Test
    public void testFind() {
        TestEntity testEntity = createTestEntity();
        datastore.save(testEntity);
        
        FindIterable<Document> find = datastore.getMongo().getDatabase("test").getCollection("test").find();
        for (Document document : find) {
            System.out.println(document);
        }
        
        List<TestEntity> asList = datastore.createQuery(TestEntity.class).field("string").equal(new EncryptValue("test")).asList();
        assertEquals(1, asList.size());
    }
    
    @Test
    public void testFindUnencrypted() {
        TestEntity testEntity = createTestEntity();
        datastore.save(testEntity);
        
        FindIterable<Document> find = datastore.getMongo().getDatabase("test").getCollection("test").find();
        for (Document document : find) {
            System.out.println(document);
        }
        
        List<TestEntity> asList = datastore.createQuery(TestEntity.class).field("unencryptedString").equal("foo").asList();
        assertEquals(1, asList.size());
    }


}
