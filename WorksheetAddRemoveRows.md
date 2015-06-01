In version 2.5 we now have the ability to add and remove rows in the worksheet. In addition there is also a very flexible validation framework.

<img src='http://jmesa.googlecode.com/svn/trunk/jmesa/resources/wiki/worksheet.png' />

### Worksheet Example ###

The code for this example is based on using the new WorksheetTableFacadeTemplate, which extends the [TableFacadeTemplate](TableFacadeTemplate.md) to offer worksheet specific functionality. The template is a powerful extension to the framework and makes working with the worksheet very easy. In general I recommend using the new template for creating tables, but when working with the new worksheet features I would say it is mandatory.

The following code is based on the worksheet [controller](http://code.google.com/p/jmesa/source/browse/trunk/jmesaWeb/src/org/jmesaweb/controller/WorksheetPresidentController.java) in the examples war file. You should take a look at that to see how these code snippets fit in the overall template. If you need more information about working with the worksheet you should look at the [tutorial](WorksheetTutorial.md), but keep in mind that I still need to convert that tutorial over to use the template.

### Adding Rows ###

The only thing you need to do to turn on the add worksheet row feature is enable the button on the toolbar via the [preferences](Preferences.md). Then by using the WorksheetTableFacadeTemplate you automatically gain support for adding rows to the worksheet. To save a row that was added refer to the section (below) on saving the worksheet. _Note: this feature is only supported by the API._

```
html.toolbar.addWorksheetRow.enabled=true
```

If you have a custom toolbar you need to add the new proper toolbar type.

```
addToolbarItem(ToolbarItemType.ADD_WORKSHEET_ROW_ITEM);
```

### Removing Rows ###

To enable the feature of removing a row from the worksheet involves using a special worksheet editor. Then by using the WorksheetTableFacadeTemplate you automatically gain support for removing rows from the worksheet. To save a row that was removed refer to the section (below) on saving the worksheet. _Note: this feature is only supported by the API._

In this example we declared a made up column called "remove" and added it as the first column. _In general if you declare a column that does not exist JMesa will assume that this is a custom column._

```
HtmlColumn remove = htmlRow.getColumn("remove");
remove.getCellRenderer().setWorksheetEditor(new RemoveRowWorksheetEditor());
remove.setTitle("&nbsp;");
remove.setFilterable(false);
remove.setSortable(false);
```

By default the RemoveRowWorksheetEditor will allow you to remove any row in the  worksheet. If you only want to remove rows that were added you can use the RemoveRowAddedWorksheetEditor instead.

### Saving Worksheet ###

The nice thing about using the worksheet template is that you no longer have to check to see if there are values that need saving explicitly. Instead you will be notified if the worksheet has changes by overriding the saveWorksheet() method of the template. Once you get the worksheet you can use the normal WorksheetCallbackHandler to simplify iterating over the worksheet rows. The one thing that is new however is the WorksheetRowStatus should be checked to see what needs to be done with each row. _Note: this feature is available regardless of if you use the API or the tag library._

```
@Override
protected void saveWorksheet(Worksheet worksheet) {
    worksheet.processRows(new WorksheetCallbackHandler() {
        public void process(WorksheetRow worksheetRow) {
            if (worksheetRow.getRowStatus().equals(WorksheetRowStatus.ADD)) {
                // would save the new President here
            } else if (worksheetRow.getRowStatus().equals(WorksheetRowStatus.REMOVE)) {
                // would delete the President here
            } else if (worksheetRow.getRowStatus().equals(WorksheetRowStatus.MODIFY)) {
                // would update the President here
            }
        }
    });
}
```

### Validation ###

The new validation framework is based on the [bassistance](http://bassistance.de/jquery-plugins/jquery-plugin-validation/) jQuery plugin.

The bassistance plugin is seamlessly integrated into the JMesa framework. All you need to do to use default validation is add the validation to the column. You can add as many validations to the column as you want.

_Do not forget to add the bassistance JavaScript to your project as well!_

#### API ####
```
HtmlColumn firstName = htmlRow.getColumn("name.firstName");
firstName.setTitle("First Name");
firstName.addWorksheetValidation(new WorksheetValidation(WorksheetValidationType.REQUIRED, WorksheetValidation.TRUE));
```

You can also add an error message like so.

```
new WorksheetValidation(WorksheetValidationType.REQUIRED, WorksheetValidation.TRUE).setErrorMessage("The first name is required.");
// OR
new WorksheetValidation(WorksheetValidationType.REQUIRED, WorksheetValidation.TRUE).setErrorMessageKey("message.firstName.required");
```


#### Tag ####

For the tag you need to separate the validation name and value with a colon. If you have multiple validations you need to separate the name/value pairs with a semi-colon.

```
<jmesa:htmlColumn property="name.firstName" title="First Name" worksheetValidation="required:true"/>
```

You can also add an error message by separating the validation name and message with a colon. If you have multiple validations you need to separate the name/message pairs with a semi-colon.

```
<jmesa:htmlColumn property="name.firstName" title="First Name" worksheetValidation="required:true" errorMessage="required:The first name is required" />
<!-- OR -->
<jmesa:htmlColumn property="name.firstName" title="First Name" worksheetValidation="required:true" errorMessageKey="required:message.firstName.required" />
```

#### Validation Methods ####

All the normal [validation methods](http://docs.jquery.com/Plugins/Validation) are supported. As a convenience I am listing the types here. For more information about what each one is and how to use it you need to read the jQuery [docs](http://docs.jquery.com/Plugins/Validation).

```
public class WorksheetValidationType {
    public static final String REQUIRED = "required";
    public static final String EMAIL = "email";
    public static final String URL = "url";
    public static final String DATE = "date";
    public static final String DATE_ISO = "dateISO";
    public static final String NUMBER = "number";
    public static final String DIGITS = "digits";
    public static final String CREDIT_CARD = "creditcard";
    public static final String ACCEPT = "accept";
    public static final String MAX_LENGTH = "maxlength";
    public static final String MIN_LENGTH = "minlength";
    public static final String RANGE_LENGTH = "rangelength";
    public static final String RANGE = "range";
    public static final String MAX_VALUE = "max";
    public static final String MIN_VALUE = "min";
}
```

#### Custom Validation ####
Apart from inbuilt validations, you can also provide a javascript function as validation. For that define a javascript function which returns true / false (valid / invalid). The value of column will be passed to the function.

```
function validateFirstName(firstName) {
	if (firstName == 'foo') {
		return false;
	}
	return true;
}
```

#### API ####

```
HtmlColumn firstName = htmlRow.getColumn("name.firstName");
firstName.setTitle("First Name");
firstName.addCustomWorksheetValidation(new WorksheetValidation("validateFirstName", "validateFirstName").setErrorMessage("You cannot use foo as a value"));
```

#### Tag ####
```
<jmesa:htmlColumn property="name.firstName" title="First Name" customWorksheetValidation="validateFirstName:validateFirstName" errorMessage="validateFirstName:You cannot use foo as a value" />
```


#### Debugging Validation ####

The validation is new so there is a chance you might have to look under the covers to see what the generated JavaScript looks like when the page loads. So if you are having problems you should first do a view source on the page. Then look for something that looks like the following code. The generated code should be valid validation code as required by the bassistance plugin. If it is not you should submit a bug report so that we can take care of it.

```
jQuery.jmesa.setValidator('worksheet', $(jQuery.jmesa.getFormByTableId('worksheet')).validate({
	rules: {
		'name.firstName' : { required: true }
	},
	showErrors: function(errorMap, errorList) {
		jQuery.jmesa.setError('worksheet', errorMap);
	},
	onsubmit: false,
	onfocusout: false,
	onkeyup: false,
	onclick: false
}));
// Following will be generated only if custom validation is used
jQuery.validator.addMethod('validateFirstName', validateFirstName);
```

_If you do not have it already you should get the [firebug](http://getfirebug.com/) plugin installed to help you track down any errors. In general you should get firebug as it makes web development so much easier._