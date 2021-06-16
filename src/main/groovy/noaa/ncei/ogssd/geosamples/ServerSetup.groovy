package noaa.ncei.ogssd.geosamples

import groovy.util.logging.Slf4j
import org.apache.catalina.connector.Connector
import org.apache.coyote.ajp.AbstractAjpProtocol
import org.apache.coyote.ajp.AjpNioProtocol
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter


@Slf4j
@Configuration
class ServerSetup {
    @Value('${ajp.port:8009}')
    int ajpPort

    @Value('${ajp.enabled:true}')
    boolean ajpEnabled

    @Value('${ajp.secret:none}')
    String ajpSecret

    @Value('${ajp.server.address:default}')
    String serverAddress

    @Bean
    ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory()

        if (ajpEnabled) {
//            Connector ajpConnector = new Connector("AJP/1.3")
//            AbstractAjpProtocol protocol =  (AbstractAjpProtocol)ajpConnector.getProtocolHandler()
            Connector ajpConnector = new Connector("org.apache.coyote.ajp.AjpNioProtocol")
            AjpNioProtocol protocol= (AjpNioProtocol)ajpConnector.getProtocolHandler()

            if (serverAddress != 'default') {
                log.debug("setting server address for AJP connection to ${serverAddress}")
                // can be IP address for fully-qualified hostname
                protocol.setAddress(InetAddress.getByName(serverAddress))
            }

            if (ajpSecret == 'none') {
                log.warn("using AJP without secret. might be vulnerable to know security exploits")
                protocol.setSecretRequired(false)
            } else {
                log.debug("setting AJP secret. make sure set corresponding worker parameter in Apache ProxyPass")
                protocol.setSecret(ajpSecret)
                protocol.setSecretRequired(true)
            }
            ajpConnector.setPort(ajpPort)
            ajpConnector.setSecure(true)
            ajpConnector.setScheme("ajp")
            ajpConnector.setAllowTrace(false)
            tomcat.addAdditionalTomcatConnectors(ajpConnector)
        }

        return tomcat;
    }


    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter()

        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(25000)  // number of characters
        filter.setIncludeHeaders(false)
        filter.setAfterMessagePrefix("REQUEST DATA : ")

        return filter
    }
}