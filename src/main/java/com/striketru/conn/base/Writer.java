package com.striketru.conn.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.striketru.conn.base.model.BaseData;
import com.striketru.conn.base.model.Result;
import com.striketru.pim.model.ProductJson;

public abstract class Writer<W extends BaseData> {
	int MAX_PRODUCT_COUNT = 50;
	List<Result> result;
	public abstract void execute(W writeData);
	
	
	public List<String> getRequestList(List<ProductJson> productJsonList) {

		int count = 0;
		List<String> optionsList = new ArrayList<>();
		StringBuilder strbuild = new StringBuilder("");
		for (ProductJson productJson : productJsonList) {
			if (count == MAX_PRODUCT_COUNT) {
				optionsList.add(strbuild.toString());
				strbuild = new StringBuilder("");
				count = 0;
			}
			strbuild.append(productJson.createRequest()).append("\n");
			count++;
		}
		if (strbuild.length() > 2) { 
			optionsList.add(strbuild.toString());
		}
		return optionsList;
	}

}
