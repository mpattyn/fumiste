package casi.fortement.pojo;

import java.util.List;

public class SteamUser {
	
	private String id;
	private String pwd;
	private List<String> jeux;
	
	public List<String> getJeux() {
		return jeux;
	} 
	public void setJeux(List<String> jeux) {
		this.jeux = jeux;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
