Using AJAX with JMesa tables works seamlessly. The reason is because creating a table with the API is no different than creating one using AJAX. The only additional things you need to do is modify your onInvokeAction() method to work with the XMLHttpRequest object and have your Controller put the updated table markup in the DOM. Hopefully that doesn't sound intimidating, because as you will learn shortly you can get that to work with the [basic tutorial](BasicTutorialV3.md) in just a few lines of code.

_This information was originally in the [Javascript](Javascript.md) page and I thought it deserved an example of its own._

Also keep in mind that when using AJAX you have to use the API to build your table. There is no support for using AJAX when using the tag library.

#### Controller Modifications ####

In the basic example what we do is get the html for the table and then put that into the request as an attribute.

```
tableModel.setTable(getHtmlTable());
String view = tableModel.render();
request.setAttribute("presidents", view);
```

Now, what we will do is check if we are using AJAX. My approach is to send a parameter that lets the Controller know that AJAX is being used. Then you need to get the bytes from the rendered HTML markup and write it out to the response. _With Spring returning null tells the Spring framework that we are writing out to the response directly._

```
tableModel.setTable(getHtmlTable());
String view = tableModel.render();

String ajax = request.getParameter("ajax");
if (ajax != null && ajax.equals("true")) {
    byte[] contents = view.getBytes();
    response.getOutputStream().write(contents);
    return null;
}
```

#### JSP HTML Modifications ####

In the basic example what we need to do is put the rendered HTML in a form.

```
<form name="presidentsForm" action="${pageContext.request.contextPath}/basic.run">
    ${presidents}
</form>
```

Now, with AJAX, what we will be doing is wrapping the rendered HTML in a form and a div element. The div element gives us a placeholder in the DOM to put the HTML when it comes back from the Controller when using AJAX.

```
<form name="presidentsForm" action="${pageContext.request.contextPath}/basic.run">
    <div id="presidents">
        ${presidents}
    </div>
</form>
```

#### JSP JavaScript Modifications ####

In the basic example what we do is use form submits to go back to the Controller and get the latest HTML display. This JavaScript creates hidden fields that contain the information that says how the user interacted with the table and then submits the form.

```
function onInvokeAction(id) {
    setExportToLimit(id, '');
    createHiddenInputFieldsForLimitAndSubmit(id);
}
```

Now, with AJAX, what we need to do is use the XMLHttpRequest object to make a request to the server and go to our Controller. The response is the HTML markup. One thing that changed now is that we need to create a parameter string that contains the information that tell us how the user interacted with the table. If you notice this is also where I pass the ajax=true parameter that tells my Controller that I am making an AJAX request.

This example also uses jQuery to do the AJAX request. If you are unfamiliar with jQuery you should also notice how the '#presidents' corresponds with the div element id that we set up as a placeholder element.

```
function onInvokeAction(id) {
    setExportToLimit(id, '');

    var parameterString = createParameterStringForLimit(id);
    $.get('${pageContext.request.contextPath}/basic.run?ajax=true&' + parameterString, function(data) {
        $("#presidents").html(data)
    });
}
```