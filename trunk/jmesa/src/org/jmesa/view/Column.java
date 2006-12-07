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
package org.jmesa.view;

import java.util.List;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public interface Column {
	public String getProperty();
	
	public void setProperty(String property);
	
	public boolean isFilterable();
	
	public void setFilterable(boolean filterable);
	
	public boolean isSortable();
	
	public void setSortable(boolean sortable);
	
	public Renderer getHeaderRenderer();
	
	public void setHeaderRenderer(Renderer renderer);

	public Renderer getFilterRenderer();
	
	public void setFilterRenderer(Renderer renderer);
	
	public List<Renderer> getCalcRenderers();
	
	public void addCalcRenderer(Renderer renderer);
}
