#### How can I place some text in the max rows droplist? ####

You can place some text next to the max rows increments label (in the droplist). Just specify the text in the [Messages](Messages.md).

```
html.toolbar.text.maxRowsDroplist=items 
```

#### How can I create my own custom toolbar items? ####

You need to create a [custom toolbar](CustomToolbarTutorial.md) and plug it in.

#### How can I display inline page numbers? ####

A heavily requested feature is the ability to display page numbers,
similar to how the google search results works. This feature was
implemented with the help from the community.

![http://jmesa.org/wiki/images/toolbar-page-items.gif](http://jmesa.org/wiki/images/toolbar-page-items.gif)

All you will have to do to use it is enable the page items on the toolbar.

##### API #####

```
HtmlToolbar toolbar = new HtmlToolbar();
toolbar.enablePageNumbers(true);
tableFacade.setToolbar(toolbar); 
```

_Note: You need to create the table before setting the toolbar on the TableFacade. You can create the table by calling the TableFacade.columnProperties() method, or by using the [builder](HtmlTableBuilder.md) and calling the TableFacade.setTable() method._

##### Tags #####

If you are using the tags you need to extend the HtmlToolbar and enable the page numbers.

```
public class FullPaginationToolbar extends HtmlToolbar {
    @Override
    public String render() {
        enablePageNumbers(true);
        return super.render();
    }

}
```

Then give the fully qualified class name to your toolbar.

```
<jmesa:tableFacade
toolbar="com.mycompany.toolbar.FullPaginationToolbar" 
```

##### Toolbar Preferences #####

If you want to increase the page numbers that are displayed you can set the number in  your [preferences](Preferences.md) file.

```
html.toolbar.maxPageNumbers=5
```