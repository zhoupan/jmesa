The CoreContext interface represents the core package and is the real plumbing. With the CoreContext you have direct access to the Items, Messages and Preferences. In addition you can specify your own filter matching strategy by type or item property if needed.

The Items are the Collection of Beans or Collection of Maps broken down after it is filtered, after it is sorted, and by the current page. The CoreContext extends Items so you always have direct access to the data.

There is quite a bit more to creating the Items if you are using JMesa to do the filtering, sorting and paging. However this functionality is hidden by some very powerful interfaces with an implementation that is very easy to work with and easy to plug in your own strategies.

The [Messages](Messages.md) are the Locale specific text. The default implementation uses Java ResourceBundles. The CoreContext extends Messages so you always have direct access to the Locale specific text in your table.

The [Preferences](Preferences.md) are used for a lot of the configuration instead of hardcoding the values. The default implementation uses Java Properties. If you need to override any of these default settings you can do so by creating your own jmesa.properties file and then change the values you want. The CoreContext extends Preferences so you always have direct access to how things are configured.

### Example ###
You can get the CoreContext directly from the Limit:

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
tableFacade.setItems(items);
CoreContext coreContext = tableFacade.getCoreContext();
```

#### Disabling Filtering And Sorting ####
If you are manually filtering and sorting be sure to call the performFilterAndSort() method. And, just to be clear, the reason you would call this method is because you have already filtered and sorted the items, so there is no reason to do it again. This does not mean that the table does not show up in the filtered or sorted state.

```
tableFacade.performFilterAndSort(false);
```

### Custom Filters ###
The CoreContext will filter the items based on the class type and/or property. If you need to add a custom filter or are trying to support a custom class type then you need to add your own [filter matching strategy](FilterMatcher.md).