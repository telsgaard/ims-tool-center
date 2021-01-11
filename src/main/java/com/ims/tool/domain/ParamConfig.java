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

@XmlRootElement(name = "template-parameters-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParamConfig {

	@XmlElement(name = "param")
	private List<Param> params;

	@XmlTransient
	private final Map<String, Param> paramMap;

	public ParamConfig() {
		paramMap = new HashMap<>();
	}

	public void setParam(String name, String value) {
		checkAndInitMap();
		paramMap.get(name).setValue(value);
	}

	public String getValue(String name) {
		checkAndInitMap();
		return paramMap.get(name).getValue();
	}

	public List<Param> getParams() {
		return params;
	}

	public List<String> getParamNames() {
		return params.stream().map(Param::getName).collect(Collectors.toList());
	}

	public void addParam(Param param) {
		params.add(param);
	}

	public void removeParam(Param param) {
		params.remove(param);
	}

	private void checkAndInitMap() {
		if (paramMap.isEmpty()) {
			paramMap.putAll(params.stream().collect(Collectors.toMap(p -> p.getName(), p -> p)));
		}
	}

}
