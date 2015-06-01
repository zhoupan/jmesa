### CellEditor ###
A CellEditor is used to display the column value. You can either use the pre-canned ones included in JMesa or create something custom. Typically most tables you create will have at least one custom editor. As you will see, creating your own CellEditor is very easy to do.

First lets go back and clarify how the CellEditor fits into the grand scheme of things and then I will offer up some examples. If you remember a Column is a component and has attributes that represents the features of the column. A CellRenderer is set on the Column and has attributes that represents how a column is to be displayed. The CellEditor is set on the CellRenderer and represents the actual column value. This keeps things very flexible and allows you to swap out one kind of functionality for another.

The CellEditor interface looks like the following:

```
public interface CellEditor {
    public Object getValue(Object item, String property, int rowcount);
}
```

The item is the current row item from the Collection of Beans or Collections of Maps. The property is the current column property that is really a reference to an item attribute. Lastly, the rowcount is the current row. The rowcount does not take in account pagination and with each page starts the count at one. However, if you need to take in account pagination you can find the current page on the Limit object, which can be found on the CoreContext.

Like most things the best way to understand this is to see an example. Here is the default CellEditor named the BasicCellEditor. It simply returns the current item property. You do not have to do anything to declare this editor as it is the default. I am just showing it as an example of what a CellEditor looks like.

```
public class BasicCellEditor extends AbstractCellEditor {
    public Object getValue(Object item, String property, int rowcount) {
        return ItemUtils.getItemValue(item, property);
    }
}
```

The CellEditors that ship with the JMesa API include the BasicCellEditor, DateCellEditor, DateTimeCellEditor and the NumberCellEditor. Again, you would not normally work directly with the BasicCellEditor, but be sure to use it as a reference.

#### CellEditor Example ####
If you want to create a custom editor then just implement the CellEditor interface. The easiest way to do that is to extend the AbstractCellEditor class.

  * API

```
// Using an anonymous class to implement a custom editor.
firstName.getCellRenderer().setCellEditor(new AbstractCellEditor() {
    public Object getValue(Object item, String property, int rowcount) {
        Object value = ItemUtils.getItemValue(item, property);
        HtmlBuilder html = new HtmlBuilder();
        html.a().href().quote().append("http://www.whitehouse.gov/history/presidents/").quote().close();
        html.append(value);
        html.aEnd();
        return html.toString();
    }
});
```

  * JSP Tag

```
<jmesa:column cellEditor="org.jmesaweb.controller.TagCellEditor">
```

#### DateCellEditor Example ####
If you have a date column chances are you will want to format it. To do that use the DateCellEditor and pass it a valid date pattern.

  * API

```
born.getCellRenderer().setCellEditor(new DateCellEditor("MM/yyyy"));
```

Use the pattern attribute with the cellEditor if you are using the tag library.

  * JSP Tag
Use the pattern attribute with the cellEditor if you are using the tag library.

```
<jmesa:htmlColumn property="born" pattern="MM/yyyy" cellEditor="org.jmesa.view.editor.DateCellEditor"/>
```

Also, if this column is filterable you will want to use the [DateFilterMatcher](FilterMatcher.md) to create a filter strategy that matches your column.

#### NumberCellEditor Example ####
If you have a numeric column chances are you will want to format it. To do that use the NumberCellEditor and pass it a valid date pattern.

  * API

```
salary.getCellRenderer().setCellEditor(new NumberCellEditor("###,##0.00"));
```

  * JSP Tag
Use the pattern attribute with the cellEditor if you are using the tag library.

```
<jmesa:htmlColumn property="salary" pattern="###,##0.00" cellEditor="org.jmesa.view.editor.NumberCellEditor"/>
```

Also, if this column is filterable you will want to use the [NumberFilterMatcher](FilterMatcher.md) to create a filter strategy that matches your column.

### HeaderEditor ###

For instance, as a rudimentary example, if you wanted to have a header that displayed a checkbox you would do this:

  * API

```
HtmlColumn htmlColumn = (HtmlColumn)lastName;
htmlColumn.getHeaderRenderer().setHeaderEditor(new HeaderEditor() {
    public Object getValue() {
        return "<input type=\"checkbox\">";
    }
});
```

  * JSP Tag

```
<jmesa:column headerEditor="org.jmesaweb.controller.TagHeaderEditor">
```

### FilterEditor ###

For instance, as a rudimentary example, if you wanted to have a custom filter that contained a radio list you would do this:

  * API

```
HtmlColumn careerColumn = (HtmlColumn)row.getColumn("career");;
careerColumn.getFilterRenderer().setFilterEditor(new FilterEditor() {
    public Object getValue() {
        Htmlbuilder html = new Htmlbuilder(); 
        html.input().type("radio").name("yes").value("yes").close().append("yes").inputEnd();
        html.input().type("radio").name("no").value("no").close().append("no").inputEnd();
        return html.toString();
    }
});
```

  * JSP Tag

```
<jmesa:column filterEditor="org.jmesaweb.controller.TagFilterEditor">
```

Keep in mind though that for this filter example you would still need to add the [Javascript](Javascript.md) to interact with the Javascript Limit. If this is something you are interested in you can look at the HtmlFilterEditor to see the default example.

#### DroplistFilterEditor ####

You can also define a droplist for the filters. The default filter implementation is to get a distinct list of the column and then use the natural sort order. If you want to pull a different list then override the DroplistFilterEditor.

![http://jmesa.org/wiki/images/filter-droplist.gif](http://jmesa.org/wiki/images/filter-droplist.gif)

  * API

```
column.getFilterRenderer().setFilterEditor(new DroplistFilterEditor());
```

  * JSP Tag

```
<jmesa:htmlColumn filterEditor="org.jmesa.view.html.editor.DroplistFilterEditor"/>;
```