The HtmlColumnsTag contains one attribute called htmlColumnsGenerator.

```
<jmesa:htmlColumns htmlColumnsGenerator="org.jmesaweb.controller.TagHtmlColumnsGenerator"/>
```

The HtmlColumnsGenerator interface is used so that you can add columns on the fly.

```
public interface HtmlColumnsGenerator {
    public List<HtmlColumn> getColumns(HtmlComponentFactory componentFactory);
}
```

### Example ###

```
public class TagHtmlColumnsGenerator extends AbstractContextSupport implements HtmlColumnsGenerator {
    public List<HtmlColumn> getColumns(HtmlComponentFactory componentFactory) {
        List<HtmlColumn> columns = new ArrayList<HtmlColumn>();

        HtmlColumn firstName = componentFactory.createColumn("name.firstName");
        firstName.setTitle("First Name");
        columns.add(firstName);

        HtmlColumn lastName = componentFactory.createColumn("name.lastName");
        lastName.setTitle("Last Name");
        columns.add(lastName);

        HtmlColumn born = componentFactory.createColumn("born");
        columns.add(born);

        return columns;
    }
}
```