package Server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Grizzly {

    public static final String BASE_URI = "http://localhost:8082/api/";

    public static HttpServer startServer() {
    	
        final ResourceConfig rc = new ResourceConfig().packages("RestServices");
        //rc.register(MultiPartFeature.class);
        rc.register(new corsFilter());
        rc.register(corsFilter.class);
        rc.register(new middleware());
        
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

	public static void main(String[] args) throws IOException {
		HttpServer server = null;
		try {
            server = startServer();
            
        	System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        	System.in.read();
		}catch(Exception e) {
			System.out.println(e);
		}finally {
        	server.shutdownNow();
		}
    }
}

