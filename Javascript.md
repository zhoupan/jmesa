The table actions are controlled by a Javascript model that mirrors the Limit object. This makes it very natural to work with the Java Limit and the Javascript Limit in a very consistant manner. The actions, of course, are how the table is filtered, sorted, paginated and the max rows.

For the most part you do not have to be concerned with how the Javascript model looks or even works. What you do need to do is call the correct function to convert the Javascript Limit in a format that can be sent to the server. Your two choices are to either build a parameter string, or create hidden form fields.

```
createParameterStringForLimit(id) // create parameter string

createHiddenInputFieldsForLimitAndSubmit(id); // create hidden form fields and submit the form

createHiddenInputFieldsForLimit(id) // just create hidden form fields
```

It really is that easy. However, you might be wondering when you should invoke one of these functions.

### Example ###

What you need to understand is that when you click on one of the table actions it will invoke a local Javascript function called onInvokeAction(id) that you implement. This is what a typical implementation looks like:

```
<script type="text/javascript">
function onInvokeAction(id, action) {
    setExportToLimit(id, '');
    createHiddenInputFieldsForLimitAndSubmit(id);
}
</script>
```

The id parameter is the table id that was invoked, and the action is what the user was doing, such as filtering, sorting, paging, etc.... The action is optional and can be very useful in custom circumstances.

The previous example will create hidden input fields that represent the Limit, and also submit the form that wraps the table. The function setExportToLimit() probably looks a little odd, but it is neccessary because it is up to your client side code to reset the export in case an export was done on the previous call. If you are not doing any exporting then you can exclude the setExportToLimit() call.

If you are doing exporting then you will need to implement the export function onInvokeExportAction(id). This is what a typical implementation of that looks like:

```
<script type="text/javascript">
function onInvokeExportAction(id, action) {
    var parameterString = createParameterStringForLimit(id);
    location.href = '${pageContext.request.contextPath}/presidents.run?' + parameterString;
}
</script>
```

This will create a parameter string that represents the Limit. When exporting you should use a parameter string, and not hidden input fields. This is because an export does not refresh the page so if you create hidden input fields there is no way to delete the hidden input fields once the call to the server completes. This is also why you need to make sure you reset the export for the onInvokeAction() function.

### Action Example ###

The action parameter, for example, would be useful for custom handling of the sorting that overrides the default behavior and to only allow one column to be sorted at a time.

```
function onInvokeAction(id, action) {
    setExportToLimit(id, '');

    if (action == 'sort') {
        var limit = jQuery.jmesa.getTableFacade(id).limit;
        var sortSet = limit.getSortSet();
        if (sortSet) {
            var sort = limit.sortSet[limit.sortSet.length - 1];
            removeAllSortsFromLimit(id);
            limit.sortSet[0] = sort;
        }
    }
    createHiddenInputFieldsForLimitAndSubmit(id);
}
```

### AJAX Example ###

What makes this all the more appealing is when you decide you want to use AJAX. As you will see nothing changes other than you would use the createParameterStringForLimit() function for both table actions as well as the exports. This example makes use of the JQuery toolkit for the AJAX call.

For example on the page we would first put in the javascript link to import the JQuery library.

```
<script type="text/javascript" src="/jmesa/js/jquery-1.1.3.1.pack.js"></script>
```

Then we need to put in the form element and include the JMesa table inside it. Note: the form action element is defined only because it is a required attribute by XHTML standards, however in this example it is never invoked. Also notice how the table markup is included in the form just like you normally would as defined by ${presidents}. In fact there is nothing about this example code that even looks like we are using AJAX.

```
<form name="presidentsForm" action="${pageContext.request.contextPath}/presidents.run">
    <div id="presidents">
        ${presidents}
    </div>
</form>
```

The magic of AJAX comes into play when the onInvokeAction() is called by one of the table actions. Here AJAX is used to call the server, and then the JQuery callback is used to swap out the new HTML markup. Note: if you haven't used it before I highly recommend using the [JQuery](http://jquery.com/) library as it makes working with
JavaScript and AJAX real easy.

```
<script type="text/javascript">
function onInvokeAction(id) {
    setExportToLimit(id, '');
	
    var parameterString = createParameterStringForLimit(id);
    $.get('${pageContext.request.contextPath}/limit.run?ajax=true&' + parameterString, function(data) {
        $("#presidents").html(data)
    });
}

function onInvokeExportAction(id) {
    var parameterString = createParameterStringForLimit(id);
    location.href = '${pageContext.request.contextPath}/presidents.run?' + parameterString;
}
</script>
```