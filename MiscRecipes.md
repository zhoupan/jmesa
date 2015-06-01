#### Why are my characters showing up as "?" on the screen? ####

You need to add the following to the top of the JSP to get it to render correctly:

```
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
```

#### Can I use AJAX with JMesa? ####

Yes, for sure. All you need to do is build a table using the API and then use some AJAX technology to get the latest display. There is a pretty good writeup about it on the [Javascript](Javascript.md) page. You can also take a look at the limit example in the examples source code as that uses AJAX. Remember you can get the examples source in the downloads section.

#### Property Editor not registered with the PropertyEditorManager Error? ####

When trying to use the JSP Tag for JMesa I receive this message when trying to display the JSP page that contains the table: <Unable to convert string "${presidents}" to class "java.util.Collection" for attribute "items": Property Editor not registered with the PropertyEditorManager>.

The solution is to make sure you have the slf4j-log4j12.jar in your application.

