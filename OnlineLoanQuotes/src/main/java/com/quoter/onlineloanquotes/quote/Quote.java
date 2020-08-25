package com.quoter.onlineloanquotes.quote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Quote {
    @ApiModelProperty(notes = "Amount to borrow", name = "amount", required = true)
    private final int amountRequested;
}
