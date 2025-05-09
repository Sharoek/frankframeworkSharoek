/*
   Copyright 2023 WeAreFrank!

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.frankframework.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.Setter;

import org.frankframework.lifecycle.DynamicRegistration.Servlet;
import org.frankframework.lifecycle.servlets.ServletConfiguration;
import org.frankframework.util.SpringUtils;

public class ServletRegisteringPostProcessor implements BeanPostProcessor, ApplicationContextAware {

	private @Setter ApplicationContext applicationContext;
	private ServletManager servletManager;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof Servlet servlet) {
			ServletConfiguration config = SpringUtils.createBean(applicationContext);
			config.fromServlet(servlet);
			servletManager.register(config);
		}

		return bean;
	}

	@Autowired
	public void setServletManager(ServletManager servletManager) {
		this.servletManager = servletManager;
	}
}
