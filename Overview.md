JMesa takes a Collection of Beans or a Collection of Maps and uses them to render a table in HTML, XLS, CSV, or any other format you choose. The Beans in the Collection are plain old java objects (POJO) where each attribute has a corresponding getter and setter method. If using maps the attributes would be name-value pairs. You can think of each bean as being one row in the table display. For the remainder of the documentation I may refer to the Collection of Beans and Collection of Maps as simply items (corresponding to the Items interface) to keep down on the amount of typing.

Working with the JMesa API can be broken down into three areas.

### Limit ###

The [Limit](Limit.md) interface represents the limit package and is used so that you can figure out how the user interacted with the table with regards to filtering, sorting, paging and the max rows.

### CoreContext ###

The [CoreContext](CoreContext.md) interface represents the core package and is the real plumbing. With the CoreContext you have direct access to the Items, Messages and Preferences.

The Items are the Collection of Beans or Collection of Maps broken down after it is filtered, after it is sorted, and by the current page. Note: if you not using JMesa to perform the filtering and sorting then everything in the Items will return the same Collection. The CoreContext extends Items so you always have direct access to the data.

There is quite a bit more to creating the Items if you are using JMesa to do the filtering, sorting and paging. However this functionality is hidden by some very powerful interfaces with an implementation that is very easy to work with and easy to plug in your own strategies. I will talk more about that later, but, as an example, if you need to filter a column a certain way you can just by plugging in your own [FilterMatcher](FilterMatcher.md) strategy.

The [Messages](Messages.md) are the Locale specific text. The default implementation uses Java ResourceBundles. The CoreContext extends Messages so you always have direct access to the Locale specific text in your table.

The [Preferences](Preferences.md) are used for a lot of the configuration instead of hardcoding the values. The default implementation uses Java Properties. If you need to override any of these default settings you can do so by creating your own jmesa.properties file and then change the values you want. The CoreContext extends Preferences so you always have direct access to how things are configured.

### View ###

The [View](View.md) interface represents the view package and is used to build your views. The default views right now are HTML, XLS and CSV. The other types of views will be built as I get time.

To build your view you will work with [components](Components.md), [renderers](Renderers.md), and [editors](Editors.md). A component is either a table, row, or column and defines attributes that are common across all views, regardless of the renderer used. A renderer defines the styling attributes for a specific view and is used to render a component a particular way. Lastly, the editor works with a renderer and defines what the actual data looks like. Editors can easily be used for many different types of views, and then decorated for a specific view.

One thing to keep in mind is each type of view has its own components, renderers and editors. This is a huge boon for developers as they have max control with how tables are created and allows for maximum reuse.

### Testing ###
With JMesa you can easily [unit test](UnitTest.md) your tables.