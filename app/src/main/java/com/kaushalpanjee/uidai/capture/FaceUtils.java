package com.kaushalpanjee.uidai.capture;

import java.util.List;

public class FaceUtils {

    public static Long startTime = 0L;
    public static Long stopTime = 0L;

    public static Long returnTotalTime() {
        return stopTime - startTime;
    }

    public static String formatCaptureResponse(String txn, CaptureResponse response) {
        Long serverComputeTime = getTimeValueFromCustOption(response.custOpts.nameValues, "serverComputeTime");
        Long clientComputeTime = getTimeValueFromCustOption(response.custOpts.nameValues, "clientComputeTime");
        Long networkLatenecyTime = getTimeValueFromCustOption(response.custOpts.nameValues, "networkLatencyTime");


        return (response.resp.errCode == 0) ? "Capture of the image and face liveness was successful for transaction - " + txn + " and \nTotal duration in AUA App " + convertToSeconds(returnTotalTime()) : "Capture failed for transaction - " + txn + " with error :" + response.resp.errCode + " - " + response.resp.errInfo + " and \nTotal duration in AUA App " + convertToSeconds(returnTotalTime());
    }

    public static String formatEkycCaptureResponse() {
        return convertToSeconds(returnTotalTime());
    }


    private static Long getTimeValueFromCustOption(List<NameValue> nameValues, String key) {
        try {
            for (NameValue param : nameValues) {
                if (param.getName().equals(key)) {
                    return Long.parseLong(nameValues.get(0).getValue());
                }
            }
            return -1L;
        } catch (Exception e) {
            return -1L;
        }
    }

    private static String convertToSeconds(Long timeInMillis) {
        return (timeInMillis / 1000f) + " secs";
    }
}