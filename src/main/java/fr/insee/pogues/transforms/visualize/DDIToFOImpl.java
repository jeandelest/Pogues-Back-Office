package fr.insee.pogues.transforms.visualize;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.pogues.api.remote.eno.transforms.EnoClient;

@Service
@Slf4j
public class DDIToFOImpl implements DDIToFO {
	
	@Autowired
	EnoClient enoClient;

	@Override
	public void transform(InputStream input, OutputStream output, Map<String, Object> params, String surveyName)
			throws Exception {
		log.debug("Eno transformation");
		if (null == input) {
			throw new NullPointerException("Null input");
		}
		if (null == output) {
			throw new NullPointerException("Null output");
		}
		String odt = transform(input, params, surveyName);
		log.debug("Eno transformation finished");
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
			return enoClient.getDDIToFO(file);

		} catch (Exception e) {
			throw new Exception(String.format("%s:%s", getClass().getName(), e.getMessage()));
		}
	}
}
