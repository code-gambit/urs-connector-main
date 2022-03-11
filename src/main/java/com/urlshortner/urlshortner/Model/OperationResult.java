package com.urlshortner.urlshortner.Model;

/**
 * Generic class for passing the result of any operations.
 * Currently, this class is used as the single source of return typ
 * for all the H2 database query.
 * @param <SUCCESSTYPE> data type for the success case
 * @param <FAILURETYPE> data type for the failure case
 */
public class OperationResult<SUCCESSTYPE, FAILURETYPE> {

    private OperationResult.TYPE type;
    private SUCCESSTYPE successData;
    private FAILURETYPE failureData;

    // returns the success data of type <SUCCESSTYPE>
    public SUCCESSTYPE getSuccessData() {
        return successData;
    }

    // return the failure data  of type <FAILURETYPE>
    public FAILURETYPE getFailureData() {
        return failureData;
    }

    // returns boolean if operation is failed
    public boolean isFailed() {
        return type == TYPE.FAILURE;
    }

    // returns boolean if the data range is exhausted
    public boolean isRangeExhausted() {
        return type == TYPE.RANGE_EXHAUSTED;
    }

    // This is the static builder for the success operation
    public static <T, O> OperationResult<T, O> SUCCESS(T data) {
        OperationResult<T, O> result = new OperationResult<>();
        result.type = TYPE.SUCCESS;
        result.successData = data;
        result.failureData = null;
        return result;
    }

    // This is the static builder for the failure operation
    public static <T, O> OperationResult<T, O> FAILURE(O data) {
        OperationResult<T, O> result = new OperationResult<>();
        result.type = TYPE.FAILURE;
        result.successData = null;
        result.failureData = data;
        return result;
    }

    // This is the static builder for the range exhausted operation
    public static <T, O> OperationResult<T, O> RANGE_EXHAUSTED() {
        OperationResult<T, O> result = new OperationResult<>();
        result.type = TYPE.RANGE_EXHAUSTED;
        return result;
    }

    public enum TYPE {
        FAILURE,
        SUCCESS,
        RANGE_EXHAUSTED
    }

}
