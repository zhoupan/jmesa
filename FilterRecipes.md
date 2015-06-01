#### How come my table does not filter correctly? ####

By default JMesa will filter your items. The items being the Collection of Beans or Collection of Maps.

However, there is no association between the [CellEditor](Editors.md) and the filtering. That was a design decision for quite a few reasons. For one I wanted a clear detachment between the view and the items. The reason for that is using the API to filter is a feature and for large result sets you have to custom filter anyway. The larger issue is performance. To filter based on the cell editor the API would have to resolve each column and build a separate items object.

The JMesa filtering mechanism is very flexible and allows you to easily plug in your own [filter strategy](FilterMatcher.md).

#### How do I create a different kind of filter to display on the table? ####

When you interact with the filter on the table you are working with the FilterEditor interface behind the scenes. The default filter editor is the HtmlFilterEditor. There is also the newer DroplistFilterEditor.

To create your own filter editor you typically extend the AbstractFilterEditor. This gives you access to CoreContext, WebContext and Column. You could also just implement the FilterEditor directly if you do not need access to any of these.

For more information read the [Editors](Editors.md) page.

#### How do I plug in a different filtering strategy overall? ####

The filtering is done through the RowFilter interface. If you want to do something different you can implement that interface and plug in your own strategy. Here is an [example](CustomSimpleRowFilter.md) of how to extend the default SimpleRowFilter.


#### Why are my characters showing up as "?" on the screen? ####

It can be solved by setting the charset of the form, that surrounds the table, to UTF-8.

Or, try adding the following to the top of the JSP.
```
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
```