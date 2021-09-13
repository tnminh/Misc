package com.home.request;

import java.util.ArrayList;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PoolData {
    @NotNull(message = "missing poolId")
    public Integer poolId;
    @NotEmpty
    public ArrayList<Integer> poolValues;
}
