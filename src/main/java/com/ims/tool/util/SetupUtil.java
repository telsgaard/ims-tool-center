package com.ims.tool.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.ims.tool.App;
import com.ims.tool.domain.ParamConfig;
import com.ims.tool.domain.ToolCenterConfig;
import com.ims.tool.domain.ToolConfig;

public final class SetupUtil {

	private static final Logger logger = Logger.getLogger(SetupUtil.class.getName());

	private static final String USER_HOME = "user.home";

	private static final ToolCenterConfig TOOL_CENTER_CONFIG;

	private static final ParamConfig PARAMETER_CONFIG;

	private static final File TOOL_CENTER_CONFIG_FILE;

	private static final File PARAM_CONFIG_FILE;

	public static ToolConfig getToolConfig() {
		return TOOL_CENTER_CONFIG.getToolConfig();
	}

	public static ParamConfig getParameterConfig() {
		return PARAMETER_CONFIG;
	}

	public static void updateParameterConfig() {
		XMLConversionUtil.pojoToXml(PARAMETER_CONFIG, ParamConfig.class, PARAM_CONFIG_FILE);
	}

	public static void updateToolConfig() {
		XMLConversionUtil.pojoToXml(TOOL_CENTER_CONFIG, ToolCenterConfig.class, TOOL_CENTER_CONFIG_FILE);
	}

	static {
		String configPath = System.getProperty(USER_HOME) + File.separator + "tool-center-config.xml";
		String paramPath = System.getProperty(USER_HOME) + File.separator + "tool-center-parameters.xml";

		TOOL_CENTER_CONFIG_FILE = new File(configPath);
		PARAM_CONFIG_FILE = new File(paramPath);

		setupConfig();
		setupParameters();

		TOOL_CENTER_CONFIG = XMLConversionUtil.xmlToPojo(ToolCenterConfig.class, TOOL_CENTER_CONFIG_FILE);
		PARAMETER_CONFIG = XMLConversionUtil.xmlToPojo(ParamConfig.class, PARAM_CONFIG_FILE);
	}

	private static void setupParameters() {
		if (!PARAM_CONFIG_FILE.exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(PARAM_CONFIG_FILE);
				InputStream configTemplate = App.class.getClassLoader().getResourceAsStream(ResourcePaths.PARAM_XML);
				IOUtils.copy(configTemplate, fos);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Problem in setting up parameter file.", e);
			}
		}
	}

	private static void setupConfig() {
		if (!TOOL_CENTER_CONFIG_FILE.exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(TOOL_CENTER_CONFIG_FILE);
				InputStream configTemplate = App.class.getClassLoader().getResourceAsStream(ResourcePaths.CONFIG_XML);
				IOUtils.copy(configTemplate, fos);
			} catch (IOException e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, "Problem in setting up config file.", e);
			}
		}
	}

}
