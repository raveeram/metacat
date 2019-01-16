/*
 * Copyright 2016 Netflix, Inc.
 *
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
 *
 */
package com.netflix.metacat.thrift;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.netflix.metacat.common.server.api.v1.MetacatV1;
import com.netflix.metacat.common.server.api.v1.PartitionV1;
import com.netflix.metacat.common.server.properties.Config;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Objects;

/**
 * Thrift service factory.
 */
public class CatalogThriftServiceFactoryImpl implements CatalogThriftServiceFactory {
    private final LoadingCache<CacheKey, CatalogThriftService> cache;

    /**
     * Constructor.
     *
     * @param config               config
     * @param hiveConverters       hive converter
     * @param metacatV1            Metacat V1
     * @param partitionV1          Partition V1
     * @param registry             registry for micrometer
     */
    public CatalogThriftServiceFactoryImpl(
        final Config config,
        final HiveConverters hiveConverters,
        final MetacatV1 metacatV1,
        final PartitionV1 partitionV1,
        final MeterRegistry registry
    ) {
        this.cache = CacheBuilder.newBuilder()
            .build(new CacheLoader<CacheKey, CatalogThriftService>() {
                public CatalogThriftService load(final CacheKey key) {
                    return new CatalogThriftService(config, hiveConverters, metacatV1, partitionV1,
                        key.catalogName, key.portNumber, registry);
                }
            });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CatalogThriftService create(final String catalogName, final int portNumber) {
        return cache.getUnchecked(new CacheKey(catalogName, portNumber));
    }

    private static final class CacheKey {
        private final String catalogName;
        private final int portNumber;

        private CacheKey(final String catalogName, final int portNumber) {
            this.catalogName = catalogName;
            this.portNumber = portNumber;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CacheKey)) {
                return false;
            }
            final CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(portNumber, cacheKey.portNumber) && Objects.equals(catalogName, cacheKey.catalogName);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(catalogName, portNumber);
        }
    }
}
