package Model;

import java.util.ArrayList;

public class Role {

	private String id;
	private String name;
	private String icon;
	private String default_path;
	private Boolean ricettaEnabled;
	private ArrayList<Pages> pages;
	public Role(String id, String name, String icon, String default_path, ArrayList<Pages> pages, Boolean ricettaEnabled) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.default_path = default_path;
		this.pages = pages;
		this.setRicettaEnabled(ricettaEnabled);
	}
	public Role() {
		this.pages = new ArrayList<Pages>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDefault_path() {
		return default_path;
	}
	public void setDefault_path(String default_path) {
		this.default_path = default_path;
	}
	public ArrayList<Pages> getPages() {
		return pages;
	}
	public void setPages(ArrayList<Pages> pages) {
		this.pages = pages;
	}
	public Boolean getRicettaEnabled() {
		return ricettaEnabled;
	}
	public void setRicettaEnabled(Boolean ricettaEnabled) {
		this.ricettaEnabled = ricettaEnabled;
	}
	
}

