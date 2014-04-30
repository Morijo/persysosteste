package br.com.rest.resources.filters;

import java.lang.annotation.Annotation;
import java.util.Date;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import br.com.rest.resources.interfaces.Cacheable;

public class CacheControlDecorator implements ContainerResponseFilter {

	 @Override
	 public ContainerResponse filter(ContainerRequest req, ContainerResponse contResp) {
	 
		contResp.getHttpHeaders().add("Modified-Since", new Date());
		for (Annotation a : contResp.getAnnotations()) {
	      if (a.annotationType() == Cacheable.class) {
	        String cc = ((Cacheable) a).cc();
	        contResp.getHttpHeaders().add("Cache-Control", cc);
	      }
	    }
	    return contResp;
  }
}
