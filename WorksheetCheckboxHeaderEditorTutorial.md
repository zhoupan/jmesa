When working with the worksheet many times you would like a checkbox in the header that allows you to select and unselect all the rows. This tutorial will describe on how you can do that.

_This custom Javascript for the checkbox is a little buggy. I think you would need to do something like inspect the columns and find out which are checked and which are not checked on loading of the page. The problem is that when you select all the rows and then go to the next page, and then return, the re-checking of the header causes problems. This code could just be viewed as a starting point...._


First create a custom header editor.

```
public class WorksheetCheckboxHeaderEditor extends AbstractHeaderEditor {
    public Object getValue() {
        HtmlBuilder html = new HtmlBuilder();
        html.input().type("checkbox").id("selectAllChkBox").end();
        return html.toString();
    }
}
```

Then plug it into your table as described on the [Editors](Editors.md) page. As a reference this is how I have it declared in my application:

```
<form id="myForm" ...>

<jmesa:htmlColumn property="assoc" title="&nbsp;" sortable="false" filterable="false"
    headerEditor="com.mycompany.view.WorksheetCheckboxHeaderEditor"
    worksheetEditor="org.jmesa.worksheet.editor.CheckboxWorksheetEditor" 
/>
```

Then lastly have jQuery assign an onclick action to your checkbox. The real reason this works is the "this.click()" call. Also note that this table assumes you only have one column that has a checkbox. If you have more then you will have to be more specific about how you bind your checkbox.

```
<script type="text/javascript">
$(document).ready(function() {
    $("#selectAllChkBox").click(function() {
        var isChecked = this.checked;
        $("#myForm :checkbox:not(#selectAllChkBox)").each(function() {
            this.click(); 
            this.checked = isChecked;                     
        });
    }); 
    
});
</script>
```