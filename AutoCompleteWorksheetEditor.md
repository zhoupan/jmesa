**Right now this example uses the now deprecated [bassistance library](http://bassistance.de/jquery-plugins/jquery-plugin-autocomplete/). I did my integration quite a while ago and when I went to go link to the site I see that the project is a part of [JQuery UI](http://jqueryui.com/demos/autocomplete/) . I am currently testing the new autocomplete plugin and will get things updated for the next release of JMesa.**


As of JMesa 3.0.4 there is support for integrating with the [JQuery Autocomplete](http://docs.jquery.com/Plugins/autocomplete) plugin. There wasn't really anything to it, but I thought it would be nice to have an example right in the project.

What I want to show you here is two examples. The first one is super simple in that it is an autocomplete on one value. The other is more complicated in that it needs a value from a different row column to work.

_Be sure that you include the JQuery Autocomplete plugin in your project! You might also want to do a quick "Hello World" example outside of JMesa so that you understand how the plugin works._

### Simple Example ###

The first thing you will need to do is create a WorksheetEditor that extends the AutoCompleteWorksheetEditor. The only thing you have to define is the url to the controller that will return the list of values (for the autocomplete).

```
import org.jmesa.worksheet.editor.AutoCompleteWorksheetEditor;

public class SingleValueWorksheetEditor extends WorksheetEditor {

    @Override
    protected String getUrl(Object item, String property) {

        String contextPath = getWebContext().getContextPath();
        return contextPath +  "/autocomplete/singlevalue.do";
    }
}
```

If you are using the tag library you need to give the fully qualified path to your custom editor.

```
<jmesa:htmlColumn worksheetEditor="com.mycompany.view.SingleValueWorksheetEditor" />
```

If you are using the API you would not have to even extend the AutoCompleteWorksheetEditor. Just give the url in the constructor and set the editor on the column:

```

htmlColumn.setWorksheetEditor(new AutoCompleteWorksheetEditor("/autocomplete/singlevalue.do"));
```

**Controller**

To be complete, here is an example of what my controller look like:

```
@RequestMapping
public String singlevalue(@RequestParam(value = "q") String key, HttpServletResponse response) {

    StringBuffer contents = new StringBuffer();

    if (StringUtils.hasText(key)) {
        List<String> values = autocompleteService.getBySearchKey(key.toUpperCase());
        for (String value : values) {
            contents.append(value).append("\n");
        }
    }

    streamContents(contents, response);
    return null;
}
```

### More Complicated Example ###

In this example we want the autocomplete values to be based on the value of the previous column (in the same row).

So, like the previous example the first thing you will need to do is create a WorksheetEditor that extends the AutoCompleteWorksheetEditor. Now however, you also need to define the getWsColumn() method. Really we might be able to abstract this out better too. It looks like the only thing that really changed was the name of the JavaScript method to call. In this example that would be createWsMultiValueAutoCompleteColumn. But this will work for now, and takes all the mystery on how things work!

```
public class MultiValueWorksheetEditor extends AutoCompleteWorksheetEditor {

    @Override
    protected String getWsColumn(WorksheetColumn worksheetColumn, Object value, Object item, String property) {
        if (isRowRemoved(getCoreContext().getWorksheet(), getColumn().getRow(), item)) {
            if (value == null) {
                return "";
            }
            return value.toString();
        }

        HtmlBuilder html = new HtmlBuilder();

        Limit limit = getCoreContext().getLimit();

        html.div();

        html.append(getStyleClass(worksheetColumn));

        html.onmouseover("$.jmesa.setTitle(this, event)");
        html.onclick(getUniquePropertyJavaScript(item) + "$.jmesa.createWsMultiValueAutoCompleteColumn(this, '" + limit.getId() + "'," + UNIQUE_PROPERTY + ",'"
            + getColumn().getProperty() + "','" + getUrl(item, property) + "')");
        html.close();
        html.append(value);
        html.divEnd();

        return html.toString();
    }

    @Override
    protected String getUrl(Object item, String property) {

        String contextPath = getWebContext().getContextPath();
        return contextPath +  "/autocomplete/multivalue.do";
    }
}
```

If you are using the tag library you need to give the fully qualified path to your custom editor.

```
<jmesa:htmlColumn worksheetEditor="com.mycompany.view.MultiValueWorksheetEditor" />
```

If you are using the API, just set the value on the column:

```

htmlColumn.setWorksheetEditor(new MultiValueWorksheetEditor("/autocomplete/multivalue.do"));
```

The extra work that you need to do is create the custom JavaScript so that you can pass the other values that you need to your controller. In this case I am including the firstVal parameter. Feel free to include this method in your jquery.jmesa.js file. Just remember that you have that in there so when you upgrade JMesa that you do not accidentally lose your work!

_Note: the "parent().parent().parent().prev().children" may not give me a lot of style points, but it was simple to do :)_

```
createWsMultiValueAutoCompleteColumn : function(column, id, uniqueProperties, property, url) {
    if (wsColumn) {
        return;
    }

    wsColumn = new classes.WsColumn(column, id, uniqueProperties, property);

    var cell = $(column);
    var width = cell.width();
    var originalValue = cell.text();

    /* Enforce the width with a style. */
    cell.width(width);
    cell.parent().width(width);
    cell.css('overflow', 'visible');

    cell.html('<div id="wsColumnDiv"><input id="wsColumnInput" name="' + property + '" style="width:' + (width + 3) + 'px" value=""/></div>');

    $('input[name=' + property + ']').autocomplete(url,
        { extraParams: {  
                firstVal: function() { return $('input[name=' + property + ']').parent().parent().parent().prev().children(':first').text(); }
            }, max:50
        });

    var input = $('#wsColumnInput');
    input.val(originalValue);
    input.focus();
    if (jQuery.browser.msie) { /* IE need a second focus */
        input.focus();
    }

    this.wsColumnKeyEvent(cell, input, originalValue);

    $('#wsColumnInput').blur(function() {
        $.jmesa.validateAndSubmitWsColumn(cell, input, originalValue);
    });
},
```


**Controller**

Again, to be complete, here is an example of what my controller looks like (at least the signature).

```
@RequestMapping
public String multiValue(@RequestParam(value = "q") String key, @RequestParam(value = "firstVal") String firstVal,
            HttpServletResponse response) {
    ...
}
```

As you can see this was quite a bit more work than the single value example. In the future we may be able to make this easier. One of the issues though would be creating something generic that would be useful for enough use cases.

### Other Notes ###

We do find that we have to hit the spacebar to get the autocomplete to come up, especially with the multi-column example. However, it has not been a problem in that our users expect this behavior now.