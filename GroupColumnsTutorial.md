Recently at work we started a new admin application. There were a few new features that ultimately made their way into the JMesa API. I thought it would be worthwhile to demo those new features. In addition this is a a great opportunity to show some ways to customize JMesa. _If you just want to be able to group your columns see the notes at the bottom of the page._

The following example will show you how group your columns in a custom way. Then some of the things to notice is that there is a custom header above the normal header, and there is a fancy tooltip hover effect.

![http://jmesa.org/wiki/images/group_columns.gif](http://jmesa.org/wiki/images/group_columns.gif)

#### Tag Declaration ####
This is just your basic JMesa tag. The custom work will be done in the CategoryHierarchyView.

The reason I have a custom date DateTimeCellEditor, DateTimeFilterMatcher, and DateTimeFilterMatcherMap is because we use [Joda Time](http://joda-time.sourceforge.net/) for our date handling at work so I needed to create custom classes to handle it. They are pretty straightforward, and you can see the custom code [here](JodaTimeDateHandling.md).

```
<jmesa:tableFacade
    id="categories"
    items="${categoryList}"
    view="com.mycompany.admin.mvc.view.CategoryHierarchyView"
    filterMatcherMap="com.mycompany.admin.mvc.view.DateTimeFilterMatcherMap"
    var="category">
    <jmesa:htmlTable width="700">
        <jmesa:htmlRow>
            <jmesa:htmlColumn property="category.key" title="Key"/>
            <jmesa:htmlColumn property="category.name" title="Name"/>
            <jmesa:htmlColumn property="category.validFrom" title="Valid From" pattern="MM/yyyy" cellEditor="com.mycompany.admin.mvc.view.DateTimeCellEditor"/>
            <jmesa:htmlColumn property="category.validTo" title="Valid To" pattern="MM/yyyy" cellEditor="com.mycompany.admin.mvc.view.DateTimeCellEditor"/>
            <jmesa:htmlColumn property="category.editorialDescription" title="Editorial" filterable="false" sortable="false"/>
            <jmesa:htmlColumn property="category.staticUrl" title="Url" filterable="false" sortable="false"/>
            <jmesa:htmlColumn property="subcategory.key" title="Key"/>
            <jmesa:htmlColumn property="subcategory.name" title="Name"/>
            <jmesa:htmlColumn property="subcategory.validFrom" title="Valid From" pattern="MM/yyyy" cellEditor="com.mycompany.admin.mvc.view.DateTimeCellEditor"/>
            <jmesa:htmlColumn property="subcategory.validTo" title="Valid To" pattern="MM/yyyy" cellEditor="com.mycompany.admin.mvc.view.DateTimeCellEditor"/>
            <jmesa:htmlColumn property="subcategory.staticUrl" title="Url" filterable="false" sortable="false"/>
        </jmesa:htmlRow>
    </jmesa:htmlTable>
</jmesa:tableFacade>
```

#### CategoryHierarchyView ####

If you take a look at the default [HtmlView](http://code.google.com/p/jmesa/source/browse/trunk/jmesa/src/org/jmesa/view/html/HtmlView.java) class you will see that this custom view is exactly the same for all the code around the '...' marks. What I am demonstrating here are the modifications for my custom view.

The customHeader() method is used to display the Category and Subcategory headers on the table. The reason for this special header is because I have the use case where I have a one to many relationship. What I did was flatten out the listing. In doing so I also wanted an easy way to show that the table really contained two tables worth of information.

The setCustomCellEditors() method is used to group the columns and set the tooltip. The editors always were designed to be useful when using the decorator pattern and this shows a perfect use case. What I am doing is grabbing the original cell editor. I then see if this is either the editorialDescription or staticUrl column. If it is then I decorate the orginal cell editor with the CategoryTooltipCellEditor. Then, lastly, because I want to group the table columns I decorate all the cell editors with the new GroupCellEditor.

```
public class CategoryHierarchyView extends AbstractHtmlView {
    public Object render() {
        setCustomCellEditors();

        ...

        html.append(customHeader());

        ...
    }

    private void setCustomCellEditors() {
        List<Column> columns = getTable().getRow().getColumns();
        for (Column column : columns) {
            CellEditor decoratedCellEditor = column.getCellRenderer().getCellEditor();

            if (column.getProperty().contains("editorialDescription") ||
                column.getProperty().contains("staticUrl")) {
                decoratedCellEditor = new CategoryTooltipCellEditor(decoratedCellEditor);
                column.getCellRenderer().setCellEditor(decoratedCellEditor);
            }

            column.getCellRenderer().setCellEditor(new GroupCellEditor(decoratedCellEditor));
        }
    }

    private String customHeader() {
        HtmlBuilder html = new HtmlBuilder();
        html.tr(1).styleClass("tableHeaderGroup").close();
        html.td(2).colspan("6").style("border-right: 1px solid white").close().append("Category").tdEnd();
        html.td(2).colspan("5").close().append("Subcategory").tdEnd();
        html.trEnd(1);
        return html.toString();
    }
}
```

#### CategoryTooltipCellEditor ####

This custom cell editor is designed to show one of two images in place of the real column value. In addition the column value is placed inside of the title of the image. To get the nice effect I used the [Bassistance](http://bassistance.de/jquery-plugins/jquery-plugin-tooltip/) tooltip [jQuery](http://www.jquery.com) plugin.

```
public class CategoryTooltipCellEditor extends AbstractCellEditor {

    private CellEditor decoratedCellEditor;

    public CategoryTooltipCellEditor(CellEditor decoratedCellEditor) {

        this.decoratedCellEditor = decoratedCellEditor;
    }

    @Override
    public Object getValue(Object item, String property, int rowcount) {

        Object columnValue = decoratedCellEditor.getValue(item, property, rowcount);

        if (columnValue == null || StringUtils.isEmpty(columnValue.toString())) {
            return null;
        }

        HtmlBuilder html = new HtmlBuilder();
        html.img();

        if (property.contains("editorialDescription")) {
            html.src(getWebContext().getContextPath() + "/img/editorial_description.gif");
        } else if (property.contains("staticUrl")) {
            html.src(getWebContext().getContextPath() + "/img/static_url.gif");
        }

        html.styleClass("tooltip").border("0").title("" + columnValue).end();

        return html.toString();
    }
}
```

#### Notes: ####

If you just want to group your columns you can use the new GroupColumnsHtmlView. What it does is extend the HtmlView so that it can decorate each cell editor with the new GroupCellEditor.

To plug the view into the TableModel you will do the following.

#### API ####

```
tableModel.setView(new GroupColumnsHtmlView());
```

#### JSP Tag ####

```
<jmesa:tableModel view="org.jmesa.view.html.GroupColumnsHtmlView">
```