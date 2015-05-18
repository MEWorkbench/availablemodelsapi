package pt.uminho.ceb.biosystems.mew.availablemodelsapi.ds;

import java.util.List;

import pt.uminho.ceb.biosystems.mew.availablemodelsapi.Format;

public class ModelInfo {

	protected Integer id;
	protected String organism;
	protected String taxonomy;
	protected String name;
	protected String author;
	protected String year;
	protected String publicationURL;
	protected List<Format> formats;

	public ModelInfo(Integer id, String name, String org, 
			String taxonomy, String author, String year,
			String publicationURL, List<Format> formats) {
		this.id = id;
		this.name = name;
		this.organism = org;
		this.taxonomy = taxonomy;
		this.author = author;
		this.year = year;
		this.publicationURL = publicationURL;
		this.formats = formats;
	}

	public void print() {
		System.out.println("Model: " + id);
		System.out.println("\tname: " + name);
		System.out.println("\torganism: " + organism);
		System.out.println("\taxonomy: " + taxonomy);
		System.out.println("\tauthor:" + author);
		System.out.println("\tyear:" + year);
		System.out.println("\turl:" + publicationURL);
		System.out.println("\tformats: " + formats);
		
		
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	public String getOrganism() {
		return organism;
	}

	public String getAuthor() {
		return author;
	}

	public String getYear() {
		return year;
	}

	public String getPublicationURL() {
		return publicationURL;
	}

	public List<Format> getFormats() {
		return formats;
	}
	
	public String getTaxonomy() {
		return taxonomy;
	}
	
}
