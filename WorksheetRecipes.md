#### How can I test a JMesa Worksheet in my controller? ####

First set up the parameters to tip the API off that we are saving the worksheet. Here we are using the adapter that hooks in with the Spring testing API. If you are using something else you can easily make your own adapter up in just a few lines of code. _If you need help just contact me and I will help you out with that._

```
Parameters parameters = new SpringParametersAdapter(request);
ParametersBuilder builder = new ParametersBuilder(ID, parameters);
builder.setSaveWorksheet();
```

Then create a worksheet object through the API and then use the SessionWorksheetState object to save your object in the test session.

```
Worksheet worksheet = new Worksheet(ID);
WorksheetRow row = new WorksheetRow(new UniqueProperty("id", "2"));
worksheet.addRow(row);

WebContext webContext = new HttpServletRequestWebContext(request);
SessionWorksheetState sessionWorksheetState = new SessionWorksheetState(ID, webContext);
sessionWorksheetState.persistWorksheet(worksheet);
```

#### Is there a way to just have a Save button under the worksheet to save the changes? ####

What you need to do is just create a button like you normally would under the table, and then set the Javascript to call the save action on the worksheet and then call your onInvokeAction script. Be sure to replace the 'id' parameter with the name of your table id.

```
<input type="button" value="Save" class="btn" style="margin-top:10px" onclick="setSaveToWorksheet('id');onInvokeAction('id','save_worksheet')" />
```

Then you probably want to turn off the toolbar save button. To do that create a [custom toolbar](http://code.google.com/p/jmesa/wiki/CustomToolbarTutorial) and remove those toolbar items.

#### Is there a way to not have the worksheet filter button available? ####

The only way to disable that is to create a [custom toolbar](http://code.google.com/p/jmesa/wiki/CustomToolbarTutorial) and remove that toolbar item.

#### How can I create a droplist (select) worksheet editor? ####

It would be great to have a default droplist editor for the worksheet. In the meantime here is an [example](DroplistWorksheetEditor.md) that was sent to me that looks pretty good. There are a few things I would like to do to have it fit more in the vein of how the filter works but it is totally usable as-is.

#### How can I set the UniqueProperty and a primary key made up of a few columns? ####

To keep things more simple and straightforward I decided to not have the UniqueProperty take more than one property...at least for the initial launch of the worksheet. I knew
that it could be expanded in the future and be fully backwards compatible.

The workaround is to create a bean (or map) attribute that combines the attributes you
need and then put something like a pipe between them. Then in your controller you
can parse the values back out.