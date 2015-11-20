package ingbank.com.tr.happybanking.common.request.map;

import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;

import ingbank.com.tr.happybanking.common.request.map.response.CompositionResponse;
import ingbank.com.tr.happybanking.map.model.map.ListAtmBranchResponse;

/**
 * Created by huseyinatasoy on 05/09/14.
 */
public class ListAtmBranchRequest extends CompositionRequest {
    @Override
    public Type getResponseType() {
        return new TypeToken<CompositionResponse<ListAtmBranchResponse>>() {
        }.getType();
    }
}
