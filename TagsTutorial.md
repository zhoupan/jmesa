The following is a walk-through of an example of creating a JMesa table with the [tag library](Tags.md). This is the same as the [basic tutorial](BasicTutorial.md) but modified to use the tag library. You should notice that it feels just about the same.

You should also look at the [complete example](http://code.google.com/p/jmesa/wiki/TagsExample) to see the example in its full context. Or look [here](TableFacade.md) to see all the examples.

### Setup ###
To start with grab the latest JMesa [download](http://code.google.com/p/jmesa/downloads/list). In there you will find the following: jmesa.jar, jmesa.css, jmesa-pdf.css, jmesa.js, jmesa.tld, default images, and the source code.

The first thing to do is take the jmesa.jar file and place it in your projects WEB-INF/lib directory. Next copy the jmesa.css and jmesa-pdf.css into a folder named css at the top of the web project. Then copy the jmesa.js into a folder named js at the top of the web project. Next copy the jmesa.tld into your products WEB-INF/tld directory. You will also want to grab the latest version of [jQuery](http://www.jquery.com) as JMesa requires that library now. Lastly, copy the default images into a folder named images/table at the top of the web project.

Of course all these files could be placed where ever you want. The places I have been recommending are pretty common, but you could use whatever you want. Here is a visual for you to look at.

  * web
    * css
      * jmesa.css
    * js
      * jmesa.js
    * images
      * table
        * clear.gif
        * filter.gif
        * etc...
    * WEB-INF
      * tld
        * jmesa.tld
      * lib
        * jmesa.jar

Moving these files around to other locations does not require any configuration on the part of JMesa, with the exception of the images. If you would like to use something other than the default images/table directory you need to set up your [Preferences](Preferences.md) and override the html.imagesPath attribute.

```
html.imagesPath=/images/table/
```

### Controller ###
The only thing you need to do in your controller is create a Collection of Beans or Maps and pass it in the request so that the tags can render it.

#### Example ####

This is calling a service to get the list of presidents.

```
Collection<President> items = presidentService.getPresidents();
request.setAttribute("presidents", items);
```

### View ###

In my example I am using JSP to display the table. What the following code is doing is rendering the table inside a form element. The tag.run is what I do to route the request back through the Controller. Generally your form action is going to be the same action that you did to get the table to display in the first place.

In many ways I think it is helpful to think of JMesa as a form component, more like an input or select field. It does not automatically include a form element or deal with parameters passed from other JSP pages. Doing this increases the flexibility quite a bit.

Also notice how the tableFacade items attribute is being set with the presidents items that we put in the request from the controller.

```
<%@ taglib uri="/WEB-INF/tld/jmesa.tld" prefix="jmesa" %>

<form name="presidentsForm" action="${pageContext.request.contextPath}/tag.run">

    <jmesa:tableFacade
        id="tag" 
        items="${presidents}"
        var="bean"
        >
        <jmesa:htmlTable>		
            <jmesa:htmlRow>	
                <jmesa:htmlColumn property="name.firstName"/>
                <jmesa:htmlColumn property="name.lastName" title="Last Name"/>
                <jmesa:htmlColumn property="term"/>
                <jmesa:htmlColumn property="career"/>
                <jmesa:htmlColumn property="born"/>
            </jmesa:htmlRow>
        </jmesa:htmlTable> 
    </jmesa:tableFacade>

</form>
```

![http://www.jmesa.org/wiki/images/api-example-before.gif](http://www.jmesa.org/wiki/images/api-example-before.gif)

#### Modifications ####

However, as great as this is, it is still not perfect. Here is a list of the changes that we need:
  * It would be nice to have a caption describing the table.
  * There should be a fixed width on the table to ensure it fits correctly on the page.
  * The first two column titles do not display very well.
  * The last column is a date and is not formatted.
  * The first column should link off to a page with more information about that row.

##### Table Caption #####

The first thing we will tackle is adding the table caption. To do that we need to define the caption attribute of the htmlTable. _Note: if you want the caption to be Locale specific then use the captionKey attribute instead._

```
<jmesa:htmlTable caption="Presidents"> 
```

##### Table Width #####

The width of a table is on the htmlTable.

```
<jmesa:htmlTable width="600px"> 
```

##### Column Titles #####

We now need to fix the titles of the first two columns. The reason the other column titles display correctly is because the default column title is defined by taking the column property and converting it to a properly formatted title. Or, in other words, it converts a camelcase word to a proper title. The first two columns have nested properties that make it impossible to clean up.

Fixing this is easy. Just pull each column and set the title manually.  _Note: if you want the title to be Locale specific then use the titleKey attribute instead._

```
<jmesa:htmlColumn property="name.firstName" title="First Name">

<jmesa:htmlColumn property="name.lastName" title="Last Name">
```

##### Column Formatting #####

To format a column you need to add a custom cell editor that knows how to format dates. Luckily JMesa includes an editor called the DateCellEditor, and only requires you to add a format pattern.

```
<jmesa:htmlColumn property="born" pattern="MM/yyyy" cellEditor="org.jmesa.view.editor.DateCellEditor"/>
```

So that gets us displaying correctly, but in this case looks aren't everything! We also need to filter the column. For that we need to make sure the filter mechanism knows how to format this column as well so that it can successfully find a match.

To do that add a custom [filter matching](FilterMatcher.md) strategy. Although, what may trip you up somewhat is that you need to define this on the tableFacade.

Its real simple to get this right though. Just create a class that implements the FilterMatcherMap interface.

```
public class TagFilterMatcherMap implements FilterMatcherMap {
    public Map<MatcherKey, FilterMatcher> getFilterMatchers() {
        Map<MatcherKey, FilterMatcher> filterMatcherMap = new HashMap<MatcherKey, FilterMatcher>();
        filterMatcherMap.put(new MatcherKey(Date.class, "born"), new DateFilterMatcher("MM/yyyy"));
        return filterMatcherMap;
    }
}
```

Then plug it the tableFacade with the filterMatcherMap attribute by giving the fully qualified path to your custom class.

```
<jmesa:tableFacade filterMatcherMap="org.jmesaweb.controller.TagFilterMatcherMap">
```

##### Column Link #####

The last thing we need to do is add a column link. The way we do this in the tag is by defining your markup in the column body. To get values from the current row item you just need to use the name you defined as your var attribute on the tableFacade. In my example the var attribute was called bean.

```
<jmesa:htmlColumn property="name.firstName" titleKey="presidents.firstName">
    <a href="http://www.whitehouse.gov/history/presidents/">${bean.name.firstName}</a>
</jmesa:htmlColumn>
```

_You could also define a custom [cellEditor](Editors.md) to do more advanced types of column rendering._


#### Modifications Finished ####

Thats it! We now have a fully functional, and good looking, HTML table!

![http://www.jmesa.org/wiki/images/api-example-after.gif](http://www.jmesa.org/wiki/images/api-example-after.gif)

The last thing we need to do is wire up the JMesa actions to the jmesa.js script. The actions, of course, are the filtering, sorting, paging, and max rows. Most of the time you will just need to create hidden input fields and submit the form, and is what we are doing here.

```
<script type="text/javascript">
function onInvokeAction(id) {
    createHiddenInputFieldsForLimitAndSubmit(id);
}
</script>
```

_Be sure to read the [Javascript](Javascript.md) page so that you understand how Javascript is used to interact with the table and what your options are._