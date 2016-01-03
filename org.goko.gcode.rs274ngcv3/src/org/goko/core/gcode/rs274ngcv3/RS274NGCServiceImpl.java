package org.goko.core.gcode.rs274ngcv3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.utils.CacheByCode;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.SequentialIdGenerator;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionIterator;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.element.StackableGCodeProviderModifier;
import org.goko.core.gcode.rs274ngcv3.element.StackableGCodeProviderRoot;
import org.goko.core.gcode.rs274ngcv3.element.source.StringGCodeSource;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.InstructionFactory;
import org.goko.core.gcode.rs274ngcv3.instruction.executiontime.InstructionTimeCalculatorFactory;
import org.goko.core.gcode.rs274ngcv3.modifier.ModifierSorter;
import org.goko.core.gcode.rs274ngcv3.modifier.ModifierSorter.EnumModifierSortType;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeLexer;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeTokenType;
import org.goko.core.gcode.rs274ngcv3.parser.ModalGroup;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.log.GkLog;
import org.goko.core.math.BoundingTuple6b;
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 */
public class RS274NGCServiceImpl implements IRS274NGCService{
	private static final GkLog LOG = GkLog.getLogger(RS274NGCServiceImpl.class);
	/** The list of listener */
	private List<IGCodeProviderRepositoryListener> listenerList;
	/** The list of modal groups */
	private List<ModalGroup> modalGroups;
	/** The cache of providers */
	private CacheById<IStackableGCodeProvider> cacheProviders;
	/** The cache of providers */
	private CacheByCode<IStackableGCodeProvider> cacheProvidersByCode;
	/** The cache of modifiers */
	private CacheById<IModifier<GCodeProvider>> cacheModifiers;

	/** Constructor */
	public RS274NGCServiceImpl() {
		initializeModalGroups();
		this.listenerList 	= new ArrayList<IGCodeProviderRepositoryListener>();
		this.cacheProviders = new CacheById<IStackableGCodeProvider>(new SequentialIdGenerator());
		this.cacheProvidersByCode = new CacheByCode<IStackableGCodeProvider>();
		this.cacheModifiers = new CacheById<IModifier<GCodeProvider>>(new SequentialIdGenerator());
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
	public void start() throws GkException {
		LOG.info("Starting " + getServiceId());

		LOG.info("Successfully started " + getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		LOG.info("Stopping " + getServiceId());

		LOG.info("Successfully stopped " + getServiceId());
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#parse(java.io.InputStream)
	 */
	@Override
	public IGCodeProvider parse(IGCodeProviderSource source, IProgressMonitor monitor) throws GkException {
		GCodeProvider provider = new GCodeProvider();
		provider.setSource(source);
		GCodeLexer lexer = new GCodeLexer();
		List<List<GCodeToken>> tokens = lexer.tokenize(source.getInputStream());

		SubMonitor subMonitor = null;
		if(monitor != null){
			subMonitor = SubMonitor.convert(monitor,"Reading file", tokens.size());
		}

		for (List<GCodeToken> lstToken : tokens) {
			verifyModality(lstToken);
			GCodeLine line = buildLine(lstToken);
			provider.addLine(line);
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
		return provider;
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

		for (GCodeToken token : lstToken) {
//			if(token.getType() == GCodeTokenType.LINE_NUMBER){
//				line.setLineNumber(GCodeTokenUtils.getLineNumber(token));
//			}else
			if(token.getType() == GCodeTokenType.WORD || token.getType() == GCodeTokenType.LINE_NUMBER){
				line.addWord(new GCodeWord(StringUtils.substring(token.getValue(), 0, 1), StringUtils.substring(token.getValue(), 1)));
			}else if(token.getType() == GCodeTokenType.SIMPLE_COMMENT
					|| token.getType() == GCodeTokenType.MULTILINE_COMMENT){
				line.addWord(new GCodeWord(";", token.getValue()));
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
		}else{
			localContext = context;
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
					traceUnusedWords(localWords);
					break;
				}else{
					// Make sure we consumed at least one word
					if(localWords.size() == wordCountBefore){
						throw new GkTechnicalException("An instruction was created but no word was removed. Instruction created : "+instruction.getClass());
					}
				}

				instruction.setIdGCodeLine(gCodeLine.getId());
				iSet.addInstruction(instruction);
				// Update context for further instructions
				update(localContext, instruction);
			}
			instructionProvider.addInstructionSet(iSet);
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
			GCodeLine line = factory.getLine(context, instructionSet);

			provider.addLine(line);
		}

		return provider;
	}

	/**
	 * Trace the unused words in a line
	 * @param unusedWords the list of unused words
	 */
	private void traceUnusedWords(List<GCodeWord> unusedWords){
		String wordstr = "";
		for (GCodeWord gCodeWord : unusedWords) {
			wordstr += gCodeWord.completeString() + " ";
		}
		LOG.warn("GCodeWord not supported "+wordstr+". They will be present in the GCode file, but won't generate instruction");
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
	 * @see org.goko.core.execution.IGCodeExecutionTimeService#evaluateExecutionTime(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public Quantity<Time> evaluateExecutionTime(IGCodeProvider provider) throws GkException {
		Quantity<Time> result = NumberQuantity.zero(Units.SECOND);

		InstructionTimeCalculatorFactory timeFactory = new InstructionTimeCalculatorFactory();
		GCodeContext baseContext = new GCodeContext();
		InstructionProvider instructions = getInstructions(baseContext, provider);

		InstructionIterator iterator = getIterator(instructions, baseContext);

		GCodeContext preContext = null;

		while(iterator.hasNext()){
			preContext = new GCodeContext(iterator.getContext());
			result = result.add( timeFactory.getExecutionTime(preContext, iterator.next()) );
		}

		return result;
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#render(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	public String render(GCodeLine line) throws GkException {
		return render(line, RenderingFormat.DEFAULT);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#render(org.goko.core.gcode.element.GCodeLine, org.goko.core.gcode.rs274ngcv3.RenderingFormat)
	 */
	@Override
	public String render(GCodeLine line, RenderingFormat format) throws GkException {
		StringBuffer buffer = new StringBuffer();
		// FIXME find a better way to classify GCode words or describe a GCodeLine within the rs274 service
		GCodeWord commentWord = null;

		// Add words
		for (GCodeWord word : line.getWords()) {
			if(StringUtils.equals(word.getLetter(), "N")){
				if(!format.isSkipLineNumbers()){
					buffer.insert(0, word.getValue());
					buffer.insert(0, word.getLetter());
				}
				continue;
			}

			if(StringUtils.equals(word.getLetter(), ";")){
				if(!format.isSkipComments()){
					commentWord = word;
				}
				continue;
			}

			if(buffer.length() > 0){
				buffer.append(" ");
			}
			buffer.append(word.getLetter());
			buffer.append(word.getValue());
		}
		// Add comment
		if(commentWord != null){
			if(buffer.length() > 0){
				buffer.append(" ");
			}
			buffer.append(commentWord.getValue());
		}
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

			if(instruction.getType() == InstructionType.STRAIGHT_TRAVERSE
			|| instruction.getType() == InstructionType.STRAIGHT_FEED){
				AbstractStraightInstruction straightInstruction = (AbstractStraightInstruction) instruction;
				Tuple6b endpoint = new Tuple6b(straightInstruction.getX(),straightInstruction.getY(),straightInstruction.getZ(),straightInstruction.getA(),straightInstruction.getB(),straightInstruction.getC());
				min = min.min(endpoint);
				max = max.max(endpoint);
			}
		}

		return new BoundingTuple6b(min, max);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#getGCodeProvider(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider getGCodeProvider(Integer id) throws GkException {
		return cacheProviders.get(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#getGCodeProvider(java.lang.String)
	 */
	@Override
	public IGCodeProvider getGCodeProvider(String code) throws GkException {		
		return cacheProvidersByCode.get(code);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#getGCodeProvider()
	 */
	@Override
	public List<IGCodeProvider> getGCodeProvider() throws GkException {
		return new ArrayList<IGCodeProvider>(cacheProviders.get());
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#addGCodeProvider(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void addGCodeProvider(IGCodeProvider provider) throws GkException {
		StackableGCodeProviderRoot wrappedProvider = new StackableGCodeProviderRoot(provider);
		cacheProviders.add(wrappedProvider);
		cacheProvidersByCode.add(wrappedProvider);
		notifyGCodeProviderCreate(provider);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#deleteGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void deleteGCodeProvider(Integer id) throws GkException {
		IGCodeProvider provider = cacheProviders.get(id);
		performDeleteByIdGCodeProvider(id);
		cacheProviders.remove(id);
		cacheProvidersByCode.remove(provider.getCode());
		notifyGCodeProviderDelete(provider);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#lockGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void lockGCodeProvider(Integer idGcodeProvider) throws GkException {
		IGCodeProvider provider = cacheProviders.get(idGcodeProvider);
		if(!provider.isLocked()){
			provider.setLocked(true);
			notifyGCodeProviderLocked(provider);
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
		return cacheProviders.get(idGcodeProvider).isLocked();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#unlockGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void unlockGCodeProvider(Integer idGcodeProvider) throws GkException {
		IGCodeProvider provider = cacheProviders.get(idGcodeProvider);
		if(provider.isLocked()){
			provider.setLocked(false);
			notifyGCodeProviderUnlocked(provider);
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

		IStackableGCodeProvider baseProvider = this.cacheProviders.get(modifier.getIdGCodeProvider());
		StackableGCodeProviderModifier wrappedProvider = new StackableGCodeProviderModifier(baseProvider, modifier);
		wrappedProvider.update();
		this.cacheProviders.remove(baseProvider.getId());
		this.cacheProviders.add(wrappedProvider);
		notifyGCodeProviderUpdate(wrappedProvider);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#updateModifier(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public void updateModifier(IModifier<GCodeProvider> modifier) throws GkException {
		assertGCodeProviderUnlocked(modifier.getIdGCodeProvider());
		this.cacheModifiers.remove(modifier.getId());
		modifier.setModificationDate(new Date());
		this.cacheModifiers.add(modifier);
		IStackableGCodeProvider provider = this.cacheProviders.get(modifier.getIdGCodeProvider());
		provider.update();
		notifyGCodeProviderUpdate(provider);
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

		this.cacheModifiers.remove(idModifier);

		// Let's find the stacked provider with this modifier
		IStackableGCodeProvider gcode = cacheProviders.get(modifier.getIdGCodeProvider());
		IStackableGCodeProvider next = null;
		IStackableGCodeProvider previous = null;

		while(gcode != null && !ObjectUtils.equals(gcode.getIdModifier(), modifier.getId())){
			next = gcode;
			gcode = gcode.getParent();
		}

		// Original --...-> previous -->  gcode  --> next
		if(gcode != null){
			previous = gcode.getParent();
			if(next != null){
				next.setParent(previous);
			}else{
				IGCodeProvider tmpProvider = getGCodeProvider(modifier.getIdGCodeProvider());
				cacheProviders.remove(tmpProvider.getId());
				cacheProvidersByCode.remove(tmpProvider.getCode());				
				cacheProviders.add(previous);
				cacheProvidersByCode.add(previous);
			}
			gcode.setParent(null);
		}
		IStackableGCodeProvider targetProvider = cacheProviders.get(modifier.getIdGCodeProvider());
		notifyGCodeProviderUpdate(targetProvider);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#getModifier(java.lang.Integer)
	 */
	@Override
	public IModifier<GCodeProvider> getModifier(Integer id) throws GkException {
		return cacheModifiers.get(id);
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
		for (IModifier<GCodeProvider> iModifier : lstModifiers) {
			cacheModifiers.remove(iModifier);
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
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : listenerList) {
				listener.onGCodeProviderUpdate(provider);
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
	 * Notify the listener that the given GCodeProvider was deleted
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : listenerList) {
				listener.onGCodeProviderDelete(provider);
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
		cacheProviders.removeAll();
	}

}
