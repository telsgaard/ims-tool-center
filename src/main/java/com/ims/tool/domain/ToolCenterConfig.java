package com.ims.tool.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tool-center-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolCenterConfig {

	@XmlElement(name = "tool-config")
	private ToolConfig toolConfig;

	public ToolConfig getToolConfig() {
		return toolConfig;
	}

}