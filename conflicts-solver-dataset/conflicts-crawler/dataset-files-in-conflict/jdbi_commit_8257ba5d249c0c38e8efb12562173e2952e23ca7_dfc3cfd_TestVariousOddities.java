/*
 * Copyright (C) 2004 - 2013 Brian McCallister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.sqlobject;

import java.util.UUID;

import org.jdbi.v3.DBI;
import org.jdbi.v3.Handle;
import org.jdbi.v3.Something;
import org.jdbi.v3.sqlobject.customizers.Mapper;
import org.jdbi.v3.sqlobject.mixins.CloseMe;

import junit.framework.TestCase;

public class TestVariousOddities extends TestCase
{
    private DBI dbi;
    private Handle handle;


    @Override
    public void setUp() throws Exception
    {
        dbi = new DBI("jdbc:h2:mem:" + UUID.randomUUID());
        handle = dbi.open();

        handle.execute("create table something (id int primary key, name varchar(100))");

    }

    @Override
    public void tearDown() throws Exception
    {
        handle.execute("drop table something");
        handle.close();
    }

    public void testAttach() throws Exception
    {
        Spiffy s = SqlObjectBuilder.attach(handle, Spiffy.class);
        s.insert(new Something(14, "Tom"));

        Something tom = s.byId(14);
        assertEquals("Tom", tom.getName());
    }

    public void testRegisteredMappersWork() throws Exception
    {

    }

    public void testEquals()
    {
        Spiffy s1 = SqlObjectBuilder.attach(handle, Spiffy.class);
        Spiffy s2 = SqlObjectBuilder.attach(handle, Spiffy.class);
        assertEquals(s1, s1);
        assertNotSame(s1, s2);
        assertFalse(s1.equals(s2));
    }

    public void testToString()
    {
        Spiffy s1 = SqlObjectBuilder.attach(handle, Spiffy.class);
        Spiffy s2 = SqlObjectBuilder.attach(handle, Spiffy.class);
        assertNotNull(s1.toString());
        assertNotNull(s2.toString());
        assertTrue(s1.toString() != s2.toString());
    }

    public void testHashCode()
    {
        Spiffy s1 = SqlObjectBuilder.attach(handle, Spiffy.class);
        Spiffy s2 = SqlObjectBuilder.attach(handle, Spiffy.class);
        assertFalse(0 == s1.hashCode());
        assertFalse(0 == s2.hashCode());
        assertTrue(s1.hashCode() != s2.hashCode());
    }

    public void testNullQueryReturn()
    {
        try {
            SqlObjectBuilder.attach(handle, SpiffyBoom.class);
        } catch (IllegalStateException e) {
            assertEquals("Method org.jdbi.v3.sqlobject.TestVariousOddities$SpiffyBoom#returnNothing " +
                    "is annotated as if it should return a value, but the method is void.", e.getMessage());
            return;
        }
        fail();
    }

    public static interface Spiffy extends CloseMe
    {

        @SqlQuery("select id, name from something where id = :id")
        @Mapper(SomethingMapper.class)
        public Something byId(@Bind("id") long id);

        @SqlUpdate("insert into something (id, name) values (:it.id, :it.name)")
        public void insert(@Bind(value = "it", binder = SomethingBinderAgainstBind.class) Something it);

    }

    public static interface SpiffyBoom extends CloseMe
    {
        @SqlQuery("SELECT 1")
        void returnNothing();
    }
}
