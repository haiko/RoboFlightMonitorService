/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * @author haiko
 * 
 * inspired by https://medium.com/@joeyvmason/developing-serverless-applications-with-spring-mvc-and-aws-lambda-151f0e7a2602.
 *
 */
@Configuration
public class IntegrationTestConfig {

    @Autowired
    private ConfigurableWebApplicationContext applicationContext;

    @Bean
    public MockLambdaContext lambdaContext() {
        return new MockLambdaContext();
    }

    @Bean
    public SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> springLambdaContainerHandler() throws ContainerInitializationException {
        SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler = SpringLambdaContainerHandler.getAwsProxyHandler(applicationContext);
        handler.setRefreshContext(false);
        handler.initialize();
        return handler;
    }

}
