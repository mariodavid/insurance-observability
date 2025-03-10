package com.insurance.common.response;

import java.util.List;

public record ErrorResponse(List<ErrorMessage> errors) {
}
