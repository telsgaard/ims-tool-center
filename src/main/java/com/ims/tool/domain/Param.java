package com.ims.tool.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "param")
@XmlAccessorType(XmlAccessType.FIELD)
public class Param {

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String value;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}