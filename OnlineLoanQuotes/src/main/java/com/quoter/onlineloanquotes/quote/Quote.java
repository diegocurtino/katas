package com.quoter.onlineloanquotes.quote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Quote {
    @ApiModelProperty(value = "Amount to borrow", required = true, example = "1400")
    private final int amountRequested;
}
