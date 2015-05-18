package pt.uminho.ceb.biosystems.mew.availablemodelsapi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import pt.uminho.ceb.biosystems.mew.availablemodelsapi.ds.ModelInfo;
import pt.uminho.ceb.biosystems.mew.availablemodelsapi.ds.ModelsIndex;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;

public class RestClient {
	
	static {
		try {
			File f = new File("config/logger.conf");
			if(!f.exists())
				f.createNewFile();
			PropertyConfigurator.configure("config/logger.conf");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	protected static final String defaultUrl = "http://darwin.di.uminho.pt/models/";
	//Default read timeout 20 seconds;
	protected static int defaultReadTimeout = 20*1000;
	//Default follow redirection is true;
	protected static boolean defaultFollowRedirection = true;
	
	private static final String ID 			= "id";
	private static final String NAME 		= "name";
	private static final String ORGANISM	= "organism";
	private static final String FORMATS		= "formats";
	private static final String VALIDATED 	= "optflux_validated";
	private static final String AUTHOR 		= "author";
	private static final String YEAR 		= "year";
	private static final String DOI 		= "doi";
	private static final String TAXONOMY	= "taxonomy";
	
	protected Client client;
	protected String url;
	protected Logger logger = Logger.getLogger("restclient");;
	
	protected int readTimeout; 
	protected boolean followRedirection;
	
	public RestClient() {
		this(defaultUrl, defaultReadTimeout, defaultFollowRedirection, null);
	}
	
	public RestClient(Proxy proxy) {
		this(defaultUrl, defaultReadTimeout, defaultFollowRedirection, proxy);
	}
	
	public RestClient(String url) {
		this(url, defaultReadTimeout, defaultFollowRedirection, null);
	}
	
	
	
	public RestClient(String url, int readTimeout, boolean followRedirection, Proxy proxy) {
		logger.debug("Initializing client with URI:" + url);
		this.url = url;
		
		client = Client.create();
		
		logger.debug("Creating client with read timeout: " + readTimeout + " and follow redirection: " + followRedirection);
		client.setReadTimeout(readTimeout);
		client.setFollowRedirects(followRedirection);
		
		
	}
	
	public InputStream getSBMLStream(int modelId) throws Exception {
		WebResource webResource = getResource("models", modelId, "sbml_file");
		logger.debug("Fetching " + webResource.toString());
		try{
			String string = webResource.get(String.class);
			return new ByteArrayInputStream(string.getBytes());
		} catch(ClientHandlerException e){
			String message = "Impossible to connect to our server. Please check your connectivity.";
			logger.error(message, e);
			throw new Exception(message);
		}
		
	}
	
	public ModelsIndex index(boolean validated) throws Exception {
		WebResource webResource = getResource("models", "json");
		String res;
		try{
			if(validated)
				webResource = webResource.queryParam(VALIDATED, Boolean.toString(validated));
				
			res = webResource.get(String.class);
		} catch(ClientHandlerException e){
			throw new Exception("Impossible to connect to our server. Please check your connectivity.");
		}
		
		JSONParser parser = new JSONParser();
		int size;
		
		JSONArray array =  (JSONArray) parser.parse(res);
		size = array.size();
		ModelsIndex index = new ModelsIndex(size);
		
		for(int i = 0; i < size; i++) {
			JSONObject entry = (JSONObject) array.get(i);
			Integer id 	= ((Long)entry.get(ID)).intValue();
			String name = (String) entry.get(NAME);
			String org  = (String) entry.get(ORGANISM);
			String author = (String) entry.get(AUTHOR);
			String year = (String) entry.get(YEAR);
			String doi  = (String) entry.get(DOI);
			String taxonomy = (String) entry.get(TAXONOMY);
			String publicationURL = "http://dx.doi.org/" + doi;
			List<Format> formats = new ArrayList<Format>();
			JSONArray formatsRaw = (JSONArray) entry.get(FORMATS);
			for(int j = 0; j < formatsRaw.size(); j++) {
				String value = (String) formatsRaw.get(j);
				formats.add(Format.valueOf(value.toUpperCase()));
			}
			
			ModelInfo info = new ModelInfo(id, name, org, taxonomy, author, 
					year, publicationURL, formats);
			index.add(info);
		}
		
		
		return index;
	} 
	
	public boolean getFollowRedirection() {
		return followRedirection;
	}
	
	public int getReadTimeout() {
		return readTimeout;
	}
	
	public void setFollowRedirection(boolean followRedirection) {
		this.followRedirection = followRedirection;
		client.setFollowRedirects(followRedirection);
	}
	
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	public WebResource getResource(String model, String format) {
		String resourceURI = url + model + "." + format;
		return client.resource(resourceURI);
	}
	
	public WebResource getResource(String model, int id, String method) {
		String resourceURI = url + model + "/" + id + "/" + method;
		return client.resource(resourceURI);
	}
	
	public WebResource getResource(String model) {
		String resourceURI = url + model;
		return client.resource(resourceURI);
	}
	
	
}
