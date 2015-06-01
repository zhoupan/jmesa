One of the JMesa users, Andy Van Den Heuvel, just submitted an example of how to do a droplist for a WorksheetEditor. I plan on using this as a starting point to implement in the project. I will probably go with a more dynamic select list in the same vein as how the filter works. In the meantime I am going to post this code so other developers can use it.

#### Java WorksheetEditor ####

```
public class DroplistWorksheetEditor extends AbstractWorksheetEditor {

    private Map<String, String> options;

    public DroplistWorksheetEditor(Map<String, String> options) {
        this.options = options;
    }

    public Object getValue(Object item, String property, int rowcount) {
        Object value = null;
        WorksheetColumn worksheetColumn = getWorksheetColumn(item, property);
        if (worksheetColumn != null) {
            value = worksheetColumn.getChangedValue();
        } else {
            value = getCellEditor().getValue(item, property, rowcount);
        }

        return getWsColumn(worksheetColumn, value, item);
    }

    private String getWsColumn(WorksheetColumn worksheetColumn, Object value, Object item) {
        HtmlBuilder html = new HtmlBuilder();
        Limit limit = getCoreContext().getLimit();

        html.select();
        html.onchange(getUniquePropertyJavaScript(item) + "submitWsDroplistColumn(this, '" + limit.getId() + "'," + UNIQUE_PROPERTY + ",'" + getColumn().getProperty() + "', '" + value + "')");
        html.end();

        for (Map.Entry<String, String> option : options.entrySet()) {
            html.option().value(option.getKey());
            if (value.equals(option.getKey())) {
                html.selected();
            }
            html.end();
            html.append(option.getValue());
            html.optionEnd();
        }
        return html.toString();
    }
}
```

#### Javascript ####

```
function submitWsDroplistColumn(column, id, uniqueProperties, property, originalValue) {
    wsColumn = new WsColumn(column, id, uniqueProperties, property);
    var changedValue = column.options[column.selectedIndex].value;
    submitWsColumn(originalValue, changedValue);
    wsColumn = null;
}
```