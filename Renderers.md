A component is composed of renderers. The renderer's job is to render the component. The attributes of each renderer represent the features that are valid to renderer the specific component. For the purpose of this article I will also talk about the Html renderers and their attributes as they are used more often. For the other views see the javadocs to find out the various renderer specific attributes and how to use them.

#### TableRenderer ####
Each Table component has a TableRenderer. There are no common attributes for a TableRenderer between different views. However, an HtmlTableRenderer has the following attributes: style, styleClass, border, cellpadding, cellspacing, and width.

The style is the style attribute on the html table element. For instance this would change the font color to blue in the table.

  * API

```
table.getTableRenderer().setStyle("color:blue");
```

  * JSP Tag

```
<jmesa:table style="color:blue">
```

The styleClass is the class attribute on the html table element. For instance this would change the css class to jmesaTable.

  * API

```
table.getTableRenderer().setStyleClass("jmesaTable");
```

  * JSP Tag

```
<jmesa:table styleClass="jmesaTable">
```

The border is the border attribute on the html table element. For instance this would change the border to 1px.

  * API

```
table.getTableRenderer().setBorder("1px");
```

  * JSP Tag

```
<jmesa:table border="1px">
```

The cellpadding is the cellpadding attribute on the html table element. For instance this would change the cellpadding to 1px.

  * API

```
table.getTableRenderer().setCellpadding("1px");
```

  * JSP Tag

```
<jmesa:table cellpadding="1px"">
```

The cellspacing is the cellspacing attribute on the html table element. For instance this would change the cellspacing to 1px.

  * API

```
table.getTableRenderer().setCellspacing("1px");
```

  * JSP Tag

```
<jmesa:table cellspacing="1px"">
```

The width is the width attribute on the html table element. For instance this would change the width to 600px.

  * API

```
table.getTableRenderer().setWidth("600px");
```

  * JSP Tag

```
<jmesa:table width="600px"">
```

#### RowRenderer ####
Each Row component has a RowRenderer. There are no common attributes for a RowRenderer between different views. However, an HtmlRowRenderer has the following attributes: style, styleClass, highlightStyle, highlightClass, evenClass, and oddClass.

The style is the style attribute on the html tr element. For instance this would change the height of the row to 20px.

  * API

```
row.getRowRenderer().setStyle("height:20px");
```

  * JSP Tag

```
<jmesa:row style="height:20px"">
```

The styleClass is the class attribute on the html tr element. For instance this would change the css class to jmesaRow.

  * API

```
row.getRowRenderer().setStyleClass("jmesaRow");
```

  * JSP Tag

```
<jmesa:row styleClass="jmesaRow">
```

The highlightStyle is the style attribute on the html tr element. For instance this would change the height of the row to 20px.

  * API

```
row.getRowRenderer().setHighlightStyle("height:20px");
```

  * JSP Tag

```
<jmesa:row highlightStyle="height:20px">
```

The highlightClass is the class attribute on the html tr element. For instance this would change the css class to jmesaRowHighlighter.

  * API

```
row.getRowRenderer().setHighlightClass("jmesaRowHighlighter");
```

  * JSP Tag

```
<jmesa:row highlightClass="jmesaRowHighlighter">
```

The evenClass is the class attribute on the html tr element. For instance this would change the css class to jmesaRowEven.

  * API

```
row.getRowRenderer().setEvenClass("jmesaRowEven");
```

  * JSP Tag

```
<jmesa:row evenClass="jmesaRowEven">
```

The oddClass is the class attribute on the html tr element. For instance this would change the css class to jmesaRowOdd.

  * API

```
row.getRowRenderer().setOddClass("jmesaRowOdd");
```

  * JSP Tag

```
<jmesa:row oddClass="jmesaRowOdd">
```

#### CellRenderer ####
Each Column component has a CellRenderer. The common attribute for all CellRenderers, regardless of the view, is the CellEditor. A CellEditor is responsible for returning the value that the renderer will render. For more information see the [Editors](Editors.md) article.

An HtmlCellRenderer has the following attributes: style, and styleClass.

The style is the style attribute on the html td element. For instance this would change the font color to blue in the column.

  * API

```
column.getCellRenderer().setStyle("color:blue");
```

  * JSP Tag

```
<jmesa:column style="color:blue">
```

The styleClass is the class attribute on the html td element. For instance this would change the css class to jmesaCell.

  * API

```
column.getCellRenderer().setStyleClass("jmesaCell");
```

  * JSP Tag

```
<jmesa:column styleClass="jmesaCell">
```

#### HeaderRenderer ####
Each Column component has a HeaderRenderer. The common attribute for all HeaderRenderers, regardless of the view, is the HeaderEditor. A HeaderEditor is responsible for returning the value that the renderer will render. For more information see the [Editors](Editors.md) article.

An HtmlHeaderRenderer has the following attributes: style, styleClass, and defaultSortOrderable.

The style is the style attribute on the html td element in the header row. For instance this would change the height of the column to 20px.

  * API

```
column.geHeaderRenderer().setStyle("height:20px");
```

  * JSP Tag

```
<jmesa:column headerStyle="height:20px">
```

The styleClass is the class attribute on the html td element in the header row. For instance this would change the css class to jmesaHeader.

  * API

```
column.getHeaderRenderer().setStyleClass("jmesaHeader");
```

  * JSP Tag

```
<jmesa:column headerClass="jmesaHeader">
```

#### FilterRenderer ####
Each Column component has a FilterRenderer. The common attribute for all FilterRenderers, regardless of the view, is the FilterEditor. A FilterEditor is responsible for returning the value that the renderer will render. For more information see the [Editors](Editors.md) article.

An HtmlFilterRenderer has the following attributes: style, and styleClass.

The style is the style attribute on the html td element. For instance this would change the height of the column to 20px.

  * API

```
column.getFilterRenderer().setStyle("height:20px");
```

  * JSP Tag

```
<jmesa:column filterStyle="height:20px">
```

The styleClass is the class attribute on the html td element. For instance this would change the css class to jmesaFilter.

  * API

```
column.getFilterRenderer().setStyleClass("jmesaFilter");
```

  * JSP Tag

```
<jmesa:column filterClass="jmesaFilter">
```