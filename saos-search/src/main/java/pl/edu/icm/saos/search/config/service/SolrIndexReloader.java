package pl.edu.icm.saos.search.config.service;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;
import pl.edu.icm.saos.search.config.model.SolrConfigurationException;

@Service
public class SolrIndexReloader {
    
    private static Logger log = LoggerFactory.getLogger(SolrIndexReloader.class);
    
    @Autowired
    @Qualifier("solrServer")
    SolrServer solrServer;


    public void reloadIndex(IndexConfiguration indexConfiguration) {

        boolean indexExists = checkIndexExists(indexConfiguration);

        if (indexExists) {
            reloadIndex(indexConfiguration.getName());
        } else {
            createIndex(indexConfiguration);
        }
    }
    
    protected boolean checkIndexExists(IndexConfiguration indexConfiguration) {
        CoreAdminResponse cores = null;
        
        try {
            CoreAdminRequest request = new CoreAdminRequest();
            request.setAction(CoreAdminAction.STATUS);
            cores = request.process(solrServer);
        } catch (SolrServerException | IOException e) {
            throw new SolrConfigurationException("Unable to check solr indexes status", e);
        }
        
        NamedList<Object> indexStatus = cores.getCoreStatus(indexConfiguration.getName());
        
        return indexStatus != null;
    }
    
    protected void reloadIndex(String indexName) {
        log.info("Reloading solr index with name {}", indexName);
        try {
            CoreAdminRequest.reloadCore(indexName, solrServer);
        } catch (SolrServerException | IOException e) {
            throw new SolrConfigurationException("Unable to reload index with name " + indexName, e);
        }
    }
    
    protected void createIndex(IndexConfiguration indexConfiguration) {
        log.info("Creating solr index with name {}", indexConfiguration.getName());
        try {
            CoreAdminRequest.createCore(indexConfiguration.getName(), indexConfiguration.getInstanceDir(), solrServer);
        } catch (SolrServerException | IOException e) {
            throw new SolrConfigurationException("Unable to create index with name " + indexConfiguration.getName(), e);
        }
    }
}