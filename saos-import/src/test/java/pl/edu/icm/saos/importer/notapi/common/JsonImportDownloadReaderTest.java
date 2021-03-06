package pl.edu.icm.saos.importer.notapi.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.springframework.batch.item.ExecutionContext;

import pl.edu.icm.saos.common.json.JsonUtilService;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonImportDownloadReaderTest {

    
    private JsonImportDownloadReader jsonImportDownloadReader = new JsonImportDownloadReader();
    
    private ImportFileUtils importFileUtils = mock(ImportFileUtils.class);
    
    private JsonFactory jsonFactory = mock(JsonFactory.class);
    
    private JsonUtilService jsonUtilService = mock(JsonUtilService.class);
    
    private String importDir = "/path/to/files/";
    
    
    
    @Before
    public void before() {
        
        jsonImportDownloadReader.setImportDir(importDir);
        
        jsonImportDownloadReader.setImportFileUtils(importFileUtils);
        
        jsonImportDownloadReader.setJsonFactory(jsonFactory);
        
        jsonImportDownloadReader.setJsonUtilService(jsonUtilService);
        
    }
    

    
    @Test
    public void open() {
        
        // prepare data
        
        File importFile1 = new File("f1.json");
        File importFile2 = new File("f2.json");
        File importFile3 = new File("f3.json");
        List<File> importFiles = Lists.newArrayList(importFile1, importFile2, importFile3);
        when(importFileUtils.listImportFiles(importDir)).thenReturn(importFiles);
        
        ExecutionContext ctx = Mockito.mock(ExecutionContext.class);
        
        
        // execute tested method
        
        jsonImportDownloadReader.open(ctx);
        
        
        // assert
        
        Mockito.verifyZeroInteractions(ctx);
        
        assertDownloadReaderState(importFile1, null, null, importFile2, importFile3);
        
        
    }

    
    
    @Test
    public void read_CurrentFileNull() throws Exception {
        
        // prepare
        
        resetDownloadReaderState(null, null, null);
        
        // execute
        
        String result = jsonImportDownloadReader.read();
        
        // assert
        
        assertNull(result);
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void read_FileReaderNull_JsonParserNotNull() throws Exception {
        
        // prepare
        
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        when(jsonParser.isClosed()).thenReturn(false);
        
        resetDownloadReaderState(Mockito.mock(File.class), null, jsonParser);
        
        
        // execute
        
        jsonImportDownloadReader.read();
        
        
    }
    
    
    @Test
    public void read_FileReaderNull_JsonParserNull_NextNodeNotNull() throws Exception {
        
        // prepare
        File currentFile = new File("f1.json");
        Reader fileReader = Mockito.mock(Reader.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        List<File> importFiles = Lists.newArrayList(new File("f2.json"));
        String nextNode = "JUDGMENTDATA";
        
        when(importFileUtils.getReader(Mockito.eq(currentFile))).thenReturn(fileReader);
        when(jsonFactory.createParser(Mockito.eq(fileReader))).thenReturn(jsonParser);
        when(jsonUtilService.nextNode(Mockito.eq(jsonParser))).thenReturn(nextNode);
        
        resetDownloadReaderInternal(currentFile, null, null, importFiles);
        
        
        // execute
        
        String judgment = jsonImportDownloadReader.read();
        
        
        // assert 
        
        verify(importFileUtils).getReader(Mockito.eq(currentFile));
        verify(jsonFactory).createParser(Mockito.eq(fileReader));
        verify(jsonUtilService).nextNode(Mockito.eq(jsonParser));
        
        assertDownloadReaderState(currentFile, fileReader, jsonParser, importFiles.get(0));
        
        assertEquals(nextNode, judgment);
        
    }
    
    
    @Test
    public void read_FileReaderNotNull_JsonParserNotNull_NextNodeNull() throws Exception {
        
        // prepare
        
        File currentFile = new File("f1.json");
        File currentFile2Loop = new File("f2.json");
        Reader fileReader = Mockito.mock(Reader.class);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        List<File> importFiles = Lists.newArrayList(currentFile2Loop);
        
        resetDownloadReaderInternal(currentFile, fileReader, jsonParser, importFiles);
        
        
        Reader fileReader2Loop = Mockito.mock(Reader.class);
        JsonParser jsonParser2Loop = Mockito.mock(JsonParser.class);
        String node2Loop = "JUDGMENT_DATA";
        
        when(importFileUtils.getReader(Mockito.eq(currentFile2Loop))).thenReturn(fileReader2Loop);
        when(jsonFactory.createParser(Mockito.eq(fileReader2Loop))).thenReturn(jsonParser2Loop);
        when(jsonUtilService.nextNode(Mockito.eq(jsonParser))).thenReturn(null);
        when(jsonUtilService.nextNode(Mockito.eq(jsonParser2Loop))).thenReturn(node2Loop);
        
        
        
        // execute
        
        String judgment = jsonImportDownloadReader.read();
        
        
        // assert
        
        verify(importFileUtils, Mockito.never()).getReader(Mockito.eq(currentFile));
        verify(importFileUtils).getReader(Mockito.eq(currentFile2Loop));
        
        verify(jsonFactory, Mockito.never()).createParser(Mockito.eq(fileReader));
        verify(jsonFactory).createParser(Mockito.eq(fileReader2Loop));
        
        verify(jsonUtilService).nextNode(Mockito.eq(jsonParser));
        verify(jsonUtilService).nextNode(Mockito.eq(jsonParser2Loop));
        
        assertDownloadReaderState(currentFile2Loop, fileReader2Loop, jsonParser2Loop);
        
        assertEquals(node2Loop, judgment);
        
        
        
    }



    



    //------------------------ PRIVATE --------------------------
    
    
    private void resetDownloadReaderState(File currentFile, Reader reader, JsonParser jsonParser) {
        
        resetDownloadReaderInternal(currentFile, reader, jsonParser, Lists.newArrayList());
    }
    
    
    
    private void assertDownloadReaderState(File currentFile, Reader fileReader, JsonParser jsonParser, File ... importFiles) {
        
        assertEquals(jsonParser, Whitebox.<JsonParser> getInternalState(jsonImportDownloadReader, "jsonParser"));
        
        assertEquals(fileReader, Whitebox.<Reader> getInternalState(jsonImportDownloadReader, "fileReader"));
        
        assertEquals(currentFile, Whitebox.<Reader> getInternalState(jsonImportDownloadReader, "currentFile"));
        
        List<File> internalImportFiles = Whitebox.<List<File>> getInternalState(jsonImportDownloadReader, "importFiles");
        
        assertThat(internalImportFiles, Matchers.containsInAnyOrder(importFiles));
    
    }
   
 
    
    private void resetDownloadReaderInternal(File currentFile, Reader reader, JsonParser jsonParser, List<File> importFiles) {
        
        Whitebox.setInternalState(jsonImportDownloadReader, "currentFile", currentFile);
        
        Whitebox.setInternalState(jsonImportDownloadReader, "fileReader", reader);
        
        Whitebox.setInternalState(jsonImportDownloadReader, "jsonParser", jsonParser);
        
        Whitebox.setInternalState(jsonImportDownloadReader, "importFiles", new LinkedList<File>(importFiles));
        
        
    }
    
}
