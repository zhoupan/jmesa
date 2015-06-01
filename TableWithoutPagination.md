If you want to display a table without the pagination you need to set the max rows to the total rows, and then create a custom view to remove the toolbar. In addition, for this example, lets say that we also want to remove the statusbar? That makes sense as it will not offer any pagination information anyway.

First we need to set the max rows to the total amount of items that we want to display.

```
tableFacade.setMaxRows(items.size());
```


Then we need to implement a custom view. If you compare this view to the [HtmlView](http://code.google.com/p/jmesa/source/browse/trunk/jmesa/src/org/jmesa/view/html/HtmlView.java) in the API you can see that I just removed two lines of code. One to remove the toolbar and one for the status bar.

```
public class CustomView  extends AbstractHtmlView {
    public Object render() {
        HtmlSnippets snippets = getHtmlSnippets();

        HtmlBuilder html = new HtmlBuilder();

        html.append(snippets.themeStart());

        html.append(snippets.tableStart());

        html.append(snippets.theadStart());

        html.append(snippets.filter());

        html.append(snippets.header());

        html.append(snippets.theadEnd());

        html.append(snippets.tbodyStart());

        html.append(snippets.body());

        html.append(snippets.tbodyEnd());

        html.append(snippets.footer());

        html.append(snippets.tableEnd());

        html.append(snippets.themeEnd());

        html.append(snippets.initJavascriptLimit());

        return html.toString();
    }
}
```


Lastly, just plug your custom view into the TableFacade.

#### API ####

```
tableFacade.setView(new CustomView());
```

#### Tags ####

```
<jmesa:tableFacade view="com.mycompany.CustomView">
```