package fr.insee.pogues.transforms;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import fr.insee.eno.GenerationService;
import fr.insee.eno.generation.DDI2JSGenerator;
import fr.insee.eno.generation.DDI2ODTGenerator;
import fr.insee.eno.generation.IdentityGenerator;
import fr.insee.eno.postprocessing.JSExternalizeVariablesPostprocessor;
import fr.insee.eno.postprocessing.JSSortComponentsPostprocessor;
import fr.insee.eno.postprocessing.NoopPostprocessor;
import fr.insee.eno.postprocessing.Postprocessor;
import fr.insee.eno.preprocessing.DDI32ToDDI33Preprocessor;
import fr.insee.eno.preprocessing.DDIPreprocessor;


@Service
public class DDI32ToDDI33Impl implements DDI32ToDDI33 {
	
final static Logger logger = LogManager.getLogger(DDI32ToDDI33Impl.class);
	
	@Override
	public void transform(InputStream input, OutputStream output, Map<String, Object> params, String surveyName)
			throws Exception {
		logger.debug("Eno transformation");
		if (null == input) {
			throw new NullPointerException("Null input");
		}
		if (null == output) {
			throw new NullPointerException("Null output");
		}
		String odt = transform(input, params, surveyName);
		logger.debug("Eno transformation finished");
		output.write(odt.getBytes(StandardCharsets.UTF_8));
	}
	
	@Override
	public String transform(InputStream input, Map<String, Object> params, String surveyName) throws Exception {
		if (null == input) {
			throw new NullPointerException("Null input");
		}
		File enoInput;
		enoInput = File.createTempFile("eno", ".xml");
		FileUtils.copyInputStreamToFile(input, enoInput);
		return transform(enoInput, params, surveyName);
	}
	
	@Override
	public String transform(String input, Map<String, Object> params, String surveyName) throws Exception {
		File enoInput;
		if (null == input) {
			throw new NullPointerException("Null input");
		}
		enoInput = File.createTempFile("eno", ".xml");
		FileUtils.writeStringToFile(enoInput, input, StandardCharsets.UTF_8);
		return transform(enoInput, params, surveyName);
	}
	
	private String transform(File file, Map<String, Object> params, String surveyName) throws Exception {
		try {
            File output=null;
			GenerationService genService = new GenerationService(new DDI32ToDDI33Preprocessor(), new IdentityGenerator(), new Postprocessor[] {new NoopPostprocessor()});
            output = genService.generateQuestionnaire(file, surveyName);
            
            return FileUtils.readFileToString(output, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new Exception(String.format("%s:%s", getClass().getName(), e.getMessage()));
        }
	}	

}
