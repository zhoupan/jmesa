#### Where can I find the JSP Tag attribute definitions? ####
All the JSP tag attributes can be found [here](JSPTagAttributes.md).

#### How can I get at the original row bean or map using the custom interfaces? ####

Technically, when using the tag library, under the hood the item is swapped out for a Map for each row. Because JMesa works with beans and maps completely transparently this works really well and is completely transparent. However, the map will only contain the values for the columns defined, and not the other values from the original bean. Now the original bean can be recovered from the map in your custom code. The bean can be recovered from the map by the TableModelTag var attribute.

For example if the tableModel looks like this:
```
<jmesa:tableModel
   id="tag" 
   ...
   var="bean"
>
```

Notice how the original item can be recovered by the var attribute! It is recommended that you use the ItemUtils because that abstracts out whether or not the item is a bean or a map, which is perfect for this.

```
public class TagRowEvent implements RowEvent {
    public String execute(Object item, int rowcount) {
        Object bean = ItemUtils.getItemValue(item, "bean");
        Object id = ItemUtils.getItemValue(bean, "id");
        return "document.location='http://www.whitehouse.gov/history/presidents?id=" + id + "'";
    }
}
```