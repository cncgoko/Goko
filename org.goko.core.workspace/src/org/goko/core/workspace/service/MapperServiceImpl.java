/**
 * 
 */
package org.goko.core.workspace.service;

import java.util.HashMap;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 * @date 10 févr. 2016
 */
public class MapperServiceImpl implements IMapperService {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(MapperServiceImpl.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.core.workspace.service.MapperServiceImpl";
	private Map<Class<?>, ILoader<?, ?>> loaders;
	private Map<Class<?>, IExporter<?, ?>> exporters;
	
	/**
	 * Constructor  
	 */
	public MapperServiceImpl() {
		loaders = new HashMap<Class<?>, ILoader<?, ?>>();
		exporters = new HashMap<Class<?>, IExporter<?, ?>>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting "+getServiceId());
		
		LOG.info("Succesfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		LOG.info("Stopping "+getServiceId());
		
		LOG.info("Succesfully stopped "+getServiceId());
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperService#addLoader(org.goko.core.workspace.service.ILoader)
	 */
	@Override
	public void addLoader(ILoader<?, ?> loader) throws GkException {
		if(loaders.containsKey(loader.getInputClass())){
			throw new GkTechnicalException("A loder for input class ["+loader.getInputClass()+"] already exist (registered is ["+loaders.get(loader.getInputClass()).getClass()+"]).");
		}
		loaders.put(loader.getInputClass(), loader);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperService#addExporter(org.goko.core.workspace.service.IExporter)
	 */
	@Override
	public void addExporter(IExporter<?, ?> exporter) throws GkException {
		if(exporters.containsKey(exporter.getInputClass())){
			throw new GkTechnicalException("An exporter for input class ["+exporter.getInputClass()+"] already exist (registered is ["+exporters.get(exporter.getInputClass()).getClass()+"]).");
		}
		exporters.put(exporter.getInputClass(), exporter);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperService#load(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <O> O load(Object object, Class<O> outputClass) throws GkException {		
		return getLoader(object.getClass(), outputClass).load(object, this);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperService#export(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <O> O export(Object object, Class<O> outputClass) throws GkException {
		return getExporter(object.getClass(), outputClass).export(object, this);
	}

	private <O> ILoader<Object, O> getLoader(Class inputClass, Class<O> outputClass) throws GkException{
		if(loaders.containsKey(inputClass)){
			ILoader<?, ?> loader = loaders.get(inputClass);
			if(outputClass.isAssignableFrom(loader.getOutputClass())){
				return (ILoader<Object, O>) loader;
			}
			throw new GkTechnicalException("No loader found for input class ["+inputClass+"] with matching output class ["+outputClass+"]");
		}
		throw new GkTechnicalException("No loader found for input class ["+inputClass+"]");
	}
	
	private <O> IExporter<Object, O> getExporter(Class inputClass, Class<O> outputClass) throws GkException{
		if(exporters.containsKey(inputClass)){
			IExporter<?, ?> exporter = exporters.get(inputClass);
			if(outputClass.isAssignableFrom(exporter.getOutputClass())){
				return (IExporter<Object, O>) exporter;
			}
			throw new GkTechnicalException("No exporter found for input class ["+inputClass+"] with matching output class ["+outputClass+"]");
		}
		throw new GkTechnicalException("No exporter found for input class ["+inputClass+"]");
	}
	
//	public static void main(String[] args) throws Exception {
//		MapperServiceImpl mapper = new MapperServiceImpl();
//		
//		ILoader<String, Integer> loader = new ILoader<String, Integer>() {
//			
//			@Override
//			public Integer load(String input, IMapperService mapperService) throws Exception {				
//				return Integer.valueOf(input);
//			}
//			
//			@Override
//			public Class<Integer> getOutputClass() {				
//				return Integer.class;
//			}
//			
//			@Override
//			public Class<String> getInputClass() {
//				return String.class;
//			}
//		};
//		
//		ILoader<String, Number> loaderNumber = new ILoader<String, Number>() {
//			
//			@Override
//			public Number load(String input, IMapperService mapperService) throws Exception {				
//				return Integer.valueOf(input);
//			}
//			
//			@Override
//			public Class<Number> getOutputClass() {				
//				return Number.class;
//			}
//			
//			@Override
//			public Class<String> getInputClass() {
//				return String.class;
//			}
//		};
//		
//		mapper.addLoader(loader);
//		//mapper.addLoader(loaderNumber);
//
//		Number n = mapper.load("13", Number.class);
//		Integer i = mapper.load("13", Integer.class);
//		System.out.println("");
//	}
	
}
