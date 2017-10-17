package es.caib.plugins.arxiu.filesystem.test;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import es.caib.plugins.arxiu.filesystem.Utils;

public class HelloLucene {
    public static void main(String[] args) throws IOException, ParseException {
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer(new CharArraySet(Collections.emptySet(), true));
        
        // 1. create the index
        //Directory index = new RAMDirectory();
        FSDirectory index = FSDirectory.open(Paths.get("/home/moisesp/Limit/programs/arxiu/test.lucene"));

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w = new IndexWriter(index, config);
        
//        addDoc(w, "1970-01-01T00:00:00.010Z", "193398817");
//        addDoc(w, "1970-01-01T00:00:00.011Z", "55320055Z");
//        addDoc(w, "1970-01-01T00:00:00.012Z", "55063554A");
//        addDoc(w, "1970-01-01T00:00:00.013Z", "9900333X");
//        addDoc(w, new Date(10));
//        addDoc(w, new Date(11));
//        addDoc(w, new Date(12));
//        addDoc(w, new Date(13));
//        addDoc(w, new Date(14));
//        addDoc(w, new Date(15));
        w.close();

        // 2. query
        String querystr = args.length > 0 ? args[0] : "date:[* TO 12]";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        // Query q = new QueryParser("title", analyzer).parse(querystr);
        QueryParser queryParser = new QueryParser(null, analyzer);
        queryParser.setAllowLeadingWildcard(true);
        
        Query q = queryParser.parse(querystr);
//        Query q = queryParser.parse(querystr);
        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
//            int docId = hits[i].doc;
//            Document d = searcher.doc(docId);
//            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
        	Document d = searcher.doc(hits[i].doc);
        	Date date = new Date(Long.parseLong(d.get("date")));
        	System.out.println(Utils.formatDateIso8601(date));
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
    }

    /*private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));

        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
    
    private static void addDoc(IndexWriter w, Date date) throws IOException {
        
//    	FieldType type = new FieldType();
//        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
//        type.setStored(true);
//        type.setOmitNorms(true);
//        type.setTokenized(true);
//        type.setStoreTermVectors(true);
//        
//    	Document doc = new Document();
//        doc.add(new Field(
//        		"date", 
//        		DateTools.dateToString(date, DateTools.Resolution.MILLISECOND),
//        		type));
//        w.addDocument(doc);
    	
    	Document doc = new Document();
        doc.add(new TextField("date", String.valueOf(date.getTime()), Field.Store.YES));
        w.addDocument(doc);
    }*/
    
    
}