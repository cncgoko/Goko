package org.goko.core.gcode.rs274ngcv3;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.element.IInstructionProvider;
import org.goko.core.gcode.element.IInstructionSet;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.instruction.InstructionFactory;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeLexer;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeTokenType;
import org.goko.core.gcode.rs274ngcv3.parser.ModalGroup;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeTokenUtils;
import org.goko.core.log.GkLog;

public class RS274NGCServiceImpl implements IRS274NGCService{
	private static final GkLog LOG = GkLog.getLogger(RS274NGCServiceImpl.class);
	/** The list of modal groups */
	private List<ModalGroup> modalGroups;

	/** Constructor */
	public RS274NGCServiceImpl() {
		initializeModalGroups();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IGCodeService#parse(java.io.InputStream)
	 */
	@Override
	public IGCodeProvider parse(InputStream inputStream) throws GkException {
		GCodeProvider provider = new GCodeProvider(); 
		GCodeLexer lexer = new GCodeLexer();
		List<List<GCodeToken>> tokens = lexer.tokenize(inputStream);
		
		for (List<GCodeToken> lstToken : tokens) {
			verifyModality(lstToken);
			provider.addLine(buildLine(lstToken));
		}
		
		return provider;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#parse(java.lang.String)
	 */
	@Override
	public IGCodeProvider parse(String inputString) throws GkException {
		return parse(new ByteArrayInputStream(inputString.getBytes()));
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#parseLine(java.lang.String)
	 */
	@Override
	public GCodeLine parseLine(String inputString) throws GkException {
		IGCodeProvider provider = parse(new ByteArrayInputStream(inputString.getBytes()));
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
			if(token.getType() == GCodeTokenType.LINE_NUMBER){
				line.setLineNumber(GCodeTokenUtils.getLineNumber(token));
			}else if(token.getType() == GCodeTokenType.WORD){
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
	public IInstructionProvider getInstructions(final GCodeContext context, IGCodeProvider gcodeProvider) throws GkException {
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
				IInstruction instruction = factory.build(localContext, localWords);
				if(instruction == null){
					// We have words is the list, but we can't build any instruction from them. End while loop
					traceUnusedWords(localWords);
					break;
				}
				iSet.addInstruction(instruction);
				// Update context for further instructions
				update(localContext, instruction);
			}
			instructionProvider.addInstructionSet(iSet);
		}
		
		return instructionProvider;
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
		this.modalGroups.add( new ModalGroup("G0", "G1", "G2", "G3", "G38.2", "G80", "G81", "G82", "G83", "G84", "G85", "G86", "G87", "G88", "G89" ));

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
		this.modalGroups.add( new ModalGroup("M3","M4","M5"));
		this.modalGroups.add( new ModalGroup("M7","M9"));
		this.modalGroups.add( new ModalGroup("M8","M9"));
		this.modalGroups.add( new ModalGroup("M48", "M49"));
	}	
	
	public GCodeContext update(GCodeContext baseContext, IInstruction instruction) throws GkException {
		// FIXME a faire
		return null;
	};
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeService#update(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstructionSet)
	 */
	@Override
	public GCodeContext update(GCodeContext baseContext, IInstructionSet instructionSet) throws GkException {
		GCodeContext result = baseContext;
		List<IInstruction> instructions = instructionSet.getInstructions();
		if(CollectionUtils.isNotEmpty(instructions)){
			for (IInstruction instruction : instructions) {
				result = update(result, instruction);
			}
		}
		return result;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.IRS274NGCService#toString(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	public String toString(GCodeLine line) throws GkException {
		StringBuffer buffer = new StringBuffer();
		List<GCodeWord> lstWords = line.getWords();
		// FIXME : add line number support
		// FIXME : add parameters support
		if(CollectionUtils.isNotEmpty(lstWords)){
			for (GCodeWord gCodeWord : lstWords) {
				buffer.append(gCodeWord);
			}
		}
		return buffer.toString();
	}
}