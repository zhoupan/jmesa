I added a series of support interfaces to automatically inject certain key classes into your custom code. The interfaces just define getter and setter methods, but using these interfaces makes things very clear and safe. The idea is if you implement a certain interface the API will check for the inclusion of that interface and inject the class for you. This support feature actually started as a tag feature because there was no other way for developers to get at the classes they needed cleanly. It turned out to be a nice addition that bubbled up to the API.

For example the DateFilterMatcher implements the WebContextSupport interface. Now when you add a FilterMatcher to the facade it automatically inserts the WebContext for you.

So now you can do this:
```
tableFacade.addFilterMatcher(new MatcherKey(Date.class, "born"), new DateFilterMatcher("MM/yyyy"));
```

Versus this:
```
DateFilterMatcher matcher = new DateFilterMatcher("MM/yyyy")
matcher.setWebContext(tableFacade.getWebContext());
tableFacade.addFilterMatcher(new MatcherKey(Date.class, "born"), matcher);
```

The reason is DateFilterMatcher implements WebContextSupport so the API knows to inject that class if it is not already set. On one hand the feature is a little magical, but on the flip side as a person wanting to implement a custom date filter matcher you really do not care that the class needs a WebContext. You just want to define your custom pattern!

The support classes include WebContextSupport, CoreContextSupport, ToolbarSupport, TableSupport, ColumnSupport and PatternSupport. Next I will outline when you can count on being able to use the feature. If you have any doubts you can also check out the javadocs. If any of the core classes implement the support interfaces you can be sure the feature is supported. Note: the javadocs are included in with the example war file in the downloads area.

Note: There is also a class called AbstractContextSupport that implements the ContextSupport interface. The ContextSupport interface in turn extends from the WebContextSupport and CoreContextSupport interfaces. This abstract class is an easy way to get the CoreContext and WebContext.

### WebContextSupport ###

The interface is defined as:

```
public interface WebContextSupport {
    public WebContext getWebContext();

    public void setWebContext(WebContext webContext);
}
```

The following places will inject the WebContext for you if you define the WebContextSupport interface in your custom code:

  * API

```
tableFacade.addFilterMatcher();

tableFacade.setMessages(Messages messages); (since 2.2.5)

tableFacade.setPreferences(Preferences preferences); (since 2.2.5)

tableFacade.setRowFilter(RowFilter rowFilter); (since 2.2.5)

tableFacade.setColumnSort(ColumnSort columnSort); (since 2.2.5)

tableFacade.setCoreContext(CoreContext coreContext); (since 2.2.5)

tableFacade.setToolbar(Toolbar toolbar); (since 2.2.5)

tableFacade.setView(View view); (since 2.2.5)

row.setOnclick(RowEvent event);

row.setOnmouseover(RowEvent event);

row.setOnmouseout(RowEvent event);

column.getCellRenderer().setCellEditor(CellEditor editor);

column.getFilterRenderer().setFilterEditor(FilterEditor filterEditor)

column.getHeaderRenderer().setHeaderEditor(HeaderEditor headerEditor)
```

  * JSP Tag

```
<jmesa:tableFacade filterMatcherMap="">

<jmesa:tableFacade messages=""> (since 2.2.5)

<jmesa:tableFacade preferences=""> (since 2.2.5)

<jmesa:tableFacade rowFilter=""> (since 2.2.5)

<jmesa:tableFacade columnSort=""> (since 2.2.5)

<jmesa:tableFacade toolbar=""> (since 2.2.5)

<jmesa:tableFacade view="">

<jmesa:row onclick="">

<jmesa:row onmouseover="">

<jmesa:row onmouseout="">

<jmesa:column cellEditor="">

<jmesa:column filterEditor="">

<jmesa:column headerEditor="">
```

### CoreContextSupport ###

The interface is defined as:

```
public interface CoreContextSupport {
    public CoreContext getCoreContext();

    public void setCoreContext(CoreContext coreContext);
}
```

The following places will inject the CoreContext for you if you define the CoreContextSupport interface in your custom code:

  * API

```
tableFacade.setView(View view); (since 2.2.5)

tableFacade.setToolbar(Toolbar toolbar); (since 2.2.5)

row.setOnclick(RowEvent event);

row.setOnmouseover(RowEvent event);

row.setOnmouseout(RowEvent event);

column.getCellRenderer().setCellEditor(CellEditor editor);

column.getFilterRenderer().setFilterEditor(FilterEditor filterEditor)

column.getHeaderRenderer().setHeaderEditor(HeaderEditor headerEditor)
```

  * JSP Tag

```
<jmesa:tableFacade view="">

<jmesa:tableFacade toolbar=""> (since 2.2.5)

<jmesa:row onclick="">

<jmesa:row onmouseover="">

<jmesa:row onmouseout="">

<jmesa:column cellEditor="">

<jmesa:column filterEditor="">

<jmesa:column headerEditor="">
```

### PatternSupport ###
The interface is defined as:
```
public interface PatternSupport {
    public String getPattern();

    public void setPattern(String pattern);
}
```

The following places will inject the pattern attribute for you if you define the PatternSupport interface in your custom code:

Tag:
```
<jmesa:tableFacade pattern="" cellEditor=""> // if have the pattern defined
```

### ToolbarSupport ###

The interface is defined as:

```
public interface ToolbarSupport {
    public Toolbar getToolbar();

    public void setToolbar(Toolbar toolbar);
}
```

The following places will inject the Toolbar for you if you define the ToolbarSupport interface in your custom code:

  * API

```
tableFacade.setView(View view); (since 2.2.5)
```

  * JSP Tag

```
<jmesa:tableFacade view="">
```

### TableSupport ###
The interface is defined as:
```
public interface TableSupport {
    public Table getTable();

    public void setTable(Table table);
}
```

The following places will inject the Table for you if you define the TableSupport interface in your custom code:

  * API

```
tableFacade.setToolbar(Toolbar toolbar); (since 2.2.5)
```

  * JSP Tag

```
<jmesa:tableFacade view="">

<jmesa:tableFacade toolbar=""> (since 2.2.5)
```

### ColumnSupport ###
The interface is defined as:
```
public interface ColumnSupport {
    public Column getColumn();

    public void setColumn(Column column);
}
```

The following places will inject the Column for you if you define the ColumnSupport interface in your custom code:

  * API

```
column.getCellRenderer().setCellEditor(CellEditor editor);

column.getFilterRenderer().setFilterEditor(FilterEditor filterEditor)

column.getHeaderRenderer().setHeaderEditor(HeaderEditor headerEditor)
```

  * JSP Tag

```
<jmesa:column cellEditor="">

<jmesa:column filterEditor="">

<jmesa:column headerEditor="">
```

### FilterMatcherRegistrySupport ###
The interface is defined as:
```
public interface FilterMatcherRegistrySupport {
    public FilterMatcherRegistry getFilterMatcherRegistry();

    public void setFilterMatcherRegistry(FilterMatcherRegistry registry);
}
```

The following places will inject the FilterMatcherRegistry for you if you define the FilterMatcherRegistrySupport interface in your custom code:

  * API

```
tableFacade.setRowFilter(RowFilter rowFilter);
```

  * JSP Tag

```
<jmesa:tableFacade rowFilter="">
```