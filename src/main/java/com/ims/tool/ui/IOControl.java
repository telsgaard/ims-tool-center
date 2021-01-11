package com.ims.tool.ui;

import java.io.File;

public interface IOControl {

	void setResponseText(String text);

	void clear();

	void setRequestText(String requestText);

	void addTab();

	void addTab(File xmlFile);

	String getRequestText();

}
