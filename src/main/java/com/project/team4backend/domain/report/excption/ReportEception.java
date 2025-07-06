package com.project.team4backend.domain.report.excption;

import com.project.team4backend.domain.report.entity.Report;
import com.project.team4backend.global.apiPayload.exception.CustomException;

public class ReportEception extends CustomException {
    public ReportEception(ReportErrorCode errorCode) {super(errorCode);}
}
