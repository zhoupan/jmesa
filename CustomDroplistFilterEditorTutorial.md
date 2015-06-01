This is a tutorial submitted by Ryan Tromp. What you will learn is that there is not much to creating your own droplist filter editor, but there are specific steps that you must go through to do it.

### FilterEditor ###

The first thing you need to do is extend the current DroplistFilterEditor in the API and add your own options.

```
public class AvailableDroplistFilterEditor extends DroplistFilterEditor {
   @Override
   protected List<Option> getOptions()  {
      List<Option> options = new ArrayList<Option>();
      options.add(new Option("available","Available"));
      options.add(new Option("unavailable", "Unavailable"));
      return options;
   }
}
```

Now plug in your newly created editor.

#### API ####

```
HtmlColumn column = new HtmlColumn("available");
column.setFilterEditor(new AvailableDroplistFilterEditor());
```

#### Tag ####

```
<jmesa:htmlColumn property="available" filterEditor="com.mycompany.view.AvailableDroplistFilterEditor" />
```

### FilterMatcher ###

Implement a custom [FilterMatcher](FilterMatcher.md).

```
public class AvailableFilterMatcher implements FilterMatcher {
   public boolean evaluate(Object itemValue, String filterValue) {

      String item = StringUtils.lowerCase(String.valueOf(itemValue));
      String filter = StringUtils.lowerCase(String.valueOf(filterValue));

      if ((filter.equals("available") && item.equals("true")) ||
         (filter.equals("unavailable") && item.equals("false"))) {
            return true;
      }

      return false;
   }
}
```

Lastly, add the filter matcher to the table model before you start setting up your columns.

#### API ####

```
tableModel.addFilterMatcher(new MatcherKey(String.class, "available"), new AvailableFilterMatcher());
```

#### Tag ####

For the tag library we need to use the [FilterMatcherMap](FilterMatcher.md) interface.

```
<jmesa:tableModel filterMatcherMap="com.mycompany.AvailableFilterMatcherMap" />
```