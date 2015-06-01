A [view](View.md) is composed of components, namely the table, row and column. The attributes of each component represent the features that are valid across multiple [renderers](Renderers.md). If you remember, each view defines their own components. For the purpose of this article I will also talk about the Html components and their attributes as they are used more often. For the other views see the javadocs to find out the various component specific attributes and how to use them.

### Table ###
The common attributes of all tables, regardless of the view, are caption, row, and tableRenderer.

#### caption and captionKey ####

The caption is used to give the table a name. Almost all views will display the caption above the table.

  * API

```
table.setCaption("Presidents");
```

  * JSP Tag

```
<jmesa:table caption="Presidents">
```

If you are using the [Messages](Messages.md) then use the alternate caption method.

  * API

```
table.setCaptionKey("table.presidents");
```

  * JSP Tag

```
<jmesa:table captionKey="table.presidents">
```

#### row ####

The row defines the Row used for the table. Each table has one Row associated with it that represents all the rows in a table.

  * API

```
table.setRow(row);
```

  * JSP Tag

```
<jmesa:row>
```

Can set global filterable and sortable attributes on the row. Also have the ability to override the row settings on a column by column basis.

  * API

```
row.setFilterable(true);
```

```
row.setSortable(true);
```

  * JSP Tag

```
<jmesa:row filterable="true"  sortable="true">
```

#### tableRenderer ####

The tableRenderer defines the TableRenderer used for the table. See the TableRenderer in the [Renderers](Renderers.md) article for more information.

  * API

```
table.setTableRenderer(tableRenderer);
```

  * JSP Tag

```
<jmesa:table tableRenderer="com.mycompany.renderer.MyTableRenderer">
```

### HtmlTable ###

The HtmlTable also has a theme attribute used to give a different display for different tables. Technically this means that the div that surrounds the table will have its class attribute modified. This technique makes it very easy to define many different displays just by declaring different CSS styles. All you have to do is prepend each selector in the CSS with the theme. For instance the default theme is called jmesa.

#### theme ####

  * API

```
table.setTheme("jmesa"); // the default
```

  * JSP Tag

```
<jmesa:table theme="jmesa">
```

If you open up the jmesa.css file you will see .jmesa prepended before each selector.

```
.jmesa caption {
    caption-side: top;
    color: #444444;
    font-weight: bold;
    font-size: 1em;
    text-align: left;
    padding-left: 5px;
}

```

### Row ###
The common attributes of all rows, regardless of the view, are columns and rowRenderer.

#### columns ####

The columns defines the Columns used for the table.

  * API

```
row.addColumn(column);
```

  * JSP Tag

```
<jmesa:column>
```

#### rowRenderer ####

The rowRenderer defines the RowRenderer used for the row. See the RowRenderer in the [Renderers](Renderers.md) article for more information.

  * API

```
row.setRowRenderer(rowRenderer);
```

  * JSP Tag

```
<jmesa:row rowRenderer="com.mycompany.renderer.MyRowRenderer">
```

### HtmlRow ###

An HtmlRow has the following attributes: highlighter, onclick, onmouseover, and onmouseout.

#### highlighter ####

The highlighter is used to give the visual appearance of a row being highlighted as your mouse flows over the row. The default value is true, meaning the row will be highlighted.

  * API

```
row.setHighlighter(true); // the default
```

  * JSP Tag

```
<jmesa:row highlighter="true">
```

#### onclick ####
The onclick method on the row looks like this:

  * API

```
public String onclick(RowEvent event);
```

The RowEvent is a callback interface:

  * API

```
public interface RowEvent {
   public String execute(Object item, int rowcount);

}
```

This allows custom onclick callback events for each row:

  * API

```
HtmlRow row = table.getRow();
row.setOnclick(new RowEvent() {
      public String execute(Object Item, int rowcount) {
           // do something custom for each click on the row
          //  which contains data from the item.
      }

};)
```

  * JSP Tag

```
<jmesa:htmlRow onclick="org.jmesaweb.controller.TagRowEvent">
```

```
public class TagRowEvent implements RowEvent {
    public String execute(Object item, int rowcount) {
        Object bean = ItemUtils.getItemValue(item, "bean");
        Object id = ItemUtils.getItemValue(bean, "id");
        return "document.location='http://www.whitehouse.gov/history/presidents?id=" + id + "'";
    }
}
```

_The onmouseover and onmouseout work the same way._

Note: When working with the row events you do not have to inject the WebContext or CoreContext specifically into your classes if you define the proper support interfaces. See the [support interfaces](SupportInterfaces.md) page for more details on that.

### Column ###
The common attributes of all columns, regardless of the view, are property, title, cellRenderer, and headerRenderer.

#### property ####

The property corresponds to the item attribute, where an item is one object in the Collection of Items or Collection of Maps. The property is optional, so if the column does not map to an item attribute then it should be null.

  * API

```
column.setProperty("firstName");
```

  * JSP Tag

```
<jmesa:column property="firstName">
```

#### title and titleKey ####

The title is used to give the header a descriptive name. If you do not define a title then the column will convert the camelcase property name to a real word. If you do not want anything to show up in the header then just add a single whitespace for the title.

  * API

```
column.setTitle("First Name"); // Note: if left blank would still show as "First Name"
```

  * JSP Tag

```
<jmesa:column title="First Name">
```

If you are using the [Messages](Messages.md) then use the alternate title method.

  * API

```
column.setTitleKey("column.firstName")
```

  * JSP Tag

```
<jmesa:column titleKey="column.firstName">
```

#### cellRenderer ####

The cellRenderer defines the CellRenderer used for the column. See the CellRenderer in the [Renderers](Renderers.md) article for more information.

  * API

```
column.setCellRenderer(cellRenderer);
```

  * JSP Tag

```
<jmesa:column cellRenderer="com.mycompany.renderer.MyCellRenderer">
```

#### headerRenderer ####

The headerRenderer defines the HeaderRenderer used for the column. See the HeaderRenderer in the [Renderers](Renderers.md) article for more information.

  * API

```
column.setHeaderRenderer(headerRenderer);
```

  * JSP Tag

```
<jmesa:column headerRenderer="com.mycompany.renderer.MyHeaderRenderer">
```

### HtmlColumn ###

The HtmlColumn also has the following attributes: filterable, sortable, filterRenderer, width, and sortOrder.

#### filterable / sortable ####

JMesa has filtering and sorting built right in. The only thing you need to decide is whether or not to use it. The attributes you will use are filterable and sortable. Both are booleans and the default value is true. If you choose not to use the sorting or filtering, then set the attributes to false.

  * API

```
column.setFilterable(true); // the default
column.setSortable(true); // the default
```

  * JSP Tag

```
<jmesa:column filterable="true" sortable="true">
```

#### filterRenderer ####

The filterRenderer defines the FilterRenderer used for the column. See the FilterRenderer in the [Renderers](Renderers.md) article for more information.

  * API

```
column.setFilterRenderer(filterRenderer);
```

  * JSP Tag

```
<jmesa:column filterRenderer="com.mycompany.renderer.MyFilterRenderer">
```

#### width ####

The width defines the width used for the column.

  * API

```
column.setWidth("10px");
```

  * JSP Tag

```
<jmesa:column width="10px">
```

#### sortOrder ####

The sort order restricts the sorting to only the types defined. Typically you would use this to exclude the 'none' Order so that the user can only sort ascending and decending once invoked.

Note: Initially this does not change the look of the column, or effect the sorting, when the table is first displayed. For instance, if you only want to sort asc and then desc then when the table is initially displayed you need to make sure you set the Limit to be ordered. The reason is, by design, the limit does not look at the view for any information. The syntax to set the limit would be: limit.getSortSet().addSort();. If you do not do this then the effect will be that the once the column is sorted then it will just flip between asc and desc, which is still a really nice effect and is what I would mostly do.

  * API

```
HtmlColumn htmlColumn = (HtmlColumn)firstName;
htmlColumn.setSortOrder(Order.ASC, Order.DESC);
```

  * JSP Tag

```
<jmesa:htmlColumn sortOrder="asc,desc">
```

The legal Column sortOrder values are 'asc', 'desc', and 'none'.