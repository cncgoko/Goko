package org.goko.core.gcode.rs274ngcv3;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.common.utils.CacheByCode;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.SequentialIdGenerator;
import org.goko.core.common.utils.UniqueCacheByCode;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.gcode.element.IGCodeProviderSourceListener;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionIterator;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.element.StackableGCodeProviderModifier;
import org.goko.core.gcode.rs274ngcv3.element.StackableGCodeProviderRoot;
import org.goko.core.gcode.rs274ngcv3.element.source.StringGCodeSource;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.InstructionFactory;
import org.goko.core.gcode.rs274ngcv3.modifier.AbstractModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener;
import org.goko.core.gcode.rs274ngcv3.modifier.ModifierSorter;
import org.goko.core.gcode.rs274ngcv3.modifier.ModifierSorter.EnumModifierSortType;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeLexer;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeTokenType;
import org.goko.core.gcode.rs274ngcv3.parser.ModalGroup;
import org.goko.core.gcode.service.GCodeProviderDeleteEvent;
import org.goko.core.gcode.service.IGCodeProviderDeleteVetoableListener;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.log.GkLog;
import org.goko.core.math.BoundingTuple6b;
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 */
public class RS274NGCServiceImpl extends AbstractGokoService implements IRS274NGCService{
	private static final GkLog LOG = GkLog.getLogger(RS274NGCServiceImpl.class);
	/** The list of listener */
	private List<IGCodeProviderRepositoryListener> listenerList;
	/** The list of modifier listener */
	private List<IModifierListener> modifierListenerList;
	/** The list of modifier listener */
	private List<IGCodeProviderDeleteVetoableListener> gcodeProviderDeleteListenerList;
	/** The list of modal groups */
	private List<ModalGroup> modalGroups;
	/** The cache of root providers */
	private CacheById<StackableGCodeProviderRoot> cacheRootProviders;
	/** The cache of providers */
	private CacheById<IStackableGCodeProvider> cacheStackedProviders;
	/** The cache of providers */
	private CacheByCode<IStackableGCodeProvider> cacheStackedProvidersByCode;
	/** The cache of modifiers */
	private CacheById<IModifier<GCodeProvider>> cacheModifiers;
	/** Boolean allowing to enable/disable gcode update notification */
	private boolean gcodeProviderUdateNotificationEnabled;
	/** The active rendering format */
	private RenderingFormat renderingFormat;
	
	/** Constructor */
	public RS274NGCServiceImpl() {
		initializeModalGroups();
		this.listenerList 	= new CopyOnWriteArrayList<IGCodeProviderRepositoryListener>();
		this.modifierListenerList = new CopyOnWriteArrayList<IModifierListener>();
		this.cacheRootProviders = new CacheById<StackableGCodeProviderRoot>(new SequentialIdGenerator());
		this.cacheStackedProviders = new CacheById<IStackableGCodeProvider>();
		this.cacheStackedProvidersByCode = new UniqueCacheByCode<IStackableGCodeProvider>();
		this.cacheModifiers = new CacheById<IModifier<GCodeProvider>>(new SequentialIdGenerator());
		this.gcodeProviderUdateNotificationEnabled = true;
		this.gcodeProviderDeleteListenerList = new CopyOnWriteArrayList<IGCodeProviderDeleteVetoableListener>();
		this.renderingFormat = RenderingFormat.COMPLETE;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return "org.goko.core.gcode.rs274ngcv3.RS274NGCServiceImpl";
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void startService() throws GkException {
		LOG.info("Starting "+getServiceId());
		
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#reload(java.lang.Integer)
	 */
	@Override	
	public void reload(Integer idGCodeProvider, IProgressMonitor monitor)throws GkException{
		StackableGCodeProviderRoot root = cacheRootProviders.get(idGCodeProvider);
		// Create the new target provider
		GCodeProvider provider = new GCodeProvider();		
		provider.setId(root.getId());
		provider.setSource(root.getSource());
		provider.setCode(root.getCode());
		
		parseProvider(provider.getSource(), provider, monitor);
		root.setParent(provider);
		root.setModificationDate(new Date());
		// Force to update the modifiers stack
		cacheStackedProviders.get(idGCodeProvider).update();
		notifyGCodeProviderUpdate(getGCodeProvider(idGCodeProvider));
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#parse(java.io.InputStream)
	 */
	@Override
	public IGCodeProvider parse(IGCodeProviderSource source, IProgressMonitor monitor) throws GkException {
		GCodeProvider provider = new GCodeProvider();
		parseProvider(source, provider, monitor);
		return provider;
	}

	public void parseProvider(IGCodeProviderSource source, GCodeProvider provider, IProgressMonitor monitor) throws GkException {		
		provider.clear();
		provider.setSource(source);		
		GCodeLexer lexer = new GCodeLexer();
		InputStream stream = source.openInputStream();
		List<List<GCodeToken>> tokens = lexer.tokenize(stream, provider);
		
		try {
			stream.close();
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
		if(!provider.hasErrors()){
			SubMonitor subMonitor = null;
			if(monitor != null){
				subMonitor = SubMonitor.convert(monitor,"Reading file", tokens.size());
			}
			
			for (List<GCodeToken> lstToken : tokens) {
				verifyModality(lstToken);
				GCodeLine line = buildLine(lstToken);
				if(line != null){
					provider.addLine(line);
				}
				if(subMonitor != null){
					subMonitor.worked(1);
				}
			}
			if(subMonitor != null){
				subMonitor.done();
			}
	
			if(monitor != null){
				subMonitor = SubMonitor.convert(monitor,"Veryfying file", 1);
			}
			getInstructions(new GCodeContext(), provider);
			if(subMonitor != null){
				subMonitor.done();
			}
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#parse(java.lang.String)
	 */
	@Override
	public IGCodeProvider parse(String inputString) throws GkException {
		return parse(new StringGCodeSource(inputString), null);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#parseLine(java.lang.String)
	 */
	@Override
	public GCodeLine parseLine(String inputString) throws GkException {
		IGCodeProvider provider = parse(new StringGCodeSource(inputString), null);
		return provider.getLines().get(0);
	}

	/**
	 * Build a GCodeLine using a list of tokens
	 * @param lstToken the list of tokens to use
	 * @return a GCodeLine
	 * @throws GkException GkException
	 */
	private GCodeLine buildLine(List<GCodeToken> lstToken) throws GkException {
		
		GCodeLine line = new GCodeLine();		
		
		if(CollectionUtils.isNotEmpty(lstToken)){
			

			for (GCodeToken token : lstToken) {
	//			if(token.getType() == GCodeTokenType.LINE_NUMBER){
	//				line.setLineNumber(GCodeTokenUtils.getLineNumber(token));
	//			}else
				if(token.getType() == GCodeTokenType.WORD || token.getType() == GCodeTokenType.LINE_NUMBER){
					line.addWord(new GCodeWord(StringUtils.substring(token.getValue(), 0, 1), StringUtils.substring(token.getValue(), 1)));
				}else if(token.getType() == GCodeTokenType.SIMPLE_COMMENT
						|| token.getType() == GCodeTokenType.MULTILINE_COMMENT){
					line.addWord(new GCodeWord(";", token.getValue()));
				}else if(token.getType() == GCodeTokenType.PERCENT){
					line.addWord(new GCodeWord("%", StringUtils.EMPTY));
				}
			}
		}
		return line;
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IGCodeService#getInstructions(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public InstructionProvider getInstructions(final GCodeContext context, IGCodeProvider gcodeProvider) throws GkException {
		GCodeContext localContext = null;
		if(context != null){
			localContext = new GCodeContext(context);
		}

		InstructionFactory factory = new InstructionFactory();
		InstructionProvider instructionProvider = new InstructionProvider();

		for (GCodeLine gCodeLine : gcodeProvider.getLines()) {
			InstructionSet iSet = new InstructionSet();
			List<GCodeWord> localWords = new ArrayList<GCodeWord>(gCodeLine.getWords());
			// A line can contain multiple instructions
			while(CollectionUtils.isNotEmpty(localWords)){
				int wordCountBefore = localWords.size();
				AbstractInstruction instruction = factory.build(localContext, localWords);
				if(instruction == null){
					// We have words is the list, but we can't build any instruction from them. End while loop
					traceUnusedWords(gcodeProvider, localWords);
					break;
				}else{
					// Make sure we consumed at least one word
					if(localWords.size() == wordCountBefore){
						throw new GkTechnicalException("An instruction was created but no word was removed for provider ["+gcodeProvider.getCode()+"]. Instruction created : "+instruction.getClass());
					}
				}

				instruction.setIdGCodeLine(gCodeLine.getId());
				iSet.addInstruction(instruction);
				// Update context for further instructions
				update(localContext, instruction);
			}
			if(CollectionUtils.isNotEmpty(iSet.getInstructions())){
				instructionProvider.addInstructionSet(iSet);
			}
		}

		return instructionProvider;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#getGCodeProvider(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.InstructionProvider)
	 */
	@Override
	public GCodeProvider getGCodeProvider(GCodeContext context, InstructionProvider instructionProvider) throws GkException {
		InstructionFactory factory = new InstructionFactory();
		GCodeProvider provider = new GCodeProvider();

		List<InstructionSet> sets = instructionProvider.getInstructionSets();
		for (InstructionSet instructionSet : sets) {
			GCodeLine line = factory.getLine(context, instructionSet, renderingFormat);

			provider.addLine(line);
		}
		return provider;
	}

	/**
	 * Trace the unused words in a line
	 * @param unusedWords the list of unused words
	 */
	private void traceUnusedWords( IGCodeProvider gcodeProvider, List<GCodeWord> unusedWords){
		String wordstr = "";
		for (GCodeWord gCodeWord : unusedWords) {
			wordstr += gCodeWord.completeString() + " ";
		}
		LOG.warn("Provider ["+gcodeProvider.getCode()+"] - GCodeWord not supported "+wordstr+". They will be present in the GCode file, but won't generate instruction");
	}

	/**
	 * Verify modality for the given list of token
	 * @param lstToken the list of tokens to check
	 * @throws GkException GkException if there is a modality violation
	 */
	private void verifyModality(List<GCodeToken> lstToken) throws GkException{
		for (ModalGroup group : modalGroups) {
			group.verifyModality(lstToken);
		}
	}

	/**
	 * Initialize the list of modal groups
	 */
	protected void initializeModalGroups(){
		this.modalGroups = new ArrayList<ModalGroup>();
		this.modalGroups.add( new ModalGroup("G0", "G00", "G1", "G01", "G2", "G02", "G3", "G03", "G38.2", "G80", "G81", "G82", "G83", "G84", "G85", "G86", "G87", "G88", "G89" ));

		this.modalGroups.add( new ModalGroup("G17", "G18", "G19"  ));
		this.modalGroups.add( new ModalGroup("G90", "G91"));
		this.modalGroups.add( new ModalGroup("G93", "G94"));
		this.modalGroups.add( new ModalGroup("G20", "G21"));
		this.modalGroups.add( new ModalGroup("G40", "G41", "G42"));
		this.modalGroups.add( new ModalGroup("G43","G49"));
		this.modalGroups.add( new ModalGroup("G98","G99"));
		this.modalGroups.add( new ModalGroup("G54", "G55", "G56", "G57", "G58", "G59", "G59.1", "G59.2", "G59.3"));
		this.modalGroups.add( new ModalGroup("G61", "G61.1", "G64"));

		this.modalGroups.add( new ModalGroup("M0", "M1", "M2", "M30", "M60"));
		this.modalGroups.add( new ModalGroup("M6"));
		this.modalGroups.add( new ModalGroup("M3", "M03","M4", "M04", "M5", "M05"));
		this.modalGroups.add( new ModalGroup("M7", "M07", "M9", "M09"));
		this.modalGroups.add( new ModalGroup("M8", "M08", "M9", "M09"));
		this.modalGroups.add( new ModalGroup("M48", "M49"));
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#update(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public GCodeContext update(GCodeContext baseContext, AbstractInstruction instruction) throws GkException {
		instruction.apply(baseContext);
		return baseContext;
	};


	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#update(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstructionSet)
	 */
	@Override
	public GCodeContext update(GCodeContext baseContext, InstructionSet instructionSet) throws GkException {
		GCodeContext result = baseContext;
		List<AbstractInstruction> instructions = instructionSet.getInstructions();
		if(CollectionUtils.isNotEmpty(instructions)){
			for (AbstractInstruction instruction : instructions) {
				result = update(result, instruction);
			}
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#update(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstructionSet)
	 */
	@Override
	public GCodeContext update(GCodeContext baseContext, IInstructionProvider<AbstractInstruction, InstructionSet> instructionProvider) throws GkException {
		GCodeContext result = baseContext;
		List<InstructionSet> instructionSets = instructionProvider.getInstructionSets();
		if(CollectionUtils.isNotEmpty(instructionSets)){
			for (InstructionSet instructionSet : instructionSets) {
				result = update(result, instructionSet);
			}
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#getIterator(org.goko.core.gcode.element.IInstructionProvider, org.goko.core.gcode.element.IGCodeContext)
	 */
	@Override
	public InstructionIterator getIterator(IInstructionProvider<AbstractInstruction, InstructionSet> instructionProvider, GCodeContext baseContext) throws GkException {
		return new InstructionIterator(instructionProvider, new GCodeContext(baseContext), this);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#render(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	public String render(GCodeLine line) throws GkException {
		return render(line, renderingFormat);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#render(org.goko.core.gcode.element.GCodeLine, org.goko.core.gcode.rs274ngcv3.RenderingFormat)
	 */
	@Override
	public String render(GCodeLine line, RenderingFormat customRenderingFormat) throws GkException {
		RenderingFormat localFormat = customRenderingFormat;
		if(localFormat == null){
			localFormat = renderingFormat;
		}
		StringBuffer buffer = new StringBuffer();
		// FIXME find a better way to classify GCode words or describe a GCodeLine within the rs274 service
		GCodeWord commentWord = null;

		// Add words
		for (GCodeWord word : line.getWords()) {
			if(StringUtils.equals(word.getLetter(), "N")){
				if(!localFormat.isSkipLineNumbers()){
					buffer.insert(0, word.getValue());
					buffer.insert(0, word.getLetter());
				}
				continue;
			}

			if(StringUtils.equals(word.getLetter(), ";")){
				if(!localFormat.isSkipComments()){
					commentWord = word;
				}
				continue;
			}

			if(buffer.length() > 0){
				buffer.append(" ");
			}
			buffer.append(word.getLetter());
			String value = word.getValue();
			// Dirty temporary hack for decimal truncation
			if("XYZABCIJKSF".contains(word.getLetter())){
				value = renderingFormat.format( new BigDecimal(value) );
			}
			buffer.append(value);
		}
		// Add comment
		if(commentWord != null){
			if(buffer.length() > 0){
				buffer.append(" ");
			}
			buffer.append(commentWord.getValue());
		}
	//	utiliser un post processeur pour afficher le gcode ou voir pourquoi les decimales ne sont pas tronquées
		return buffer.toString();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#getBounds(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.InstructionProvider)
	 */
	@Override
	public BoundingTuple6b getBounds(GCodeContext context, InstructionProvider instructionProvider) throws GkException {
		Tuple6b min = new Tuple6b();
		Tuple6b max = new Tuple6b();

		GCodeContext preContext = new GCodeContext(context);
		InstructionIterator iterator = getIterator(instructionProvider, preContext);

		while (iterator.hasNext()) {
			preContext = new GCodeContext(iterator.getContext());
			AbstractInstruction instruction = iterator.next();

			Tuple6b endpoint = iterator.getContext().getPosition();//new Tuple6b(straightInstruction.getX(),straightInstruction.getY(),straightInstruction.getZ(),straightInstruction.getA(),straightInstruction.getB(),straightInstruction.getC());
			min = min.min(endpoint);
			max = max.max(endpoint);
//			FIXME : add bound support for Arcs
//			if(instruction.getType() == InstructionType.STRAIGHT_TRAVERSE
//			|| instruction.getType() == InstructionType.STRAIGHT_FEED){
//				//AbstractStraightInstruction straightInstruction = (AbstractStraightInstruction) instruction;
//				Tuple6b endpoint = iterator.getContext().getPosition();//new Tuple6b(straightInstruction.getX(),straightInstruction.getY(),straightInstruction.getZ(),straightInstruction.getA(),straightInstruction.getB(),straightInstruction.getC());
//				min = min.min(endpoint);
//				max = max.max(endpoint);
//			}else if(instruction.getType() == InstructionType.ARC_FEED){
//				ArcFeedInstruction arcfeedInstruction = (ArcFeedInstruction) instruction;
//				
//			}
		}

		return new BoundingTuple6b(min, max);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#getGCodeProvider(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider getGCodeProvider(final Integer id) throws GkException {	
		// Make sure the required id exists
		internalGetGCodeProvider(id);		
		return new RS274GCodeReference(this, id);
	}
	

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryFriend#internalGetGCodeProvider(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider internalGetGCodeProvider(Integer id) throws GkException {
		return cacheStackedProviders.get(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#findGCodeProvider(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider findGCodeProvider(Integer id) throws GkException {		
		return cacheStackedProviders.find(id);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#getGCodeProvider(java.lang.String)
	 */
	@Override
	public IGCodeProvider getGCodeProvider(String code) throws GkException {		
		return cacheStackedProvidersByCode.get(code);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#getGCodeProvider()
	 */
	@Override
	public List<IGCodeProvider> getGCodeProvider() throws GkException {
		List<IGCodeProvider> result = new ArrayList<IGCodeProvider>();
		List<IStackableGCodeProvider> lstProviders = cacheStackedProviders.get();
		for (IGCodeProvider provider : lstProviders) {
			result.add(getGCodeProvider(provider.getId()));
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#addGCodeProvider(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void addGCodeProvider(IGCodeProvider provider) throws GkException {
		LOG.info("Adding GCode provider code=["+provider.getCode()+"], id=["+provider.getId()+"]");
		StackableGCodeProviderRoot wrappedProvider = new StackableGCodeProviderRoot(provider);		
		cacheRootProviders.add(wrappedProvider);
		cacheStackedProviders.add(wrappedProvider);
		cacheStackedProvidersByCode.add(wrappedProvider);
		provider.setId(wrappedProvider.getId());	
		provider.getSource().bind();
		provider.getSource().addListener(new GCodeSourceWatcher(provider.getId()));
		notifyGCodeProviderCreate(getGCodeProvider(wrappedProvider.getId()));
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#deleteGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void deleteGCodeProvider(Integer id) throws GkException {
		IGCodeProvider provider = cacheStackedProviders.get(id);
		boolean canDelete = checkDeleteGCodeProvider(id);
		if(canDelete){
			LOG.info("Deleting GCode provider code=["+provider.getCode()+"], id=["+provider.getId()+"]");
			IGCodeProvider notificationProvider = getGCodeProvider(provider.getId());
			// Notifies before listeners first
			notifyBeforeGCodeProviderDelete(notificationProvider);
			// Remove attached modifiers 
			performDeleteByIdGCodeProvider(id);
			// Update the provider once it's modified
			provider = cacheStackedProviders.get(id);
			cacheStackedProviders.remove(id);
			cacheStackedProvidersByCode.remove(provider.getCode()); 
			provider.getSource().delete();
			
			// Notifies after listeners
			notifyAfterGCodeProviderDelete(notificationProvider);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#lockGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void lockGCodeProvider(Integer idGcodeProvider) throws GkException {
		IGCodeProvider provider = cacheStackedProviders.get(idGcodeProvider);
		if(!provider.isLocked()){
			provider.setLocked(true);
			notifyGCodeProviderLocked(getGCodeProvider(provider.getId()));
		}
	}

	/**
	 * Assert on the provider for the locked state
	 * @param idGcodeProvider id of the provider to test
	 * @throws GkException GkException
	 */
	void assertGCodeProviderUnlocked(Integer idGcodeProvider) throws GkException {
		if(isGCodeProviderUnlocked(idGcodeProvider)){
			throw new GkFunctionalException("GCO-150");
		}
	}

	/**
	 * Determines if this provider is locked
	 * @param idGcodeProvider id of the provider to test
	 * @return <code>true</code> if it's locked, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	boolean isGCodeProviderUnlocked(Integer idGcodeProvider) throws GkException {
		return cacheStackedProviders.get(idGcodeProvider).isLocked();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#unlockGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void unlockGCodeProvider(Integer idGcodeProvider) throws GkException {
		IGCodeProvider provider = cacheStackedProviders.get(idGcodeProvider);
		if(provider.isLocked()){
			provider.setLocked(false);
			notifyGCodeProviderUnlocked(getGCodeProvider(provider.getId()));
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#addModifier(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public void addModifier(IModifier<GCodeProvider> modifier) throws GkException {
		assertGCodeProviderUnlocked(modifier.getIdGCodeProvider());

		// Assign the order of the modifier on the target GCodeProvider
		List<IModifier<GCodeProvider>> lstModifier = getModifierByGCodeProvider(modifier.getIdGCodeProvider());
		modifier.setOrder(lstModifier.size());
		this.cacheModifiers.add(modifier);
		if(modifier instanceof AbstractModifier){
			// FIXME: found how to remove this dirty hack
			((AbstractModifier) modifier).setRS274NGCService(this);
		}
		IStackableGCodeProvider baseProvider = this.cacheStackedProviders.get(modifier.getIdGCodeProvider());
		StackableGCodeProviderModifier wrappedProvider = new StackableGCodeProviderModifier(baseProvider, modifier);
		
		this.cacheStackedProviders.remove(baseProvider.getId());
		this.cacheStackedProviders.add(wrappedProvider);
		
		wrappedProvider.update();
		
		notifyModifierCreate(modifier);
		notifyGCodeProviderUpdate(getGCodeProvider(wrappedProvider.getId()));		
	}
	
	@Override
	public void setModifierOrder(IModifier<GCodeProvider> modifier, int order) throws GkException {
		assertGCodeProviderUnlocked(modifier.getIdGCodeProvider());
		
		IStackableGCodeProvider stackableModifier = unchainModifier(modifier);
		
		// Let's find the current modifier at the given order
		IStackableGCodeProvider target = findStackableProviderForModifier(modifier.getIdGCodeProvider(), order);
		boolean chainBefore = true;
		if(target == null){
			target = findStackableProviderForModifier(modifier.getIdGCodeProvider(), order - 1);
			chainBefore = false;
		}
						
		if(chainBefore){
			chainModifierBefore(stackableModifier, target);
		}else{			
			chainModifierAfter(stackableModifier, target);
		}
		updateModifier(modifier);
		updateModifiersOrder(modifier.getIdGCodeProvider());
		notifyGCodeProviderUpdate(getGCodeProvider(modifier.getIdGCodeProvider()));
		notifyModifierUpdate(modifier);
	}
	
	private void updateModifiersOrder(Integer idGCodeProvider) throws GkException{	
		assertGCodeProviderUnlocked(idGCodeProvider);		
		IStackableGCodeProvider stackedProvider = cacheStackedProviders.get(idGCodeProvider);
		if(stackedProvider.getIdModifier() != null){
			recursiveOrderUpdate( getModifier( stackedProvider.getIdModifier() ) );
		}
	}
	
	private int recursiveOrderUpdate(IModifier<GCodeProvider> modifier) throws GkException{	
		IStackableGCodeProvider target = findStackableProviderForModifier(modifier.getId());
		if(target.getParent() == null || target.getParent().getIdModifier() == null){
			modifier.setOrder(0);
			return 0;
		}
		
		IModifier<GCodeProvider> parentModifier = getModifier( target.getParent().getIdModifier() );
		int parentOrder = recursiveOrderUpdate(parentModifier);
		modifier.setOrder(parentOrder + 1);
		return modifier.getOrder();		
	}
	
	private void chainModifierAfter(IStackableGCodeProvider stackedModifier, IStackableGCodeProvider afterModifier) throws GkException{		
		IStackableGCodeProvider child = afterModifier.getChild();
		afterModifier.setChild(stackedModifier);
		stackedModifier.setChild(child);
		stackedModifier.setParent(afterModifier);
		if(child != null){
			child.setParent(stackedModifier);
		}else{
			// There is no child. It means this was the latest modifier that was used as reference in the cacheStackedProviders			
			cacheStackedProviders.remove(afterModifier);
			cacheStackedProviders.add(stackedModifier);	
		}
	}
	
	private void chainModifierBefore(IStackableGCodeProvider modifier, IStackableGCodeProvider beforeModifier) throws GkException{		
		IStackableGCodeProvider parent = beforeModifier.getParent();
		modifier.setChild(beforeModifier);
		beforeModifier.setParent(modifier);
		modifier.setParent(parent);
		if(parent != null){
			parent.setChild(modifier);
		}
	}
	
	private IStackableGCodeProvider unchainModifier(IModifier<GCodeProvider> modifier) throws GkException{
		assertGCodeProviderUnlocked(modifier.getIdGCodeProvider());
		IStackableGCodeProvider stackedModifier = findStackableProviderForModifier(modifier.getId());
		
		IStackableGCodeProvider parent = stackedModifier.getParent();
		IStackableGCodeProvider child = stackedModifier.getChild();
		if(parent != null){
			parent.setChild(child);
		}
		if(child != null){
			child.setParent(parent);
		}else{
			// There is no child. It means this was the latest modifier that was used as reference in the cacheStackedProviders
			IGCodeProvider provider = getGCodeProvider(modifier.getIdGCodeProvider());
			cacheStackedProviders.remove(provider.getId());			
			cacheStackedProviders.add(parent);		
		}
		stackedModifier.setParent(null);
		stackedModifier.setChild(null);
		updateModifiersOrder(modifier.getIdGCodeProvider());
		return stackedModifier;
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#updateModifier(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public void updateModifier(IModifier<GCodeProvider> modifier) throws GkException {
		assertGCodeProviderUnlocked(modifier.getIdGCodeProvider());
		// Make sure the modifier exists
		getModifier(modifier.getId());
		this.cacheModifiers.remove(modifier.getId());
		modifier.setModificationDate(new Date());
		this.cacheModifiers.add(modifier);
		IStackableGCodeProvider provider = this.cacheStackedProviders.get(modifier.getIdGCodeProvider());
		provider.update();
		notifyModifierUpdate(modifier);
		notifyGCodeProviderUpdate(getGCodeProvider(provider.getId()));		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#deleteModifier(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public void deleteModifier(IModifier<GCodeProvider> modifier) throws GkException {
		deleteModifier(modifier.getId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#deleteModifier(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public void deleteModifier(Integer idModifier) throws GkException {
		IModifier<GCodeProvider> modifier = this.cacheModifiers.get(idModifier);
		assertGCodeProviderUnlocked(modifier.getIdGCodeProvider());
		
		// Remove the link of the modifier in the chain		
		IStackableGCodeProvider stackableModifier = findStackableProviderForModifier(idModifier);
		IStackableGCodeProvider parent = stackableModifier.getParent();
		IStackableGCodeProvider child = stackableModifier.getChild();
		unchainModifier(modifier);
		
		// Force a refresh from the parent modifier
		IModifier<GCodeProvider> forceRefreshModifier = null;
		if(parent != null && parent.getIdModifier() != null){
			forceRefreshModifier = getModifier(parent.getIdModifier());
			
		}else if(child != null){ // Or child modifier
			forceRefreshModifier = getModifier(child.getIdModifier());
		}
		if(forceRefreshModifier != null){
			forceRefreshModifier.setModificationDate(new Date());
		}
		// Definitely remove the modifier
		this.cacheModifiers.remove(idModifier);
		
		IStackableGCodeProvider targetProvider = cacheStackedProviders.get(modifier.getIdGCodeProvider());
		//Force update
		targetProvider.update();
		notifyModifierDelete(modifier);
		notifyGCodeProviderUpdate(getGCodeProvider(targetProvider.getId()));		
	}
	
	/**
	 * Returns the IStackableGCodeProvider for the given modifier
	 * @param idModifier id of the modifier
	 * @return IStackableGCodeProvider or <code>null</code> if none found
	 * @throws GkException GkException
	 */
	private IStackableGCodeProvider findStackableProviderForModifier(Integer idModifier) throws GkException{
		IModifier<GCodeProvider> modifier = getModifier(idModifier);		
		// Let's start at the bottom most modifier on the gcode provider
		IStackableGCodeProvider currentModifier = cacheStackedProviders.get(modifier.getIdGCodeProvider());
		
		while(currentModifier.getParent() != null){			
			if(ObjectUtils.equals(currentModifier.getIdModifier(), idModifier)){
				// The id of the parent match the modifier we want the child for. We have our match
				return currentModifier;				
			}else{
				// We have a parent, but the id don't match.
				currentModifier = currentModifier.getParent();
			}
		}		
		return null;
	}
	
	/**
	 * Returns the IStackableGCodeProvider for the given gcode provider and the given order
	 * @param idGCodeProvider id of the gcode provider
	 * 
	 * @return IStackableGCodeProvider or <code>null</code> if none found
	 * @throws GkException GkException
	 */
	private IStackableGCodeProvider findStackableProviderForModifier(Integer idGCodeProvider, int order) throws GkException{				
		// Let's start at the bottom most modifier on the gcode provider
		IStackableGCodeProvider stackedProvider = cacheStackedProviders.get(idGCodeProvider);
		
		while(stackedProvider.getParent() != null){			
			if(stackedProvider.getIdModifier() != null){
				IModifier<GCodeProvider> modifier = getModifier(stackedProvider.getIdModifier());
				if(modifier.getOrder() == order){
					return stackedProvider;
				}		
			}
			// We have a parent, but the id don't match.
			stackedProvider = stackedProvider.getParent();			
		}		
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#getModifier(java.lang.Integer)
	 */
	@Override
	public IModifier<GCodeProvider> getModifier(Integer id) throws GkException {
		return cacheModifiers.get(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#findModifier(java.lang.Integer)
	 */
	@Override
	public IModifier<GCodeProvider> findModifier(Integer id) throws GkException {		
		return cacheModifiers.find(id);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#getModifier(java.util.List)
	 */
	@Override
	public List<IModifier<GCodeProvider>> getModifier(List<Integer> lstId) throws GkException {
		return cacheModifiers.get(lstId);
	}

	@Override
	public List<IModifier<GCodeProvider>> getModifierByGCodeProvider(Integer idGcodeProvider) throws GkException {
		List<IModifier<GCodeProvider>> lstProviderModifiers = new ArrayList<IModifier<GCodeProvider>>();
		List<IModifier<GCodeProvider>> lstModifiers = cacheModifiers.get();

		if(CollectionUtils.isNotEmpty(lstModifiers)){
			for (IModifier<GCodeProvider> iModifier : lstModifiers) {
				if(ObjectUtils.equals(iModifier.getIdGCodeProvider(), idGcodeProvider)){
					lstProviderModifiers.add(iModifier);
				}
			}
		}
		Collections.sort(lstProviderModifiers, new ModifierSorter(EnumModifierSortType.ORDER));
		return lstProviderModifiers;
	}

//	private IGCodeProvider applyModifiers(GCodeProvider provider) throws GkException {
//		List<IModifier<GCodeProvider>> lstModifiers = getModifierByGCodeProvider(provider.getId());
//		GCodeProvider source = provider;
//		GCodeProvider target = null;
//		if(CollectionUtils.isNotEmpty(lstModifiers)){
//			for (IModifier<GCodeProvider> modifier : lstModifiers) {
//				if(modifier.isEnabled()){
//					target = new GCodeProvider();
//					target.setId(source.getId());
//					target.setCode(source.getCode());
//					modifier.apply(source, target);
//					source = target;
//				}
//			}
//		}
//		return source;
//	}

	protected void performDeleteByIdGCodeProvider(Integer id) throws GkException{
		List<IModifier<GCodeProvider>> lstModifiers = getModifierByGCodeProvider(id);
		if(CollectionUtils.isNotEmpty(lstModifiers)){
			disableGcodeProviderUdateNotification();
			for (IModifier<GCodeProvider> iModifier : lstModifiers) {
				deleteModifier(iModifier);
			}
			enableGcodeProviderUdateNotification();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#addListener(org.goko.core.gcode.service.IGCodeProviderRepositoryListener)
	 */
	@Override
	public void addListener(IGCodeProviderRepositoryListener listener) throws GkException {
		listenerList.add( listener );
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#addModifierListener(org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener)
	 */
	@Override
	public void addModifierListener(IModifierListener listener){
		modifierListenerList.add(listener);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#addDeleteVetoableListener(org.goko.core.gcode.service.IGCodeProviderDeleteVetoableListener)
	 */
	@Override
	public void addDeleteVetoableListener(IGCodeProviderDeleteVetoableListener listener) throws GkException {
		if(!gcodeProviderDeleteListenerList.contains(listener)){
			gcodeProviderDeleteListenerList.add(listener);
		}		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#removeDeleteVetoableListener(org.goko.core.gcode.service.IGCodeProviderDeleteVetoableListener)
	 */
	@Override
	public void removeDeleteVetoableListener(IGCodeProviderDeleteVetoableListener listener) throws GkException {
		if(gcodeProviderDeleteListenerList.contains(listener)){
			gcodeProviderDeleteListenerList.remove(listener);
		}
	}
	/**
	 * Calls every delete listener to make sure the GCodeProvider can be deleted
	 * @param idGCodeProvider the id of the provider to delete
	 * @return <code>true</code> if it can be deleted, <code>false</code> otherwise
	 */
	protected boolean checkDeleteGCodeProvider(Integer idGCodeProvider){
		GCodeProviderDeleteEvent event = new GCodeProviderDeleteEvent(idGCodeProvider);
		for (IGCodeProviderDeleteVetoableListener deleteListener : gcodeProviderDeleteListenerList) {
			deleteListener.beforeDelete(event);
			if(!event.isDoIt()){
				break;
			}
		}
		return event.isDoIt();
	}
	
	 /** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#removeListener(org.goko.core.gcode.service.IGCodeProviderRepositoryListener)
	 */
	@Override
	public void removeListener(IGCodeProviderRepositoryListener listener) throws GkException {
		listenerList.remove( listener );
	}

	/**
	 * Notify the listener that the given GCodeProvider was updated
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		if(isGcodeProviderUdateNotificationEnabled()){
			if (CollectionUtils.isNotEmpty(listenerList)) {
				for (IGCodeProviderRepositoryListener listener : listenerList) {
					listener.onGCodeProviderUpdate(provider);
				}
			}
		}
	}

	/**
	 * Notify the listener that the given GCodeProvider was locked
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyGCodeProviderLocked(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : listenerList) {
				listener.onGCodeProviderLocked(provider);
			}
		}
	}

	/**
	 * Notify the listener that the given GCodeProvider was unlocked
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyGCodeProviderUnlocked(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : listenerList) {
				listener.onGCodeProviderUnlocked(provider);
			}
		}
	}
	/**
	 * Notify the listener that the given GCodeProvider was created
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyGCodeProviderCreate(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : listenerList) {
				listener.onGCodeProviderCreate(provider);
			}
		}
	}

	/**
	 * Notify the listener that the given GCodeProvider is about to be deleted
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyBeforeGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : listenerList) {
				listener.beforeGCodeProviderDelete(provider);
			}
		}
	}
	
	/**
	 * Notify the listener that the given GCodeProvider was deleted
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyAfterGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : listenerList) {
				listener.afterGCodeProviderDelete(provider);
			}
		}
	}

	/**
	 * Notify the listener that the given modifier was created
	 * @param modifier the target provider
	 * @throws GkException GkException
	 */
	protected void notifyModifierCreate(IModifier<?> modifier) throws GkException {
		if (CollectionUtils.isNotEmpty(modifierListenerList)) {
			for (IModifierListener listener : modifierListenerList) {
				listener.onModifierCreate(modifier.getId());
			}
		}
	}
	
	/**
	 * Notify the listener that the given modifier was created
	 * @param modifier the target provider
	 * @throws GkException GkException
	 */
	protected void notifyModifierUpdate(IModifier<?> modifier) throws GkException {
		if (CollectionUtils.isNotEmpty(modifierListenerList)) {
			for (IModifierListener listener : modifierListenerList) {
				listener.onModifierUpdate(modifier.getId());
			}
		}
	}
	
	/**
	 * Notify the listener that the given modifier was created
	 * @param modifier the target provider
	 * @throws GkException GkException
	 */
	protected void notifyModifierDelete(IModifier<?> modifier) throws GkException {
		if (CollectionUtils.isNotEmpty(modifierListenerList)) {
			for (IModifierListener listener : modifierListenerList) {
				listener.onModifierDelete(modifier);
			}
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#clearAll()
	 */
	@Override
	public void clearAll() throws GkException {
		for (IGCodeProvider gcodeProvider : getGCodeProvider()) {
			deleteGCodeProvider(gcodeProvider.getId());
		}
		cacheModifiers.removeAll();		
		cacheStackedProviders.removeAll();
		cacheStackedProvidersByCode.removeAll();
	}

	/**
	 * @return the gcodeProviderUdateNotificationEnabled
	 */
	protected boolean isGcodeProviderUdateNotificationEnabled() {
		return gcodeProviderUdateNotificationEnabled;
	}

	/**
	 * @param gcodeProviderUdateNotificationEnabled the gcodeProviderUdateNotificationEnabled to set
	 */
	protected void setGcodeProviderUdateNotificationEnabled(boolean gcodeProviderUdateNotificationEnabled) {
		this.gcodeProviderUdateNotificationEnabled = gcodeProviderUdateNotificationEnabled;
	}

	protected void disableGcodeProviderUdateNotification(){
		setGcodeProviderUdateNotificationEnabled(false);
	}
	
	protected void enableGcodeProviderUdateNotification(){
		setGcodeProviderUdateNotificationEnabled(true);
	}
	
	/**
	 * Listener class in charge of propagating source change events
	 */
	class GCodeSourceWatcher implements IGCodeProviderSourceListener{
		/** The target provider */
		private Integer idGCodeProvider;
		
		
		/**
		 * @param idGCodeProvider the id of the target provider
		 */
		public GCodeSourceWatcher(Integer idGCodeProvider) {
			super();
			this.idGCodeProvider = idGCodeProvider;
		}


		/** (inheritDoc)
		 * @see org.goko.core.gcode.element.IGCodeProviderSourceListener#onSourceChanged(org.goko.core.gcode.element.IGCodeProviderSource)
		 */
		@Override
		public void onSourceChanged(IGCodeProviderSource taget) {
			try {				
				RS274NGCServiceImpl.this.reload(idGCodeProvider, null);
			} catch (GkException e) {
				LOG.error(e);
			}
		}
		
	}

	/**
	 * @return the renderingFormat
	 */
	public RenderingFormat getRenderingFormat() {
		return renderingFormat;
	}

	/**
	 * @param renderingFormat the renderingFormat to set
	 */
	public void setRenderingFormat(RenderingFormat renderingFormat) {
		this.renderingFormat = renderingFormat;	
	}
}

