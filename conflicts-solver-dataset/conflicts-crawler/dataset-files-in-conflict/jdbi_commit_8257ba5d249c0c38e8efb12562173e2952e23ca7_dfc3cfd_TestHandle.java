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
package org.jdbi.v3;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.tweak.HandleCallback;
import org.jdbi.v3.tweak.ResultSetMapper;
import org.junit.Rule;
import org.junit.Test;

public class TestHandle
{
    @Rule
    public MemoryDatabase db = new MemoryDatabase();

    @Test
    public void testInTransaction() throws Exception
    {
        Handle h = db.openHandle();

        String value = h.inTransaction(new TransactionCallback<String>()
        {
            @Override
            public String inTransaction(Handle handle, TransactionStatus status) throws Exception
            {
                handle.insert("insert into something (id, name) values (1, 'Brian')");
                return handle.createQuery("select name from something where id = 1").map(Something.class).first().getName();
            }
        });
        assertEquals("Brian", value);
    }

    @Test
    public void testSillyNumberOfCallbacks() throws Exception
    {
        Handle h = db.openHandle();
        h.insert("insert into something (id, name) values (1, 'Keith')");
        h.close();


        String value = db.getDbi().withHandle(new HandleCallback<String>()
        {
            @Override
            public String withHandle(Handle handle) throws Exception
            {
                return handle.inTransaction(new TransactionCallback<String>()
                {
                    @Override
                    public String inTransaction(Handle handle, TransactionStatus status) throws Exception
                    {
                        return handle.createQuery("select name from something where id = 1").map(new ResultSetMapper<String>()
                        {
                            @Override
                            public String map(int index, ResultSet r, StatementContext ctx) throws SQLException
                            {
                                return r.getString(1);
                            }
                        }).first();
                    }
                });
            }
        });

        assertEquals("Keith", value);
    }
}
