The WebContext is an adapter for the servlet request. A WebContext allows the table to be used in multiple containers.

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
WebContext webContext = tableFacade.getWebContext();
```