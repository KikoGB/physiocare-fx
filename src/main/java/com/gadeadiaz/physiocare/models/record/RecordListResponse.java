package com.gadeadiaz.physiocare.models.record;

import com.gadeadiaz.physiocare.models.BaseResponse;
import com.gadeadiaz.physiocare.models.Record;

import java.util.List;

public class RecordListResponse extends BaseResponse {
    private List<Record> result;

    public List<Record> getResult() {
        return result;
    }
}
