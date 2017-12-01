/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * @author haiko
 *
 */
public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    private static final Logger log = LogManager.getLogger(LambdaHandler.class);

    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        if (handler == null) {
            try {
                handler = SpringLambdaContainerHandler.getAwsProxyHandler(ApplicationConfig.class);
            } catch (ContainerInitializationException e) {
                log.error("Cannot initialize spring handler", e);
                return null;
            }
            catch(RuntimeException e) {
            	log.error(e.getLocalizedMessage());
            	throw new RuntimeException("runtime error", e.getCause());
            }
        }
        
        return handler.proxy(awsProxyRequest, context);
    }
}
