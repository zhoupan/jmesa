/*
 * Copyright 2004 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmesa.core.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * Register and then retrive Match objects.
 * 
 * @since 2.0
 * @author Jeff Johnston
 */
public class FilterMatcherRegistryImpl implements FilterMatcherRegistry {
    private Map<MatcherKey, FilterMatcher> matchers = new HashMap<MatcherKey, FilterMatcher>();

    public void addFilterMatcher(MatcherKey key, FilterMatcher matcher) {
        matchers.put(key, matcher);
    }

    public FilterMatcher getFilterMatcher(MatcherKey key) {
        FilterMatcher matcher = getFilterMatchByProperty(key.getProperty());

        if (matcher != null) {
            return matcher;
        }

        matcher = getFilterMatchByType(key.getType());

        if (matcher != null) {
            return matcher;
        }

        matcher = getFilterMatchByObject(key.getType());

        if (matcher != null) {
            return matcher;
        }

        throw new IllegalArgumentException("There is no FilterMatch with the MatchKey [" + key.toString() + "]");
    }

    /**
     * If there is a FilterMatch that is registered by the specific property
     * name then use that, otherwise return null. The most specific search
     * because we matching based on the property.
     * 
     * @param property The column property for the current column item.
     * @return The FilterMatch object that will do the comparison.
     */
    private FilterMatcher getFilterMatchByProperty(String property) {
        if (property == null) {
            return null;
        }

        for (MatcherKey key : matchers.keySet()) {
            String prop = key.getProperty();
            if (prop != null && prop.equals(property)) {
                return matchers.get(key);
            }
        }

        return null;
    }

    /**
     * If there is a FilterMatch that is registered by the specific class type
     * then use that, otherwise return null. The intermediate search. To find a
     * match here means that a FilterMatch only has to match by the class type.
     * 
     * @param type The Class type for the current column item.
     * @return The FilterMatch object that will do the comparison.
     */
    private FilterMatcher getFilterMatchByType(Class type) {
        for (MatcherKey key : matchers.keySet()) {
            Class typ = key.getType();
            if (typ.equals(type)) {
                return matchers.get(key);
            }
        }

        return null;
    }

    /**
     * If there is a FilterMatch that is registered by the specific class
     * instance then use that, otherwise return null. Is the most general search
     * because will return the first match that is an instanceof the current
     * column.
     * 
     * @param type The Class type for the current column item.
     * @return The FilterMatch object that will do the comparison.
     */
    private FilterMatcher getFilterMatchByObject(Class type) {
        for (MatcherKey key : matchers.keySet()) {
            Class typ = key.getType();
            if (typ.isInstance(type)) {
                return matchers.get(key);
            }
        }

        return null;
    }
}
