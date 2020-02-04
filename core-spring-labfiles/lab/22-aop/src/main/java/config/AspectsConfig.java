package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import rewards.internal.monitor.MonitorFactory;
import rewards.internal.monitor.jamon.JamonMonitorFactory;

//	TODO-04: Update this configuration
//  - Add a class-level annotation to scan for components
//	  located in the rewards.internal.aspects package.
//	- Add another class-level annotation to instruct Spring
//	  to process beans that have the @Aspect annotation.
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "rewards.internal.aspects")
public class AspectsConfig {

	@Bean
	public MonitorFactory monitorFactory(){
		return new JamonMonitorFactory();
	}
	
}
