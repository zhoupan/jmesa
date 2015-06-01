The View interface represents the view package and is used to build your views. The default views right now are HTML, PDF, Excel and CSV. The other types of views will be built as I get time.

To build your view you will work with [components](Components.md), [renderers](Renderers.md), and [editors](Editors.md). A component is either a table, row, or column and defines attributes that are common across all views, regardless of the renderer used. A renderer defines the styling attributes for a specific view and is used to render a component a particular way. Lastly, the editor works with a renderer and defines what the actual data looks like. Editors can easily be used for many different types of views, and then decorated for a specific view.

One thing to keep in mind is each type of view has its own components, renderers and editors. This is a huge boon for developers as they have max control with how tables are created and allows for maximum reuse.

The View interface is responsible for rendering the markup.