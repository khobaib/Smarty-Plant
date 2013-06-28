package com.mistersmartyplants.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerResponse {

    JSONObject jObj;
    JSONArray jArr;
    String str;
    
    int        status;

    public ServerResponse() {
        // TODO Auto-generated constructor stub
    }

    public ServerResponse(JSONObject jObj, int status) {
        this.jObj = jObj;
        this.status = status;
    }

    public ServerResponse(JSONArray jArr, int status) {
        this.jArr = jArr;
        this.status = status;
    }
    
    public ServerResponse(String str, int status) {
        this.str = str;
        this.status = status;
    }
    public JSONObject getjObj() {
        return jObj;
    }

    public void setjObj(JSONObject jObj) {
        this.jObj = jObj;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JSONArray getjArr() {
		return jArr;
	}
    public void setjArr(JSONArray jArr) {
		this.jArr = jArr;
	}
    
    public String getStr() {
		return str;
	}
    
    public void setStr(String str) {
		this.str = str;
	}
}
