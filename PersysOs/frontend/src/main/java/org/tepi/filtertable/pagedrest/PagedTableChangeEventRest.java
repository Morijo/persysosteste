package org.tepi.filtertable.pagedrest;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PagedTableChangeEventRest implements Serializable {
    private final PagedFilterTableRest<?> table;

    public PagedTableChangeEventRest(PagedFilterTableRest<?> table) {
        this.table = table;
    }

    public PagedFilterTableRest<?> getTable() {
        return table;
    }

    public int getCurrentPage() {
        return table.getCurrentPage();
    }

    public int getTotalAmountOfPages() {
        return table.getTotalAmountOfPages();
    }
}
