This is a quick example that shows how you can modify the SimpleRowFilter to do something more custom. It gets around an [edge case](http://code.google.com/p/jmesa/issues/detail?id=162) that I was not sure what to do about yet. I thought I would take the opportunity to put a [support](SupportInterfaces.md) interface in so that custom code could get a hold of the FilterMatcherRegistry to the solve problem as well.

First you need to extend the SimpleRowFilter and override the getFilterMatchers() method.

```
public class CustomSimpleRowFilter extends SimpleRowFilter {

    @Override
    protected Map<Filter, FilterMatcher> getFilterMatchers(Collection<?> items, FilterSet filterSet) {
        Map<Filter, FilterMatcher> filterMatchers = new HashMap<Filter, FilterMatcher>();

        FilterMatcherRegistry registry = getFilterMatcherRegistry();

        // insert custom code

        return filterMatchers;
    }
}
```

Then plug your custom RowFilter into the TableFacade.

  * API
```
tableModel.setRowFilter(new CustomSimpleRowFilter());
```

  * JSP Tag
```
<jmesa:tableModel rowFilter="com.mycompany.CustomSimpleRowFilter">
```