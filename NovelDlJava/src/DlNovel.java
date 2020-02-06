import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;

public class DlNovel {
	private String url;
	private String chapterUrl;
	private Document doc;
	
	private Book book;
	
	private String title;
	
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
			
			this.title = this.doc.select("h1.entry-title").text();
			
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
		
		String htmlPath = this.writeContentInHtml(filePath, fileName);
		
		this.book = new Book();
		
		book.getMetadata().addTitle(this.title);
		
		book.getMetadata().addAuthor(new Author( this.getAuthorFromUrl() ));
		
		book.addSection(this.title, new Resource(htmlPath));
		
		EpubWriter ew = new EpubWriter();
		
		try {
			ew.write(book, new FileOutputStream(htmlPath));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String writeContentInHtml(String filePath, String fileName) {
		String p = filePath +"\\"+ fileName;
		p = p.replace('\\', '/');
		
		try {
			
			PrintWriter pw = new PrintWriter(p);
			
			pw.print(this.content.toString());
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
//		try {
//			p = "file://" + p;
//			p = p.replace('\\', '/');
//			
//			java.awt.Desktop.getDesktop().browse(new URI(p));
//		} catch (IOException | URISyntaxException e) {
//			e.printStackTrace();
//		}
		
		return p;
	}
	
	public String getAuthorFromUrl() {
		Pattern p = Pattern.compile("://*.*");
		Matcher m = p.matcher(this.url);
		
		String s = "";
		
		if(m.find()) {
			s = m.group(0);
		}
		
		s = s.substring(3);
		
		return s;
	}
	
	public String getText() {
		return this.textContent;
	}
	
	public String getTitle() {
		return this.title;
	}
}
