package com.ims.tool.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "tool-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolConfig {

	public enum ConfigParams {
		URL, USER, PASS;

		public String toString() {
			return name().toLowerCase();
		}
	}

	@XmlElement(name = "param")
	private List<Param> params;

	@XmlTransient
	private final Map<String, Param> paramMap;

	public ToolConfig() {
		paramMap = new HashMap<>();
	}

	public void set(ConfigParams configParams, String value) {
		checkAndInitMap();
		addParamIfNotExist(configParams);
		paramMap.get(configParams.toString()).setValue(value);
	}

	public String get(ConfigParams configParams) {
		checkAndInitMap();
		addParamIfNotExist(configParams);
		return paramMap.get(configParams.toString()).getValue();
	}

	private void addParamIfNotExist(ConfigParams configParams) {
		if (!paramMap.containsKey(configParams.toString())) {
			Param param = new Param();
			param.setValue("");
			param.setName(configParams.toString());

			params.add(param);
			paramMap.put(configParams.toString(), param);
		}
	}

	private void checkAndInitMap() {
		if (paramMap.isEmpty()) {
			paramMap.putAll(params.stream().collect(Collectors.toMap(p -> p.getName(), p -> p)));
		}
	}

}