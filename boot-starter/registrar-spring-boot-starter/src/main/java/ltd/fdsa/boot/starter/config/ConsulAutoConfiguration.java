package ltd.fdsa.boot.starter.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecwid.consul.transport.TLSConfig;
import com.ecwid.consul.transport.TLSConfig.KeyStoreInstanceType;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@ConditionalOnClass(StarterService.class)
//@EnableConfigurationProperties(StarterServiceProperties.class)
@Configuration
public class ConsulAutoConfiguration implements ApplicationListener<ApplicationStartedEvent> {

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		log.info("==========ApplicationStartedEvent ServiceRegister start===========");
		try {
			this.createConsulClient().agentServiceRegister(this.createNewService());
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
		log.info("=========ApplicationStartedEvent ServiceRegister end===========");
	}

	@Value("${spring.cloud.consul.host:localhost}")
	String agentHost;
	@Value("${spring.cloud.consul.port:8500}")
	int agentPort;
//	@Value("${spring.cloud.consul.tls.type:8500}")
//	KeyStoreInstanceType keyStoreInstanceType;
//	@Value("${spring.cloud.consul.tls.certificatePath}")
//	String certificatePath;
//	@Value("${spring.cloud.consul.tls.certificatePassword}")
//	String certificatePassword;
//	@Value("${spring.cloud.consul.tls.keyStorePath}")
//	String keyStorePath;
//	@Value("${spring.cloud.consul.tls.keyStorePassword}")
//	 String keyStorePassword;
//		 

	@Bean(name = "consulClient")
	// @ConfigurationProperties("spring.cloud.consul.host.port")
	public ConsulClient createConsulClient() {
		log.debug("new Consul Client");
		// TODO TLSConfig tlsConfig = new TLSConfig();
		return new ConsulClient(this.agentHost, this.agentPort);
	}

	@Value("${spring.application.name:defalutapp}")
	String applicationName;
	@Value("${server.port:8080}")
	int applicationPort;

	private NewService createNewService() {
		log.debug("new service");
		NewService service = new NewService();
		service.setAddress(this.getHost());
		service.setId(this.getHost());
		service.setName(this.applicationName);
		service.setPort(this.applicationPort);
		return service;
	}

	private String host;

	public String getHost() {
		if (Strings.isNullOrEmpty(host)) {
			try {
				InetAddress address = InetAddress.getLocalHost();
				this.host = address.getHostAddress();
			} catch (UnknownHostException e) {
				this.host = "localhost";
			}
		}
		return host;
	}
}