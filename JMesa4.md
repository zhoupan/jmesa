Here is a quick list of changes in JMesa 4.0.

  * The filter and worksheet columns are now simple HTML input tags
    * The reason we had the dynamic div/input tags is years ago some browsers (IE6) had a difficult time rendering input tags well in a `<td>` element. That does not seem to be the case anymore so we are finally going to regular HTML tags
  * The JMesa JQuery file has been greatly simplified
    * This is in part as a result of not having to use the input/div tags. In addition JMesa is embracing being a form component even more and no longer carrying a domain model in JavaScript. Now all state attributes are hidden input fields
  * Refactored back the Toolbar/ToolbarItem implementations to just have a simple render() method.
    * The exisiting implementation of the toolbar was completely overbaked and just had to be redone. This is a breaking change but it needed to be done to clean things up
  * The ViewExporter can be set on the TableModel
  * The export file name can be set on the TableModel
  * Removed all previous deprecated methods


#### Things To Consider ####

I dropped the worksheet client side validation as we currently know it. It was just too difficult to work with and error prone. What I want to put in its place is some sort of server side plugable validation framework. That way we do not have to carry the complexity in a specific JavaScript plugin like we have now. From the users perspective nothing will change as we will still need to communicate errors to the client, but it would be validated on the server and work with a JQuery callback.

In general the worksheet needs to give better feedback as the user is working. Right now you only get feedback if some action is performed and the table is refreshed.