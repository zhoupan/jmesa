To do totaling with JMesa requires making a custom view. As you will see this is very easy to do.

The easiest way to approach this is to extend the AbstractHtmlView and override the render() method. Then copy the existing render method from the [HtmlView](http://code.google.com/p/jmesa/source/browse/trunk/jmesa/src/org/jmesa/view/html/HtmlView.java). The code that really contains all the parts of the table is in the HtmlSnippets implementation so it is expected that you will just use the parts of the snippets that you want, and then customize the rest in your view.

```
public Object render() {
    HtmlSnippets snippets = getHtmlSnippets();

    HtmlBuilder html = new HtmlBuilder();

    html.append(snippets.themeStart());

    html.append(snippets.tableStart());

    html.append(snippets.theadStart());

    html.append(snippets.toolbar());

    html.append(snippets.filter());

    html.append(snippets.header());

    html.append(snippets.theadEnd());

    html.append(snippets.tbodyStart());

    html.append(snippets.body());

    html.append(snippets.tbodyEnd());

    html.append(snippets.footer());

    html.append(totals());

    html.append(snippets.statusBar());

    html.append(snippets.tableEnd());

    html.append(snippets.themeEnd());

    html.append(snippets.initJavascriptLimit());

    return html.toString();
}
```

In the above code the method to pay attention to is the totals() method call. What that does is include the row with the totals. There really isn't anything to doing this. Just total up whatever you need, and then create the row. Granted this is a pretty arbitrary example because the only thing in the presidents that can be totaled is the database primary key, but you will still get the idea.

The thing you might not know is that the CoreContext implements the [Items](http://docs.jmesa.org/org/jmesa/core/Items.html) interface. That means you have access to all the items in every way you might need. This include items for the current page, the items after filtering, and all the items (before filtering).

```
protected String totals() {
    int totals = 0;

    Collection items = getCoreContext().getPageItems();
    for (Object obj : items) {
        President president = (President) obj;
        Integer id = president.getId();
        totals += id;
    }

    HtmlBuilder html = new HtmlBuilder();
    html.tr(1).close();
    html.td(2).colspan("5").style("text-align:right;padding-right:5px").close().append("Totals:").tdEnd();
    html.td(2).close().append(totals).tdEnd();
    html.trEnd(1);
    return html.toString();
}
```

The last thing we need to do is plug the custom view into the TableModel.

#### API ####

```
tableModel.setView(new CustomView());
```

#### JSP Tag ####

```
<jmesa:tableModel view="com.mycompany.CustomView">
```