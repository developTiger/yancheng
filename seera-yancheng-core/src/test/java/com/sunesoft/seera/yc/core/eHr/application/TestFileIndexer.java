/*
package com.sunesoft.seera.syms.eHr.application;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

*/
/**
 * Created by MJ006 on 2016/7/6.
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestFileIndexer {
    @Test
    public void test1() throws Exception {
        */
/*  指明要索引文件夹的位置,这里是C盘的source文件夹下  *//*

        File fileDir = new File("d:\\source ");
        */
/*  这里放索引文件的位置  *//*

        File indexDir = new File("d:\\index");
        Directory dir = FSDirectory.open(indexDir);//将索引存放在磁盘上
        Analyzer lucenAnalyzer = new StandardAnalyzer(Version.LUCENE_36);//分析器
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, lucenAnalyzer);
        iwc.setOpenMode(OpenMode.CREATE);//创建新的索引文件create 表示创建或追加到已有索引库
        IndexWriter indexWriter = new IndexWriter(dir, iwc);//把文档写入到索引库
        File[] textFiles = fileDir.listFiles();//得到索引文件夹下所有文件
        long startTime = new Date().getTime();
        //增加document到检索去
        for (int i = 0; i < textFiles.length; i++) {
//          if (textFiles[i].isFile()&& textFiles[i].getName().endsWith(".txt")) {
            System.out.println(":;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
            System.out.println("File" + textFiles[i].getCanonicalPath() + "正在被索引...");
            String temp = FileReaderAll(textFiles[i].getCanonicalPath(), "utf-8");
            System.out.println(temp);
            Document document = new Document();
            Field FieldPath = new Field("path", textFiles[i].getPath(), Field.Store.YES, Field.Index.NO);
            Field FieldBody = new Field("body", temp, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            NumericField modifiField = new NumericField("modified");//所以key为modified
            modifiField.setLongValue(fileDir.lastModified());
            document.add(FieldPath);
            document.add(FieldBody);
            document.add(modifiField);
            indexWriter.addDocument(document);

//          }
        }
        indexWriter.close();
        //计算一下索引的时间
        long endTime = new Date().getTime();
        System.out.println("花了" + (endTime - startTime) + "毫秒把文档添加到索引里面去" + fileDir.getPath());


        String index="d:\\index";//搜索的索引路径
        IndexReader reader=IndexReader.open(FSDirectory.open(new File(index)));
        IndexSearcher searcher=new IndexSearcher(reader);//检索工具
        ScoreDoc[] hits=null;
        String queryString="a";  //搜索的索引名称
        Query query=null;
        Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_36);
        try {
            QueryParser qp=new QueryParser(Version.LUCENE_36,"body",analyzer);//用于解析用户输入的工具
            query=qp.parse(queryString);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (searcher!=null) {
            TopDocs results=searcher.search(query, 10);//只取排名前十的搜索结果
            hits=results.scoreDocs;
            Document document=null;
            for (int i = 0; i < hits.length; i++) {
                document=searcher.doc(hits[i].doc);
                String body=document.get("body");
                String path=document.get("path");
                String modifiedtime=document.get("modifiField");
                System.out.println(body+"        ");
                System.out.println(path);
            }
            if (hits.length>0) {
                System.out.println("找到"+hits.length+"条结果");
            }
            searcher.close();
            reader.close();
        }
    }

    public static String FileReaderAll(String FileName, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FileName), charset));
        String line = new String();
        String temp = new String();
        while ((line = reader.readLine()) != null) {
            temp += line;
        }
        reader.close();
        return temp;
    }
    @Test
    public void test2() throws ParseException, IOException {

        String index="d:\\index";//搜索的索引路径
        IndexReader reader=IndexReader.open(FSDirectory.open(new File(index)));
        IndexSearcher searcher=new IndexSearcher(reader);//检索工具
        ScoreDoc[] hits=null;
        String queryString="a";  //搜索的索引名称
        Query query=null;
        Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_36);
        try {
            QueryParser qp=new QueryParser(Version.LUCENE_36,"body",analyzer);//用于解析用户输入的工具
            query=qp.parse(queryString);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (searcher!=null) {
            TopDocs results=searcher.search(query, 10);//只取排名前十的搜索结果
            hits=results.scoreDocs;
            Document document=null;
            for (int i = 0; i < hits.length; i++) {
                document=searcher.doc(hits[i].doc);
                String body=document.get("body");
                String path=document.get("path");
                String modifiedtime=document.get("modifiField");
                System.out.println(body+"        ");
                System.out.println(path);
            }
            if (hits.length>0) {
                System.out.println("找到"+hits.length+"条结果");
            }
            searcher.close();
            reader.close();
        }


    }
}
*/
