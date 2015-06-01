By default JMesa will filter your items. The items being the Collection of Beans or Collection of Maps.

However, there is no association between the cell editor and the filtering. That was a design decision for quite a few reasons. For one I wanted a clear detachment between the view and the items. The reason for that is using the API to filter is a feature and for large result sets you have to custom filter anyway. The larger issue is performance. To filter based on the cell editor the API would have to resolve each column and build a separate items object.

The JMesa filtering mechanism is very flexible and allows you to easily plug in your own filter strategy. You plug in your filter strategy via the TableModel

For example if you are using the API you would do the following:

```
public void addFilterMatcher(MatcherKey key, FilterMatcher matcher);
```

We will start by looking at the MatcherKey and FilterMatcher in more depth, and then come back to how we add them to the TableModel.

The MatcherKey is used to find the proper FilterMatcher strategy. MatcherKey is an immutable object that has two constructors. One takes only a class type, and the other takes a class type and property. This allows you to match filters either by a specific class type, or more specifically, by class type and for a given column property.

```
public class MatcherKey {
    public MatcherKey(Class type)

    public MatcherKey(Class type, String property)
}
```

However the actual work of whether or not an item is filtered is the work of the FilterMatcher interface:

```
public interface FilterMatcher {
    public boolean evaluate(Object itemValue, String filterValue);
}
```

The itemValue is the current item value from the current Bean or Map in the Collection. The filterValue is the value entered by the user in the table filter. With these two pieces of information the FilterMatcher will evaluate whether a match was or was not found.

Note: When working with the filter matchers supplied by the JMesa API you do not have to inject the WebContext specifically into the matchers. See the [support interfaces](SupportInterfaces.md) page for more details on that.

### Example ###
As an example this is what the default string filter matcher looks like:
```
public class StringFilterMatcher implements FilterMatcher {
    public boolean evaluate(Object itemValue, String filterValue) {
        String item = StringUtils.lowerCase(String.valueOf(itemValue));
        String filter = StringUtils.lowerCase(String.valueOf(filterValue));
        if (StringUtils.contains(item, filter)) {
            return true;
        }

        return false;
    }
}
```

Then just add this to the TableModel.
```
tableModel.addFilterMatcher(new MatcherKey(Object.class), new StringFilterMatcher());
```

This is saying that all filters that represent an Object class will be matched against the StringFilterMatcher, which we now know is case-insensitive. If you specify a more specific filtering strategy then that will be used. Keep in mind that the (above) example is the default so that is already set up for you. Hopefully by examining this one it should make sense on how you could easily come up with your own filtering strategy.

#### DateFilterMatcher Example ####
If one of your columns is using the [DateCellEditor](Editors.md), and is filterable, then you will want to have the filter strategy match. To that is simple, as you just need to declare the DateFilterMatcher.

```
tableModel.addFilterMatcher(new MatcherKey(Date.class, "born"), new DateFilterMatcher("MM/yyyy"));
```

Keep in mind that if all your date columns have the same pattern you could do this:
```
tableModel.addFilterMatcher(new MatcherKey(Date.class), new DateFilterMatcher("MM/yyyy"));
```

By taking off the "born" property it is saying any column of type Date will use this matcher as the filter strategy.

#### NumberFilterMatcher Example ####
If one of your columns is using the [NumberCellEditor](Editors.md), and is filterable, then you will want to have the filter strategy match. To that is simple, as you just need to declare the NumberFilterMatcher.

```
tableModel.addFilterMatcher(new MatcherKey(Integer.class, "salary"), new NumberFilterMatcher("###,##0.00"));
```

Keep in mind that if all your integer columns have the same pattern you could do this:
```
tableModel.addFilterMatcher(new MatcherKey(Integer.class), new NumberFilterMatcher("###,##0.00"));
```

By taking off the "salary" property it is saying any column of type Integer will use this matcher as the filter strategy.

### Tags FilterMatcherMap ###
If using the tag library then you need to implement the FilterMatcherMap interface and plug it into the tableModel tag to declare your filter matchers.

For example if you have a column that uses a special editor such as the DateCellEditor and you want to have the JMesa API filter the results you will need to register custom FilterMatchers. With the tag library you would do this through the filterMatcherMap attribute.

For instance if you have a column that looks like this:
```
<jmesa:htmlColumn property="born" pattern="MM/yyyy" cellEditor="org.jmesa.view.editor.DateCellEditor"/>
```

You now need to set up a custom FilterMatcherMap.

```
public class TagFilterMatcherMap implements FilterMatcherMap {
    public Map<MatcherKey, FilterMatcher> getFilterMatchers() {
        Map<MatcherKey, FilterMatcher> filterMatcherMap = new HashMap<MatcherKey, FilterMatcher>();
        filterMatcherMap.put(new MatcherKey(Date.class, "born"), new DateFilterMatcher("MM/yyyy"));
        return filterMatcherMap;
    }
}
```

Lastly, make sure you define the filterMatcherMap on the TableModelTag:

```
<jmesa:tableModel
        id="tag" 
        filterMatcherMap="org.jmesaweb.controller.TagFilterMatcherMap"
```

#### FilterMatcherMap in API ####
Even though the FilterMatcherMap is generally used for the tag library to set the filter matchers I added the FilterMatcherMap to the TableModel as well. By adding this to the TableModel it means you can use the same object for the exports. You could also use this for the general API if desired.

```
tableModel.addFilterMatcherMap(new TagFilterMatcherMap());
```