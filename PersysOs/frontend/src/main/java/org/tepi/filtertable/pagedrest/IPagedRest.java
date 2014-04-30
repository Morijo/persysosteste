package org.tepi.filtertable.pagedrest;

import com.github.wolfie.refresher.Refresher;

public interface IPagedRest {

	public void load(Integer start, Integer size);
	public void update(Refresher source);
	
}
