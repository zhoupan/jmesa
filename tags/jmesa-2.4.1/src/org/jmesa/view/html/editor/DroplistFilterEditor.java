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
package org.jmesa.view.html.editor;

import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jmesa.limit.Filter;
import org.jmesa.limit.Limit;
import org.jmesa.util.ItemUtils;
import org.jmesa.view.editor.AbstractFilterEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlColumn;

/**
 * Create a droplist for the filter.
 * 
 * @since 2.2
 * @author Jeff Johnston
 */
public class DroplistFilterEditor extends AbstractFilterEditor {

    public Object getValue() {
        HtmlBuilder html = new HtmlBuilder();

        Limit limit = getCoreContext().getLimit();
        HtmlColumn column = (HtmlColumn) getColumn();
        String property = column.getProperty();
        Filter filter = limit.getFilterSet().getFilter(property);

        String filterValue = "";
        if (filter != null) {
            filterValue = filter.getValue();
        }

        StringBuilder array = new StringBuilder();
        array.append("{");

        Collection<Option> options = getOptions();
        for (Iterator<Option> it = options.iterator(); it.hasNext();) {
            Option option = it.next();
            String value = escapeJavaScript(escapeHtml(option.getValue()));
            String label = escapeJavaScript(escapeHtml(option.getLabel()));
            array.append("'").append(value).append("':'").append(label).append("'");
            if (it.hasNext()) {
                array.append(",");
            }
        }

        array.append("}");

        html.div().styleClass("dynFilter");
        html.onclick("jQuery.jmesa.createDroplistDynFilter(this,'" + limit.getId() + "','" + column.getProperty() + "'," + array + ")");
        html.close();
        html.append(escapeHtml(filterValue));
        html.divEnd();

        return html.toString();
    }

    /**
     * @return The unique list of options for the droplist values.
     */
    protected List<Option> getOptions() {
        Set<String> values = new HashSet<String>();

        String property = getColumn().getProperty();

        for (Object item : getCoreContext().getAllItems()) {
            Object value = ItemUtils.getItemValue(item, property);

            if (value == null) {
                continue;
            }

            String valueStr = String.valueOf(value);

            if (valueStr.length() == 0) {
                continue;
            }

            values.add(valueStr);
        }
        
        List<Option> options = new ArrayList<Option>();

        for (String value : values) {
            Option option = new Option(value, value);
            options.add(option);
        }

        Collections.sort(options, null);

        return options;
    }

    /**
     * Represents an Html Select Option.
     */
    protected static class Option implements Comparable<Option> {
        private final String value;
        private final String label;

        public Option(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        public int compareTo(Option option) {
            return this.getLabel().compareToIgnoreCase(option.getLabel());
        }

        /**
         * Equality is based on the label. Or, in other words no two Option
         * Objects can have the same property.
         */
        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;

            if (!(o instanceof Option))
                return false;

            Option that = (Option) o;

            return that.getLabel().equals(this.getLabel());
        }

        @Override
        public int hashCode() {
            int result = 17;
            int property = this.getLabel() == null ? 0 : this.getLabel().hashCode();
            result = result * 37 + property;
            return result;
        }

        @Override
        public String toString() {
            ToStringBuilder builder = new ToStringBuilder(this);
            builder.append("value", getValue());
            builder.append("label", getLabel());
            return builder.toString();
        }
    }
}