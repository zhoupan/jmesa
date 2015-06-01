This tutorial will show how to customize a table's column display. The example will change the background color (light green) of the career column if the president had a career as a soldier.

![http://jmesa.org/wiki/images/customCellRenderer.jpg](http://jmesa.org/wiki/images/customCellRenderer.jpg)

We will start by creating our own custom HtmlCellRenderer. The easiest way to do this is by extending the default HtmlCellRenderer and overriding the render() method.

Note: in most cases you will want to just pass through to your cell editor to get the column value. However, if you need to get values from other columns you have two other choices. You could either cast the item down to the bean that it really is, or use the ItemUtils.getItemValue() method to get at the value you want.

```
private static class CustomHtmlCellRenderer extends HtmlCellRenderer {

    @Override
    public Object render(Object item, int rowcount) {
        String property = getColumn().getProperty();
        Object value = getCellEditor().getValue(item, property, rowcount);

        HtmlBuilder html = new HtmlBuilder();
        html.td(2);
        html.width(getColumn().getWidth());

        String valueStr = String.valueOf(value).toLowerCase();
        if (valueStr.contains("soldier")) {
            html.style("background-color:#c0dba7");
        } else {
            html.style(getStyle());
        }

        html.styleClass(getStyleClass());
        html.close();

        if (value != null) {
            html.append(value.toString());
        }

        html.tdEnd();

        return html.toString();
    }
}
```

Then we need to plug in our custom cell renderer.

```
HtmlColumn column = new HtmlColumn("career");
column.setCellRenderer(new CustomHtmlCellRenderer());
```


However, if you are using the tags then you just need to give the fully qualified path to your custom cell renderer.

```
<jmesa:htmlColumn property="career" cellRenderer="com.mycompany.view.CustomHtmlCellRenderer"/>
```