#### What do I do if my framework does not give me access to the servlet request and response objects? ####

In my opinion, frameworks that do not offer a way to reach the request and response objects are taking things too far. At a minimum they should offer a way to let other frameworks still get at those objects. What I did was create a servlet filter to set the request and response in a thread local object.

First setup the filter:
```
<filter>
    <filter-name>TableFacadeFilter</filter-name>
    <filter-class>org.jmesa.facade.filter.TableFacadeFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>TableFacadeFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping> 
```

Then use a static import to get at the request and response:

```
import static org.jmesa.facade.filter.TableFacadeFilter.FilterThreadLocal.getHttpServletRequest;
import static org.jmesa.facade.filter.TableFacadeFilter.FilterThreadLocal.getHttpServletResponse;
...

TableModel tableModel = new TableModel(id, getHttpServletRequest(), getHttpServletResponse());
tableModel.setExportTypes(CSV, JEXCEL, PDF);
...
```