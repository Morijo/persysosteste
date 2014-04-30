 package org.tepi.filtertable.pagedrest;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.tepi.filtertable.FilterTable;
import org.vaadin.haijian.CSVExporter;
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.haijian.PdfExporter;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public  class PagedFilterTableRest<T extends Container.Indexed & Container.Filterable & Container.ItemSetChangeNotifier>
        extends FilterTable {

    public interface PageChangeListener {
        public void pageChanged(PagedTableChangeEventRest event);
    }
    
    public static Boolean EXPORT_VISIBLE = true;
    public static Boolean EXPORT_INVISIBLE = false;
  
    private List<PageChangeListener> listeners = null;

    private PagedFilterTableContainerRest<T> container;

    private IPagedRest restCallback = null;
    
    public PagedFilterTableRest(String caption, IPagedRest restCallback) {
        super(caption);
        this.restCallback = restCallback;
        setPageLength(50);
        addStyleName("pagedtable");
    }

    protected VerticalLayout exportar(String title){
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		
		PdfExporter pdfExporter = new PdfExporter(this.getContainerDataSource(),this.getColumnHeaders());
			pdfExporter.setCaption("Exportar para PDF");
			pdfExporter.setWidth("100%");
			pdfExporter.setWithBorder(false);
			pdfExporter.setHeader(title);
			pdfExporter.setWithBorder(true);
			pdfExporter.setVisible(true);
			verticalLayout.addComponent(pdfExporter);
			
			CSVExporter csvExporter = new CSVExporter(this.getContainerDataSource(),this.getVisibleColumns());
			csvExporter.setWidth("100%");
			csvExporter.setCaption("Exportar para CSV");
			csvExporter.setVisible(true);
			verticalLayout.addComponent(csvExporter);
			
			ExcelExporter excelExporter = new ExcelExporter(this.getContainerDataSource(),this.getVisibleColumns());
			excelExporter.setWidth("100%");
			excelExporter.setCaption("Exportar para Excel");
			excelExporter.setVisible(true);
			verticalLayout.addComponent(excelExporter);
		
			return verticalLayout;	
		}
		
    
    public HorizontalLayout createControls(Boolean export) {
        Label itemsPerPageLabel = new Label("Itens por página:");
        itemsPerPageLabel.setSizeUndefined();
        final ComboBox itemsPerPageSelect = new ComboBox();

        itemsPerPageSelect.addItem("50");
        itemsPerPageSelect.setImmediate(true);
        itemsPerPageSelect.setNullSelectionAllowed(false);
        itemsPerPageSelect.setWidth("50px");
        itemsPerPageSelect.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;

            @Override
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                setPageLength(Integer.valueOf(String.valueOf(event
                        .getProperty().getValue())));
            }
        });
        itemsPerPageSelect.select("50");
        Label pageLabel = new Label("Página:&nbsp;", ContentMode.HTML);
        final TextField currentPageTextField = new TextField();
        currentPageTextField.setValue(String.valueOf(getCurrentPage()));
        currentPageTextField.setConverter(new StringToIntegerConverter() {
            @Override
            protected NumberFormat getFormat(Locale locale) {
                return super.getFormat(UI.getCurrent().getLocale());
            }
        });
        Label separatorLabel = new Label("&nbsp;/&nbsp;", ContentMode.HTML);
        final Label totalPagesLabel = new Label(
                String.valueOf(getTotalAmountOfPages()), ContentMode.HTML);
        currentPageTextField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        currentPageTextField.setImmediate(true);
        currentPageTextField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;

            @Override
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                if (currentPageTextField.isValid()
                        && currentPageTextField.getValue() != null) {
                    int page = Integer.valueOf(String
                            .valueOf(currentPageTextField.getValue()));
                    setCurrentPage(page);
                }
            }
        });
        pageLabel.setWidth(null);
        currentPageTextField.setColumns(3);
        separatorLabel.setWidth(null);
        totalPagesLabel.setWidth(null);

        HorizontalLayout controlBar = new HorizontalLayout();
        HorizontalLayout pageSize = new HorizontalLayout();
        HorizontalLayout pageManagement = new HorizontalLayout();
        final Button first = new Button("<<", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            @Override
            public void buttonClick(ClickEvent event) {
                setCurrentPage(0);
            }
        });
        final Button previous = new Button("<", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            @Override
            public void buttonClick(ClickEvent event) {
                previousPage();
            }
        });
        final Button next = new Button(">", new ClickListener() {
            private static final long serialVersionUID = -1927138212640638452L;

            @Override
            public void buttonClick(ClickEvent event) {
                nextPage();
            }
        });
        final Button last = new Button(">>", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            @Override
            public void buttonClick(ClickEvent event) {
                setCurrentPage(getTotalAmountOfPages());
            }
        });
        first.setStyleName(Reindeer.BUTTON_LINK);
        previous.setStyleName(Reindeer.BUTTON_LINK);
        next.setStyleName(Reindeer.BUTTON_LINK);
        last.setStyleName(Reindeer.BUTTON_LINK);

        itemsPerPageLabel.addStyleName("pagedtable-itemsperpagecaption");
        itemsPerPageSelect.addStyleName("pagedtable-itemsperpagecombobox");
        pageLabel.addStyleName("pagedtable-pagecaption");
        currentPageTextField.addStyleName("pagedtable-pagefield");
        separatorLabel.addStyleName("pagedtable-separator");
        totalPagesLabel.addStyleName("pagedtable-total");
        first.addStyleName("pagedtable-first");
        previous.addStyleName("pagedtable-previous");
        next.addStyleName("pagedtable-next");
        last.addStyleName("pagedtable-last");

        itemsPerPageLabel.addStyleName("pagedtable-label");
        itemsPerPageSelect.addStyleName("pagedtable-combobox");
        pageLabel.addStyleName("pagedtable-label");
        currentPageTextField.addStyleName("pagedtable-label");
        separatorLabel.addStyleName("pagedtable-label");
        totalPagesLabel.addStyleName("pagedtable-label");
        first.addStyleName("pagedtable-button");
        previous.addStyleName("pagedtable-button");
        next.addStyleName("pagedtable-button");
        last.addStyleName("pagedtable-button");

        pageSize.addComponent(itemsPerPageLabel);
        pageSize.addComponent(itemsPerPageSelect);
        pageSize.setComponentAlignment(itemsPerPageLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(itemsPerPageSelect,
                Alignment.MIDDLE_LEFT);
        pageSize.setSpacing(true);
        pageManagement.addComponent(first);
        pageManagement.addComponent(previous);
        pageManagement.addComponent(pageLabel);
        pageManagement.addComponent(currentPageTextField);
        pageManagement.addComponent(separatorLabel);
        pageManagement.addComponent(totalPagesLabel);
        pageManagement.addComponent(next);
        pageManagement.addComponent(last);
        pageManagement.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(pageLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(currentPageTextField,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(separatorLabel,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(totalPagesLabel,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(next, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(last, Alignment.MIDDLE_LEFT);
        pageManagement.setWidth(null);
        pageManagement.setSpacing(true);
        
        if(export){
        Button  buttonExportar = new Button("Exportar",new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                final Window w = new Window();

                w.setModal(true);
                w.setClosable(false);
                w.setResizable(false);
                w.addStyleName("edit-dashboard");

                getUI().addWindow(w);

                w.setContent(new VerticalLayout() {
                    {
                  	  
                        addComponent(exportar(""));

                        addComponent(new HorizontalLayout() {
                            {
                                setMargin(true);
                                setSpacing(true);
                                addStyleName("footer");
                                setWidth("100%");

                                Button ok = new Button("Fechar");
                                ok.addStyleName("wide");
                                ok.addStyleName("default");
                                ok.addClickListener(new ClickListener() {
                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        w.close();
                                    }
                                });
                                ok.setClickShortcut(KeyCode.ENTER, null);
                                addComponent(ok);
                                setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
                            }
                        });

                    }
                });

            }
        });
  		buttonExportar.setWidth("-1px");
  		buttonExportar.setHeight("-1px");
  		buttonExportar.setVisible(true);
  		
  		controlBar.addComponent(buttonExportar);
  		controlBar.setExpandRatio(buttonExportar, 1.0f);
        }
        
        controlBar.addComponent(pageSize);
        controlBar.addComponent(pageManagement);
        controlBar.setComponentAlignment(pageManagement,
                Alignment.MIDDLE_CENTER);
        controlBar.setWidth(100, Unit.PERCENTAGE);
        controlBar.setExpandRatio(pageSize, 1);
        addListener(new PageChangeListener() {
            private boolean inMiddleOfValueChange;

            @Override
            public void pageChanged(PagedTableChangeEventRest event) {
                if (!inMiddleOfValueChange) {
                    inMiddleOfValueChange = true;
                    first.setEnabled(container.getStartIndex() > 0);
                    previous.setEnabled(container.getStartIndex() > 0);
                    next.setEnabled(container.getStartIndex() < container
                            .getRealSize() - getPageLength());
                    last.setEnabled(container.getStartIndex() < container
                            .getRealSize() - getPageLength());
                    currentPageTextField.setValue(String
                            .valueOf(getCurrentPage()));
                    totalPagesLabel.setValue(Integer
                            .toString(getTotalAmountOfPages()));
                    itemsPerPageSelect
                            .setValue(String.valueOf(getPageLength()));
                    inMiddleOfValueChange = false;
                }
            }
        });
        return controlBar;
    }

    @Override
    public PagedFilterTableContainerRest<T> getContainerDataSource() {
        return container;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setContainerDataSource(Container newDataSource) {
        if (!(newDataSource instanceof Container.Indexed)
                || !(newDataSource instanceof Container.Filterable)) {
            throw new IllegalArgumentException(
                    "PagedFilteringTable can only use containers that implement Container.Indexed AND Container.Filterable");
        }
        PagedFilterTableContainerRest<T> pagedFilteringTableContainer = new PagedFilterTableContainerRest<T>(
                (T) newDataSource);
        pagedFilteringTableContainer.setPageLength(getPageLength());
        container = pagedFilteringTableContainer;
        super.setContainerDataSource(pagedFilteringTableContainer);
        firePagedChangedEvent();
    }

    private void setPageFirstIndex(int firstIndex) {
        if (container != null) {
            if (firstIndex <= 0) {
                firstIndex = 0;
            }
            if (firstIndex > container.getRealSize() - 1) {
                int size = container.getRealSize() - 1;
                int pages = 0;
                if (getPageLength() != 0) {
                    pages = (int) Math.floor(0.0 + size / getPageLength());
                }
                firstIndex = pages * getPageLength();
            }
            container.setStartIndex(firstIndex);
            containerItemSetChange(new Container.ItemSetChangeEvent() {
                private static final long serialVersionUID = -5083660879306951876L;

                @Override
                public Container getContainer() {
                    return container;
                }
            });
            if (alwaysRecalculateColumnWidths) {
                for (Object columnId : container.getContainerPropertyIds()) {
                    setColumnWidth(columnId, -1);
                }
            }
            firePagedChangedEvent();
        }
    }

    private void firePagedChangedEvent() {
        if (listeners != null) {
            PagedTableChangeEventRest event = new PagedTableChangeEventRest(this);
            for (PageChangeListener listener : listeners) {
                listener.pageChanged(event);
            }
        }
    }

    @Override
    public void setPageLength(int pageLength) {
        if (pageLength >= 0 && getPageLength() != pageLength) {
            container.setPageLength(pageLength);
            super.setPageLength(pageLength);
            firePagedChangedEvent();
        }
    }

    public void nextPage() {
        setPageFirstIndex(container.getStartIndex() + getPageLength());
        restCallback.load(container.getStartIndex(), getPageLength());
    }

    public void previousPage() {
        setPageFirstIndex(container.getStartIndex() - getPageLength());
    }

    public int getCurrentPage() {
        double pageLength = getPageLength();
        int page = (int) Math.floor(container.getStartIndex() / pageLength) + 1;
        if (page < 1) {
            page = 1;
        }
        return page;
    }

    public void setCurrentPage(int page) {
        int newIndex = (page - 1) * getPageLength();
        if (newIndex < 0) {
            newIndex = 0;
        }
        setPageFirstIndex(newIndex);
    }

    public int getTotalAmountOfPages() {
        int size = container.getContainer().size();
        double pageLength = getPageLength();
        int pageCount = (int) Math.ceil(size / pageLength);
        if (pageCount < 1) {
            pageCount = 1;
        }
        return pageCount;
    }

    public void addListener(PageChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<PageChangeListener>();
        }
        listeners.add(listener);
    }

    public void removeListener(PageChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<PageChangeListener>();
        }
        listeners.remove(listener);
    }

    @Override
    public void resetFilters() {
        super.resetFilters();
        setCurrentPage(1);
    }
   
}