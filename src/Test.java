
public class Test {

	public static void main(String[] args) {
		
		String results ,url;
		
		//url = "https://empiredesnovels.fr/remonster/";
		//url = "https://empiredesnovels.fr/legends-of-ogre-gate/";
		
		url = "https://empiredesnovels.fr/remonster/remonster-jours-01-10/";
		
		DlNovel n = new DlNovel(url);
		
		n.parseNovel("Remonster-Jours-01-10" , url);
		//n.parseChapters(url);
		results = n.getText();
		//results = n.getContent().toString();
		System.out.println(results);
		
		String path = System.getProperty("user.home");
				
		n.writeContentInHtml(path, "remonster.html");		
	}

}
