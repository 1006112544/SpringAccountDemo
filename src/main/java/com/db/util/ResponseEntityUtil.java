package com.db.util;

import com.db.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

    public static ResponseEntity<ResponseModel> createResponse(Integer code,String message,Object data){
        ResponseModel resModel = new ResponseModel();
        resModel.setCode(code);
        resModel.setData(data);
        resModel.setMessage(message);
        return new ResponseEntity<ResponseModel>(resModel,HttpStatus.OK);
    }

    public static ResponseEntity<ResponseModel> createResponse(Integer code, String message) {
        ResponseModel res = new ResponseModel();
        res.setCode(code);
        res.setMessage(message);
        res.setData(null);
        return new ResponseEntity<ResponseModel>(res, HttpStatus.OK);
    }
}
