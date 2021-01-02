package com.online.helling.zaehler;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ServletFactoryBean {
	
	@Bean
	public TomcatServletWebServerFactory embeddedServletContainerFactory() {
		return new TomcatServletWebServerFactory() {
			protected void customizeConnector(Connector connector) {
				int maxSize = 100;
				super.customizeConnector(connector);
				connector.setMaxPostSize(maxSize);
				connector.setMaxSavePostSize(maxSize);
				connector.setPort(8085);
				if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {

					((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(maxSize);
					logger.info("Set MaxSwallowSize " + maxSize);
				}
			}
		};

	}
}