package com.simple.original.server.service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.simple.domain.CellPhoneProvider;
import com.simple.domain.CellPhoneProviders;

/**
 * Fetches the cellphone providers list from the XML
 */
public class CellPhoneProvidersXmlReader {
	
	private static final Logger logger = Logger.getLogger(CellPhoneProvidersXmlReader.class.getName());

	//Properties properties = System.getProperties();

	@Inject
	@Named("com.simple.app.phone.config.file")
	private static String xmlConfigFile;
	
	private CellPhoneProvidersXmlReader(){
		getCellPhoneProviders();
	}
	
	private List<CellPhoneProvider> providerList = null;

	
	public List<CellPhoneProvider> getProviderList() {
		return providerList;
	}

	public void setProviderList(List<CellPhoneProvider> providerList) {
		this.providerList = providerList;
	}
	
	private void getCellPhoneProviders() {
		try {
			String cellMasterFilePath = xmlConfigFile;
			logger.info("file path is "+cellMasterFilePath);
			File file = new File(cellMasterFilePath);
			logger.fine("is file readable---->"+file.canRead());
			logger.fine("file exists?"+file.exists());
			logger.fine("file can be read"+file.getPath());
			InputStream inputStream = (CellPhoneProvidersXmlReader.class).getResourceAsStream(cellMasterFilePath);
			logger.fine("input stream is "+inputStream.toString());
			JAXBContext context = JAXBContext.newInstance(CellPhoneProviders.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			CellPhoneProviders cellPhoneProviders = (CellPhoneProviders) unmarshaller.unmarshal(inputStream);
			setProviderList(cellPhoneProviders.getCellPhoneProviders());
			logger.info("cell phone provers list size is : "+getProviderList().size());
		} catch (JAXBException e1) {
			logger.severe(" Exception occured while reading cellPhoneProviders.xml -> "+ e1.toString());
		}
	}
	

	/**
	 * Fetches the cellphone providers list from the XML
	 */
	public List<String> getCellPhoneProviderNames() {
		List<String> providers = new ArrayList<String>();
		
		for(CellPhoneProvider provider : getProviderList()){
			providers.add(provider.getName());
		}
			
		return providers;
	}


	/**
     * Fetches  cellPhoneProvider based on cellPhoneProvider Name
     */
	public CellPhoneProvider findProviderByProviderName(String cellProvider)
	{
		CellPhoneProvider cellPhoneProvider = null;
		Iterator<CellPhoneProvider> it = getProviderList().iterator();
		
		while(it.hasNext())
		{
			CellPhoneProvider provider = it.next();
			if(provider.getName().equalsIgnoreCase(cellProvider))
			{
				cellPhoneProvider = provider;
			}
		}
		return cellPhoneProvider;
	}
}
