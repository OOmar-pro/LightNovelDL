import java.awt.print.Book;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DlNovel {
	private String url;
	private String chapterUrl;
	private Document doc;
	
	private Elements content;
	
	private ArrayList<String[]> chapters;
	
	private String textContent;
	
	public DlNovel(String u) {
		this.url = u;		
	}
	
	public void parseChapters(String listChapUrl) {
		this.chapters = new ArrayList<String[]>();
		this.chapterUrl = listChapUrl;
		
		String[] s = new String[2];
		
		Elements chapters = null;
		try {
			this.doc = Jsoup.connect(listChapUrl).get();

			chapters = doc.select(".vc_tta-panel .vc_tta-panel-body a");
			
			for(Element e: chapters) {
				
				s[0] = e.text();
				s[1] = e.attr("href");
								
				this.chapters.add(s);
			}
			
			this.content = chapters;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void parseNovel(String title, String url) {
		Elements elts = null;
		
		try {
			this.doc = Jsoup.connect(url).get();
			elts = this.doc.select("#content .single-page-content");
			
			//filtre les donnees parser
			this.content = new Elements();
			
			for(String s: this.content.eachText()) {
				this.textContent += s;
				this.textContent += "\n";
			}
			
			this.content = elts;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Elements getContent() {
		return this.content;
	}
	
	public ArrayList<String[]> getChapters(){
		return this.chapters;
	}
	
	public void writeContentInEpub(String filePath, String fileName) {
		Book b = new Book();
		
		
	}
	
	public void writeContentInHtml(String filePath, String fileName) {
		try {
			
			PrintWriter pw = new PrintWriter(filePath + "\\" + fileName);
			
			pw.print(this.content.toString());
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			String p = "file:///" + filePath +"\\"+ fileName;
			p = p.replace('\\', '/');
			
			java.awt.Desktop.getDesktop().browse(new URI(p));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public String getText() {
		return this.textContent;
	}
}
