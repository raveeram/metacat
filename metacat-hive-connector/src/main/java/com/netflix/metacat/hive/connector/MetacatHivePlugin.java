/*
 * Copyright 2016 Netflix, Inc.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.metacat.hive.connector;

import com.facebook.presto.spi.ConnectorFactory;
import com.facebook.presto.spi.Plugin;
import com.facebook.presto.spi.type.Type;
import com.facebook.presto.spi.type.TypeManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.facebook.presto.type.FloatType.FLOAT;
import static com.facebook.presto.type.IntType.INT;
import static com.facebook.presto.type.TinyIntType.TINY_INT;
import static com.facebook.presto.type.SmallIntType.SMALL_INT;
import static com.facebook.presto.type.DecimalType.DECIMAL;
import static com.facebook.presto.type.CharType.CHAR;
import static com.facebook.presto.type.StringType.STRING;
import static com.google.common.base.Preconditions.checkNotNull;

public class MetacatHivePlugin implements Plugin
{
    private Map<String, String> optionalConfig = ImmutableMap.of();
    private TypeManager typeManager;

    @Inject
    public void setTypeManager(TypeManager typeManager)
    {
        this.typeManager = checkNotNull(typeManager, "typeManager is null");
    }

    @Override
    public void setOptionalConfig(Map<String, String> optionalConfig)
    {
        this.optionalConfig = ImmutableMap.copyOf(checkNotNull(optionalConfig, "optionalConfig is null"));
    }

    @Override
    public <T> List<T> getServices(Class<T> type)
    {
        if (type == ConnectorFactory.class) {
            return ImmutableList.of(type.cast(new MetacatHiveConnectorFactory("metacat-hive", optionalConfig, getClassLoader(), typeManager)));
        } else if (type == Type.class){
            return ImmutableList.of(type.cast(FLOAT), type.cast(INT), type.cast(TINY_INT), type.cast(SMALL_INT), type.cast(DECIMAL), type.cast(CHAR), type.cast(STRING));
        }
        return ImmutableList.of();
    }

    private static ClassLoader getClassLoader()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = MetacatHivePlugin.class.getClassLoader();
        }
        return classLoader;
    }
}
